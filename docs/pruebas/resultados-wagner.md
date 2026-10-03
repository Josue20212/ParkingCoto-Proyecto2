# Resultados de pruebas de Wagner

55 pruebas: 50 APROBADAS, 5 FALLIDAS

Fechas controladas: 2026-10-01 08:00. Caracterizaciones >24 h: APROBADO significa que coincide con la implementación observada; el requisito debe confirmarse.

| Caso | Entrada | Resultado esperado | Resultado obtenido | Estado |
| --- | --- | --- | --- | --- |
| Ingreso AUTOMOVIL (obligatorio 1-3) | Vehículo registrado, E1 disponible AUTOMOVIL | Ticket ACTIVO, tipo AUTOMOVIL, espacio OCUPADO | ACTIVO / AUTOMOVIL / OCUPADO | APROBADO |
| Ingreso MOTOCICLETA (obligatorio 1-3) | Vehículo registrado, E1 disponible MOTOCICLETA | Ticket ACTIVO, tipo MOTOCICLETA, espacio OCUPADO | ACTIVO / MOTOCICLETA / OCUPADO | APROBADO |
| Ingreso CARGA (obligatorio 1-3) | Vehículo registrado, E1 disponible CARGA | Ticket ACTIVO, tipo CARGA, espacio OCUPADO | ACTIVO / CARGA / OCUPADO | APROBADO |
| 4. Espacio ocupado | E1 ocupado; ocupar y asignar a segundo automóvil | Dos rechazos; un ticket activo | Ambas operaciones rechazadas; un ticket; OCUPADO | APROBADO |
| 5. Fuera de servicio | E1 FUERA_DE_SERVICIO; ocupar y registrar ingreso | Rechazos; cero tickets | Rechazado; FUERA_DE_SERVICIO; cero tickets | APROBADO |
| 6. Incompatible AUTOMOVIL/MOTOCICLETA | Solo espacio MOTOCICLETA | esCompatible false y selección/ingreso rechazados | false; selección e ingreso rechazados; DISPONIBLE | APROBADO |
| 6. Incompatible AUTOMOVIL/CARGA | Solo espacio CARGA | esCompatible false y selección/ingreso rechazados | false; selección e ingreso rechazados; DISPONIBLE | APROBADO |
| 6. Incompatible MOTOCICLETA/AUTOMOVIL | Solo espacio AUTOMOVIL | esCompatible false y selección/ingreso rechazados | false; selección e ingreso rechazados; DISPONIBLE | APROBADO |
| 6. Incompatible MOTOCICLETA/CARGA | Solo espacio CARGA | esCompatible false y selección/ingreso rechazados | false; selección e ingreso rechazados; DISPONIBLE | APROBADO |
| 6. Incompatible CARGA/AUTOMOVIL | Solo espacio AUTOMOVIL | esCompatible false y selección/ingreso rechazados | false; selección e ingreso rechazados; DISPONIBLE | APROBADO |
| 6. Incompatible CARGA/MOTOCICLETA | Solo espacio MOTOCICLETA | esCompatible false y selección/ingreso rechazados | false; selección e ingreso rechazados; DISPONIBLE | APROBADO |
| 7. Segundo ingreso activo | ABC123 activo; E2 disponible; ingreso abc123 | Rechazo por ticket activo; E2 disponible | Rechazado; un ticket activo; E2 DISPONIBLE | APROBADO |
| 8. Duración 1 minutos | Automóvil; entrada fija + 1 min | 1 horas; CRC 900.0 | 1 horas; CRC 900.0 | APROBADO |
| 9. Duración 60 minutos | Automóvil; entrada fija + 60 min | 1 horas; CRC 900.0 | 1 horas; CRC 900.0 | APROBADO |
| 10. Duración 61 minutos | Automóvil; entrada fija + 61 min | 2 horas; CRC 1800.0 | 2 horas; CRC 1800.0 | APROBADO |
| 11. Máximo diario AUTOMOVIL 10 h | AUTOMOVIL; 10 horas | CRC 7000.0 | CRC 7000.0 | APROBADO |
| 11. Máximo diario AUTOMOVIL 11 h | AUTOMOVIL; 11 horas | CRC 7000.0 | CRC 7000.0 | APROBADO |
| 11. Máximo diario AUTOMOVIL 24 h | AUTOMOVIL; 24 horas | CRC 7000.0 | CRC 7000.0 | APROBADO |
| Adicional. Tope diario AUTOMOVIL 8 h | AUTOMOVIL; 8 horas | CRC 7000.0 (interpretación de máximo diario) | java.lang.AssertionError: Esperado: 7000.0; obtenido: 7200.0 | FALLIDO |
| Adicional. Tope diario AUTOMOVIL 9 h | AUTOMOVIL; 9 horas | CRC 7000.0 (interpretación de máximo diario) | java.lang.AssertionError: Esperado: 7000.0; obtenido: 8100.0 | FALLIDO |
| Caracterización >24 h AUTOMOVIL 25 h | AUTOMOVIL; 25 horas | Comportamiento actual por bloques de 24 h; regla pendiente de Kendall | CRC 7900.0; caracterización, no validación del enunciado | APROBADO |
| Caracterización >24 h AUTOMOVIL 34 h | AUTOMOVIL; 34 horas | Comportamiento actual por bloques de 24 h; regla pendiente de Kendall | CRC 14000.0; caracterización, no validación del enunciado | APROBADO |
| Caracterización >24 h AUTOMOVIL 48 h | AUTOMOVIL; 48 horas | Comportamiento actual por bloques de 24 h; regla pendiente de Kendall | CRC 14000.0; caracterización, no validación del enunciado | APROBADO |
| 11. Máximo diario MOTOCICLETA 10 h | MOTOCICLETA; 10 horas | CRC 4000.0 | CRC 4000.0 | APROBADO |
| 11. Máximo diario MOTOCICLETA 11 h | MOTOCICLETA; 11 horas | CRC 4000.0 | CRC 4000.0 | APROBADO |
| 11. Máximo diario MOTOCICLETA 24 h | MOTOCICLETA; 24 horas | CRC 4000.0 | CRC 4000.0 | APROBADO |
| Adicional. Tope diario MOTOCICLETA 8 h | MOTOCICLETA; 8 horas | CRC 4000.0 (interpretación de máximo diario) | CRC 4000.0 | APROBADO |
| Adicional. Tope diario MOTOCICLETA 9 h | MOTOCICLETA; 9 horas | CRC 4000.0 (interpretación de máximo diario) | java.lang.AssertionError: Esperado: 4000.0; obtenido: 4500.0 | FALLIDO |
| Caracterización >24 h MOTOCICLETA 25 h | MOTOCICLETA; 25 horas | Comportamiento actual por bloques de 24 h; regla pendiente de Kendall | CRC 4500.0; caracterización, no validación del enunciado | APROBADO |
| Caracterización >24 h MOTOCICLETA 34 h | MOTOCICLETA; 34 horas | Comportamiento actual por bloques de 24 h; regla pendiente de Kendall | CRC 8000.0; caracterización, no validación del enunciado | APROBADO |
| Caracterización >24 h MOTOCICLETA 48 h | MOTOCICLETA; 48 horas | Comportamiento actual por bloques de 24 h; regla pendiente de Kendall | CRC 8000.0; caracterización, no validación del enunciado | APROBADO |
| 11. Máximo diario CARGA 10 h | CARGA; 10 horas | CRC 11000.0 | CRC 11000.0 | APROBADO |
| 11. Máximo diario CARGA 11 h | CARGA; 11 horas | CRC 11000.0 | CRC 11000.0 | APROBADO |
| 11. Máximo diario CARGA 24 h | CARGA; 24 horas | CRC 11000.0 | CRC 11000.0 | APROBADO |
| Adicional. Tope diario CARGA 8 h | CARGA; 8 horas | CRC 11000.0 (interpretación de máximo diario) | java.lang.AssertionError: Esperado: 11000.0; obtenido: 12000.0 | FALLIDO |
| Adicional. Tope diario CARGA 9 h | CARGA; 9 horas | CRC 11000.0 (interpretación de máximo diario) | java.lang.AssertionError: Esperado: 11000.0; obtenido: 13500.0 | FALLIDO |
| Caracterización >24 h CARGA 25 h | CARGA; 25 horas | Comportamiento actual por bloques de 24 h; regla pendiente de Kendall | CRC 12500.0; caracterización, no validación del enunciado | APROBADO |
| Caracterización >24 h CARGA 34 h | CARGA; 34 horas | Comportamiento actual por bloques de 24 h; regla pendiente de Kendall | CRC 22000.0; caracterización, no validación del enunciado | APROBADO |
| Caracterización >24 h CARGA 48 h | CARGA; 48 horas | Comportamiento actual por bloques de 24 h; regla pendiente de Kendall | CRC 22000.0; caracterización, no validación del enunciado | APROBADO |
| 12. Cierre correcto | Automóvil; salida a los 61 min | CERRADO; salida fija; CRC 1800; espacio disponible | CERRADO; 2026-10-01T09:01; CRC 1800; DISPONIBLE | APROBADO |
| 13. Pago EFECTIVO | Ticket cerrado CRC 900; EFECTIVO | Pago vinculado, CRC 900, tipo correcto; PAGADO | Pago creado; CRC 900; EFECTIVO; PAGADO | APROBADO |
| 13. Pago TARJETA | Ticket cerrado CRC 900; TARJETA | Pago vinculado, CRC 900, tipo correcto; PAGADO | Pago creado; CRC 900; TARJETA; PAGADO | APROBADO |
| 13. Pago SINPE_MOVIL | Ticket cerrado CRC 900; SINPE_MOVIL | Pago vinculado, CRC 900, tipo correcto; PAGADO | Pago creado; CRC 900; SINPE_MOVIL; PAGADO | APROBADO |
| 14. Liberación y reutilización | Salida válida; segundo vehículo usa E1 | DISPONIBLE tras salida; nuevo ingreso en E1 | DISPONIBLE tras salida; E1 reutilizado | APROBADO |
| 15. Ingresos totales | Pagos automóvil 900, moto 500, carga 1500; un ticket sin pagar | Suma exacta CRC 2900 | CRC 2900.0 | APROBADO |
| Adicional. Salida sin activo | Vehículo registrado sin ingreso | Rechazo controlado | Rechazado; espacio disponible | APROBADO |
| Adicional. Pagar ACTIVO | Ticket activo | Rechazo; ACTIVO; ingresos cero | Rechazado; ACTIVO; CRC 0 | APROBADO |
| Adicional. Doble pago | Ticket ya pagado CRC 900 | Rechazo; ingresos siguen en CRC 900 | Rechazado; PAGADO; CRC 900 | APROBADO |
| Adicional. Placa duplicada | ABC123 registrada; registrar abc123 | Rechazo; un vehículo registrado | Rechazado; un vehículo | APROBADO |
| Adicional. Espacio duplicado | E1 registrado; registrar e1 | Rechazo; un espacio disponible | Rechazado; un espacio | APROBADO |
| Adicional. Vehículo inexistente | Ingreso NOEXISTE | Rechazo; cero tickets y espacio disponible | Búsqueda mediante registrarIngreso rechazada; cero tickets | APROBADO |
| Adicional. Salida inválida -1 min | Salida = entrada -1 min | Rechazo; ticket intacto y espacio ocupado | Rechazado; ACTIVO; salida null; CRC 0; OCUPADO | APROBADO |
| Adicional. Salida inválida 0 min | Salida = entrada 0 min | Rechazo; ticket intacto y espacio ocupado | Rechazado; ACTIVO; salida null; CRC 0; OCUPADO | APROBADO |
| Adicional. Fuera de servicio ocupado | E1 ocupado; ponerFueraDeServicio | Rechazo; OCUPADO | Rechazado; OCUPADO | APROBADO |
| Adicional. Sin espacios | Vehículo registrado; cero espacios | Rechazo; cero tickets | Rechazado; cero tickets | APROBADO |
