# Entrega de pruebas — Wagner

Rama: `test/wagner-reglas-negocio`. Sin merge a main.

## Ejecución

Desde la raíz del repositorio:

```powershell
powershell -NoProfile -ExecutionPolicy Bypass -File scripts/ejecutar-pruebas.ps1
```

Requiere JDK 17 o posterior con `java` y `javac` en PATH. Verificado con JDK 21.0.6, compilando con `--release 17`. No se dispone de Maven en PATH y el pom no declara JUnit. No se modificaron dependencias. La suite usa verificaciones explícitas y no necesita `-ea`; cada escenario crea su propio parqueo. No utiliza el menú ni `LocalDateTime.now()`.

El script compila las clases de dominio y pruebas en `target/pruebas-wagner`, ejecuta todos los escenarios aunque haya fallas y regenera `docs/pruebas/resultados-wagner.md`. Sale con código 1 si hay fallas y 0 si todas pasan. **Estas pruebas no se ejecutan automáticamente con `mvn test`**: integrar JUnit queda pendiente de coordinación con Kendall.

Resultado: **55 pruebas, 50 aprobadas y 5 fallidas**. Los 15 casos obligatorios están cubiertos y sus variantes pasan. De las 50 aprobadas, 9 son caracterizaciones de duraciones mayores de 24 horas; no certifican conformidad con una regla que todavía debe confirmarse.

Se probaron los tres ingresos, ocupación, espacios fuera de servicio, todas las combinaciones incompatibles, doble ingreso con otro espacio disponible, duraciones de 1/60/61 minutos, topes a las 10/11/24 horas para los tres vehículos, cierre, los tres tipos de pago, reutilización e ingresos exactos. También se cubrieron todos los casos adicionales solicitados. La búsqueda de vehículo es privada y se prueba mediante `registrarIngreso` con placa inexistente.

## Hallazgo a revisar con Kendall: máximo diario antes de 10 horas

Los siguientes resultados violan la interpretación de “tarifa máxima diaria” como límite de cobro dentro de un período de 24 horas. Si el enunciado completo exige aplicar la tarifa plana exclusivamente a partir de 10 horas, Kendall debe confirmar esa excepción antes de cambiar la lógica o las expectativas de estos casos adicionales. El enunciado completo no está disponible en este repositorio.

| Caso | Entrada | Esperado bajo un tope diario | Obtenido | Clase/método posiblemente responsable |
| --- | --- | --- | --- | --- |
| Automóvil, 8 h | 2026-10-01 08:00 → 16:00 | ₡7000 | ₡7200 | `Vehiculo.calcularMonto(long)` |
| Automóvil, 9 h | 2026-10-01 08:00 → 17:00 | ₡7000 | ₡8100 | `Vehiculo.calcularMonto(long)` |
| Motocicleta, 9 h | 2026-10-01 08:00 → 17:00 | ₡4000 | ₡4500 | `Vehiculo.calcularMonto(long)` |
| Carga, 8 h | 2026-10-01 08:00 → 16:00 | ₡11000 | ₡12000 | `Vehiculo.calcularMonto(long)` |
| Carga, 9 h | 2026-10-01 08:00 → 17:00 | ₡11000 | ₡13500 | `Vehiculo.calcularMonto(long)` |

La implementación multiplica las horas restantes por la tarifa horaria cuando son menores que 10; únicamente aplica el máximo cuando alcanzan 10. Por eso el cobro incluso disminuye entre 9 y 10 horas. No se corrigió la lógica: se dejan las pruebas fallidas y la evidencia para revisión de Kendall.

## Ambigüedad para más de 24 horas

La implementación primero redondea la permanencia a horas enteras y luego divide esas horas en bloques de 24. Cobra un máximo por bloque completo y calcula el resto con la regla de 10 horas. Observaciones:

| Duración | Motocicleta | Automóvil | Carga |
| --- | --- | --- | --- |
| 25 h | ₡4500 | ₡7900 | ₡12500 |
| 34 h | ₡8000 | ₡14000 | ₡22000 |
| 48 h | ₡8000 | ₡14000 | ₡22000 |

Las pruebas de estas duraciones están marcadas como caracterización. Kendall debe confirmar si el enunciado usa bloques desde la entrada, días calendario u otra regla. No se inventó ni implementó una nueva regla.

## Archivos entregados

- `src/test/java/com/mycompany/parkingcoto/ReglasNegocioTest.java`: suite sin dependencias.
- `scripts/ejecutar-pruebas.ps1`: compilación, ejecución y código de salida.
- `docs/pruebas/resultados-wagner.md`: tabla completa generada con entrada, esperado, obtenido y estado.
- `docs/pruebas/entrega-kendall.md`: resumen, hallazgos y pendientes de coordinación.

Las clases compiladas quedan excluidas mediante las reglas existentes de `.gitignore`.

No se modificaron clases principales ni `pom.xml`. No se realizó merge ni push. Esta entrega queda preparada para revisión; no se ha enviado ningún mensaje externo a Kendall.

Los commits de esta rama separan la suite (`test: add business rule and edge case suite`) y la documentación (`docs: record Wagner test results for Kendall`). Sus identificadores se pueden consultar con `git log -2 --oneline`.
