package com.mycompany.parkingcoto;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/** Suite independiente sin dependencias. No requiere habilitar las aserciones de Java. */
public final class ReglasNegocioTest {
    private static final LocalDateTime ENTRADA = LocalDateTime.of(2026, 10, 1, 8, 0);
    private static final List<String> FILAS = new ArrayList<>();
    private static int fallidos;

    @FunctionalInterface
    private interface Caso { String ejecutar(); }

    private record Contexto(Parqueo parqueo, Vehiculo vehiculo, EspacioParqueo espacio) { }

    private static Vehiculo vehiculo(TipoEspacio tipo, String placa) {
        return switch (tipo) {
            case AUTOMOVIL -> new Automovil(placa, "Marca", "Modelo", "Azul");
            case MOTOCICLETA -> new Motocicleta(placa, "Marca", "Modelo", "Azul");
            case CARGA -> new VehiculoCarga(placa, "Marca", "Modelo", "Azul");
        };
    }

    private static Contexto contexto(TipoEspacio tipo) {
        Parqueo parqueo = new Parqueo("Prueba");
        Vehiculo vehiculo = vehiculo(tipo, "ABC123");
        EspacioParqueo espacio = new EspacioParqueo("E1", tipo);
        parqueo.registrarVehiculo(vehiculo);
        parqueo.registrarEspacio(espacio);
        return new Contexto(parqueo, vehiculo, espacio);
    }

    private static TicketParqueo ingresar(Contexto c) {
        return c.parqueo.registrarIngreso(c.vehiculo.getPlaca(), ENTRADA);
    }

    private static TicketParqueo salir(Contexto c, long minutos) {
        ingresar(c);
        return c.parqueo.registrarSalida(c.vehiculo.getPlaca(), ENTRADA.plusMinutes(minutos));
    }

    private static void igual(Object esperado, Object obtenido) {
        if (!java.util.Objects.equals(esperado, obtenido)) {
            throw new AssertionError("Esperado: " + esperado + "; obtenido: " + obtenido);
        }
    }

    private static void rechazo(Class<? extends RuntimeException> tipo, Runnable accion) {
        try { accion.run(); }
        catch (RuntimeException e) {
            if (tipo.isInstance(e) && e.getMessage() != null && !e.getMessage().isBlank()) return;
            throw new AssertionError("Rechazo incorrecto: " + e);
        }
        throw new AssertionError("Se aceptó la operación que debía rechazarse");
    }

    private static String celda(String texto) {
        return texto.replace("|", "\\|").replace("\n", " ").replace("\r", " ");
    }

    private static void probar(String caso, String entrada, String esperado, Caso prueba) {
        String obtenido;
        String estado = "APROBADO";
        try { obtenido = prueba.ejecutar(); }
        catch (AssertionError | RuntimeException e) {
            obtenido = e.toString(); estado = "FALLIDO"; fallidos++;
        }
        FILAS.add("| " + celda(caso) + " | " + celda(entrada) + " | " + celda(esperado)
                + " | " + celda(obtenido) + " | " + estado + " |");
        System.out.println(estado + " - " + caso + ": " + obtenido);
    }

    public static void main(String[] args) throws Exception {
        for (TipoEspacio tipo : TipoEspacio.values()) {
            probar("Ingreso " + tipo + " (obligatorio 1-3)", "Vehículo registrado, E1 disponible " + tipo,
                    "Ticket ACTIVO, tipo " + tipo + ", espacio OCUPADO", () -> {
                Contexto c = contexto(tipo); TicketParqueo t = ingresar(c);
                igual(EstadoTicket.ACTIVO, t.getEstado()); igual(tipo, t.getEspacio().getTipo());
                igual(EstadoEspacio.OCUPADO, c.espacio.getEstado()); igual(c.vehiculo, t.getVehiculo());
                igual(ENTRADA, t.getFechaHoraEntrada()); igual(1, c.parqueo.obtenerTicketsActivos().size());
                return "ACTIVO / " + tipo + " / OCUPADO";
            });
        }
        probar("4. Espacio ocupado", "E1 ocupado; ocupar y asignar a segundo automóvil", "Dos rechazos; un ticket activo", () -> {
            Contexto c = contexto(TipoEspacio.AUTOMOVIL); ingresar(c);
            rechazo(IllegalStateException.class, c.espacio::ocupar);
            c.parqueo.registrarVehiculo(vehiculo(TipoEspacio.AUTOMOVIL, "XYZ456"));
            rechazo(IllegalStateException.class, () -> c.parqueo.registrarIngreso("XYZ456", ENTRADA));
            igual(1, c.parqueo.obtenerTicketsActivos().size()); igual(EstadoEspacio.OCUPADO, c.espacio.getEstado());
            return "Ambas operaciones rechazadas; un ticket; OCUPADO";
        });
        probar("5. Fuera de servicio", "E1 FUERA_DE_SERVICIO; ocupar y registrar ingreso", "Rechazos; cero tickets", () -> {
            Contexto c = contexto(TipoEspacio.AUTOMOVIL); c.espacio.ponerFueraDeServicio();
            rechazo(IllegalStateException.class, c.espacio::ocupar);
            rechazo(IllegalStateException.class, () -> ingresar(c));
            igual(EstadoEspacio.FUERA_DE_SERVICIO, c.espacio.getEstado()); igual(0, c.parqueo.obtenerTicketsActivos().size());
            return "Rechazado; FUERA_DE_SERVICIO; cero tickets";
        });
        for (TipoEspacio tipo : TipoEspacio.values()) {
            for (TipoEspacio incompatible : TipoEspacio.values()) {
                if (tipo == incompatible) continue;
                probar("6. Incompatible " + tipo + "/" + incompatible, "Solo espacio " + incompatible,
                        "esCompatible false y selección/ingreso rechazados", () -> {
                    Parqueo p = new Parqueo("Prueba"); Vehiculo v = vehiculo(tipo, "ABC123");
                    EspacioParqueo e = new EspacioParqueo("E1", incompatible);
                    p.registrarVehiculo(v); p.registrarEspacio(e); igual(false, e.esCompatible(v));
                    rechazo(IllegalStateException.class, () -> p.buscarEspacioCompatible(v));
                    rechazo(IllegalStateException.class, () -> p.registrarIngreso(v.getPlaca(), ENTRADA));
                    igual(EstadoEspacio.DISPONIBLE, e.getEstado()); igual(0, p.obtenerTicketsActivos().size());
                    return "false; selección e ingreso rechazados; DISPONIBLE";
                });
            }
        }
        probar("7. Segundo ingreso activo", "ABC123 activo; E2 disponible; ingreso abc123", "Rechazo por ticket activo; E2 disponible", () -> {
            Contexto c = contexto(TipoEspacio.AUTOMOVIL); ingresar(c);
            EspacioParqueo e2 = new EspacioParqueo("E2", TipoEspacio.AUTOMOVIL); c.parqueo.registrarEspacio(e2);
            rechazo(IllegalStateException.class, () -> c.parqueo.registrarIngreso("abc123", ENTRADA.plusMinutes(1)));
            igual(1, c.parqueo.obtenerTicketsActivos().size()); igual(EstadoEspacio.DISPONIBLE, e2.getEstado());
            return "Rechazado; un ticket activo; E2 DISPONIBLE";
        });
        long[] minutos = {1, 60, 61}; long[] horas = {1, 1, 2}; double[] montos = {900, 900, 1800};
        for (int i = 0; i < minutos.length; i++) {
            final int j = i;
            probar((8 + i) + ". Duración " + minutos[i] + " minutos", "Automóvil; entrada fija + " + minutos[i] + " min",
                    horas[i] + " horas; CRC " + montos[i], () -> {
                TicketParqueo t = salir(contexto(TipoEspacio.AUTOMOVIL), minutos[j]);
                igual(horas[j], t.calcularHorasCobradas()); igual(montos[j], t.getMontoFinal());
                return t.calcularHorasCobradas() + " horas; CRC " + t.getMontoFinal();
            });
        }
        for (TipoEspacio tipo : TipoEspacio.values()) {
            double maximo = switch (tipo) { case AUTOMOVIL -> 7000; case MOTOCICLETA -> 4000; case CARGA -> 11000; };
            for (long duracion : new long[]{10, 11, 24}) {
                probar("11. Máximo diario " + tipo + " " + duracion + " h", tipo + "; " + duracion + " horas",
                        "CRC " + maximo, () -> {
                    TicketParqueo t = salir(contexto(tipo), duracion * 60);
                    igual(duracion, t.calcularHorasCobradas()); igual(maximo, t.getMontoFinal());
                    return "CRC " + t.getMontoFinal();
                });
            }
            for (long duracion : new long[]{8, 9}) {
                probar("Adicional. Tope diario " + tipo + " " + duracion + " h", tipo + "; " + duracion + " horas",
                        "CRC " + maximo + " (interpretación de máximo diario)", () -> {
                    TicketParqueo t = salir(contexto(tipo), duracion * 60);
                    igual(maximo, t.getMontoFinal()); return "CRC " + t.getMontoFinal();
                });
            }
            for (long duracion : new long[]{25, 34, 48}) {
                probar("Caracterización >24 h " + tipo + " " + duracion + " h", tipo + "; " + duracion + " horas",
                        "Comportamiento actual por bloques de 24 h; regla pendiente de Kendall", () -> {
                    Contexto c = contexto(tipo); TicketParqueo t = salir(c, duracion * 60);
                    double tarifa = switch (tipo) { case AUTOMOVIL -> 900; case MOTOCICLETA -> 500; case CARGA -> 1500; };
                    double esperado = duracion == 25 ? maximo + tarifa : 2 * maximo;
                    igual(esperado, t.getMontoFinal());
                    return "CRC " + t.getMontoFinal() + "; caracterización, no validación del enunciado";
                });
            }
        }
        probar("12. Cierre correcto", "Automóvil; salida a los 61 min", "CERRADO; salida fija; CRC 1800; espacio disponible", () -> {
            Contexto c = contexto(TipoEspacio.AUTOMOVIL); TicketParqueo t = salir(c, 61);
            igual(EstadoTicket.CERRADO, t.getEstado()); igual(ENTRADA.plusMinutes(61), t.getFechaHoraSalida());
            igual(1800.0, t.getMontoFinal()); igual(EstadoEspacio.DISPONIBLE, c.espacio.getEstado());
            igual(0, c.parqueo.obtenerTicketsActivos().size()); igual(List.of(t), c.parqueo.obtenerTicketsPendientesPago());
            return "CERRADO; " + t.getFechaHoraSalida() + "; CRC 1800; DISPONIBLE";
        });
        for (TipoPago tipo : TipoPago.values()) {
            probar("13. Pago " + tipo, "Ticket cerrado CRC 900; " + tipo, "Pago vinculado, CRC 900, tipo correcto; PAGADO", () -> {
                Contexto c = contexto(TipoEspacio.AUTOMOVIL); TicketParqueo t = salir(c, 1);
                Pago pago = c.parqueo.registrarPago(t, tipo, ENTRADA.plusMinutes(2));
                igual(t, pago.getTicket()); igual(900.0, pago.getMonto()); igual(tipo, pago.getTipo());
                igual(ENTRADA.plusMinutes(2), pago.getFechaHoraPago()); igual(EstadoTicket.PAGADO, t.getEstado());
                igual(0, c.parqueo.obtenerTicketsPendientesPago().size()); igual(900.0, c.parqueo.calcularIngresosTotales());
                return "Pago creado; CRC 900; " + tipo + "; PAGADO";
            });
        }
        probar("14. Liberación y reutilización", "Salida válida; segundo vehículo usa E1", "DISPONIBLE tras salida; nuevo ingreso en E1", () -> {
            Contexto c = contexto(TipoEspacio.AUTOMOVIL); salir(c, 1);
            igual(EstadoEspacio.DISPONIBLE, c.espacio.getEstado());
            c.parqueo.registrarVehiculo(vehiculo(TipoEspacio.AUTOMOVIL, "XYZ456"));
            TicketParqueo nuevo = c.parqueo.registrarIngreso("XYZ456", ENTRADA.plusMinutes(2));
            igual(c.espacio, nuevo.getEspacio()); igual(EstadoEspacio.OCUPADO, c.espacio.getEstado());
            return "DISPONIBLE tras salida; E1 reutilizado";
        });
        probar("15. Ingresos totales", "Pagos automóvil 900, moto 500, carga 1500; un ticket sin pagar", "Suma exacta CRC 2900", () -> {
            Parqueo p = new Parqueo("Prueba"); igual(0.0, p.calcularIngresosTotales()); double suma = 0;
            for (TipoEspacio tipo : TipoEspacio.values()) {
                Vehiculo v = vehiculo(tipo, tipo.name()); p.registrarVehiculo(v);
                p.registrarEspacio(new EspacioParqueo(tipo.name(), tipo)); p.registrarIngreso(v.getPlaca(), ENTRADA);
                TicketParqueo t = p.registrarSalida(v.getPlaca(), ENTRADA.plusMinutes(1));
                suma += p.registrarPago(t, TipoPago.EFECTIVO, ENTRADA.plusMinutes(2)).getMonto();
            }
            p.registrarIngreso("AUTOMOVIL", ENTRADA.plusHours(1));
            p.registrarSalida("AUTOMOVIL", ENTRADA.plusHours(2));
            igual(2900.0, suma); igual(suma, p.calcularIngresosTotales()); return "CRC " + p.calcularIngresosTotales();
        });
        probar("Adicional. Salida sin activo", "Vehículo registrado sin ingreso", "Rechazo controlado", () -> {
            Contexto c = contexto(TipoEspacio.AUTOMOVIL);
            rechazo(IllegalStateException.class, () -> c.parqueo.registrarSalida("ABC123", ENTRADA.plusMinutes(1)));
            igual(EstadoEspacio.DISPONIBLE, c.espacio.getEstado()); return "Rechazado; espacio disponible";
        });
        probar("Adicional. Pagar ACTIVO", "Ticket activo", "Rechazo; ACTIVO; ingresos cero", () -> {
            Contexto c = contexto(TipoEspacio.AUTOMOVIL); TicketParqueo t = ingresar(c);
            rechazo(IllegalStateException.class, () -> c.parqueo.registrarPago(t, TipoPago.EFECTIVO, ENTRADA.plusMinutes(1)));
            igual(EstadoTicket.ACTIVO, t.getEstado()); igual(0.0, c.parqueo.calcularIngresosTotales()); return "Rechazado; ACTIVO; CRC 0";
        });
        probar("Adicional. Doble pago", "Ticket ya pagado CRC 900", "Rechazo; ingresos siguen en CRC 900", () -> {
            Contexto c = contexto(TipoEspacio.AUTOMOVIL); TicketParqueo t = salir(c, 1);
            c.parqueo.registrarPago(t, TipoPago.EFECTIVO, ENTRADA.plusMinutes(2));
            rechazo(IllegalStateException.class, () -> c.parqueo.registrarPago(t, TipoPago.TARJETA, ENTRADA.plusMinutes(3)));
            igual(EstadoTicket.PAGADO, t.getEstado()); igual(900.0, c.parqueo.calcularIngresosTotales()); return "Rechazado; PAGADO; CRC 900";
        });
        probar("Adicional. Placa duplicada", "ABC123 registrada; registrar abc123", "Rechazo; un vehículo registrado", () -> {
            Contexto c = contexto(TipoEspacio.AUTOMOVIL);
            rechazo(IllegalArgumentException.class, () -> c.parqueo.registrarVehiculo(vehiculo(TipoEspacio.MOTOCICLETA, "abc123")));
            igual(1, c.parqueo.obtenerVehiculosRegistrados().size()); return "Rechazado; un vehículo";
        });
        probar("Adicional. Espacio duplicado", "E1 registrado; registrar e1", "Rechazo; un espacio disponible", () -> {
            Contexto c = contexto(TipoEspacio.AUTOMOVIL);
            rechazo(IllegalArgumentException.class, () -> c.parqueo.registrarEspacio(new EspacioParqueo("e1", TipoEspacio.CARGA)));
            igual(1, c.parqueo.consultarEspaciosDisponibles().size()); return "Rechazado; un espacio";
        });
        probar("Adicional. Vehículo inexistente", "Ingreso NOEXISTE", "Rechazo; cero tickets y espacio disponible", () -> {
            Contexto c = contexto(TipoEspacio.AUTOMOVIL);
            rechazo(IllegalArgumentException.class, () -> c.parqueo.registrarIngreso("NOEXISTE", ENTRADA));
            igual(0, c.parqueo.obtenerTicketsActivos().size()); igual(EstadoEspacio.DISPONIBLE, c.espacio.getEstado());
            return "Búsqueda mediante registrarIngreso rechazada; cero tickets";
        });
        for (long diferencia : new long[]{-1, 0}) {
            probar("Adicional. Salida inválida " + diferencia + " min", "Salida = entrada " + diferencia + " min",
                    "Rechazo; ticket intacto y espacio ocupado", () -> {
                Contexto c = contexto(TipoEspacio.AUTOMOVIL); TicketParqueo t = ingresar(c);
                rechazo(IllegalArgumentException.class, () -> c.parqueo.registrarSalida("ABC123", ENTRADA.plusMinutes(diferencia)));
                igual(EstadoTicket.ACTIVO, t.getEstado()); igual(null, t.getFechaHoraSalida()); igual(0.0, t.getMontoFinal());
                igual(EstadoEspacio.OCUPADO, c.espacio.getEstado()); return "Rechazado; ACTIVO; salida null; CRC 0; OCUPADO";
            });
        }
        probar("Adicional. Fuera de servicio ocupado", "E1 ocupado; ponerFueraDeServicio", "Rechazo; OCUPADO", () -> {
            Contexto c = contexto(TipoEspacio.AUTOMOVIL); ingresar(c);
            rechazo(IllegalStateException.class, c.espacio::ponerFueraDeServicio);
            igual(EstadoEspacio.OCUPADO, c.espacio.getEstado()); return "Rechazado; OCUPADO";
        });
        probar("Adicional. Sin espacios", "Vehículo registrado; cero espacios", "Rechazo; cero tickets", () -> {
            Parqueo p = new Parqueo("Prueba"); p.registrarVehiculo(vehiculo(TipoEspacio.AUTOMOVIL, "ABC123"));
            rechazo(IllegalStateException.class, () -> p.registrarIngreso("ABC123", ENTRADA));
            igual(0, p.obtenerTicketsActivos().size()); return "Rechazado; cero tickets";
        });
        String resumen = FILAS.size() + " pruebas: " + (FILAS.size() - fallidos) + " APROBADAS, " + fallidos + " FALLIDAS";
        String documento = "# Resultados de pruebas de Wagner\n\n" + resumen
                + "\n\nFechas controladas: 2026-10-01 08:00. Caracterizaciones >24 h: APROBADO significa que coincide con la implementación observada; el requisito debe confirmarse.\n\n"
                + "| Caso | Entrada | Resultado esperado | Resultado obtenido | Estado |\n"
                + "| --- | --- | --- | --- | --- |\n" + String.join("\n", FILAS) + "\n";
        Files.createDirectories(Path.of("docs/pruebas"));
        Files.writeString(Path.of("docs/pruebas/resultados-wagner.md"), documento, StandardCharsets.UTF_8);
        System.out.println(resumen);
        if (fallidos > 0) System.exit(1);
    }
}
