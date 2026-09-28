package com.mycompany.parkingcoto;

import java.time.LocalDateTime;

public class ParkingCoto {

    public static void main(String[] args) {

        Parqueo parqueo = new Parqueo("Parking Coto");

        Vehiculo auto = new Automovil(
                "ABC123",
                "Toyota",
                "Corolla",
                "Blanco"
        );

        Vehiculo moto = new Motocicleta(
                "MOTO01",
                "Yamaha",
                "MT-07",
                "Azul"
        );

        parqueo.registrarVehiculo(auto);
        parqueo.registrarVehiculo(moto);

        parqueo.registrarEspacio(
                new EspacioParqueo("A-01", TipoEspacio.AUTOMOVIL)
        );

        parqueo.registrarEspacio(
                new EspacioParqueo("M-01", TipoEspacio.MOTOCICLETA)
        );

        LocalDateTime entrada =
                LocalDateTime.of(2026, 9, 27, 10, 0);

        TicketParqueo ticket =
                parqueo.registrarIngreso("ABC123", entrada);

        System.out.println("=== INGRESO ===");
        System.out.println("Ticket: " + ticket.getNumero());
        System.out.println("Vehículo: " + ticket.getVehiculo().getPlaca());
        System.out.println("Espacio: " + ticket.getEspacio().getId());
        System.out.println("Estado: " + ticket.getEstado());

        System.out.println("\nVehículos dentro: "
                + parqueo.obtenerVehiculosDentro().size());

        TicketParqueo salida = parqueo.registrarSalida(
                "ABC123",
                entrada.plusMinutes(61)
        );

        System.out.println("\n=== SALIDA ===");
        System.out.println("Horas cobradas: "
                + salida.calcularHorasCobradas());
        System.out.println("Monto: ₡"
                + salida.getMontoFinal());
        System.out.println("Estado ticket: "
                + salida.getEstado());
        System.out.println("Estado espacio: "
                + salida.getEspacio().getEstado());

        Pago pago = parqueo.registrarPago(
                salida,
                TipoPago.TARJETA,
                entrada.plusMinutes(65)
        );

        System.out.println("\n=== PAGO ===");
        System.out.println("Pago ID: " + pago.getId());
        System.out.println("Tipo: " + pago.getTipo());
        System.out.println("Monto: ₡" + pago.getMonto());
        System.out.println("Estado ticket: "
                + salida.getEstado());

        System.out.println("\n=== RESUMEN ===");
        System.out.println("Vehículos dentro: "
                + parqueo.obtenerVehiculosDentro().size());
        System.out.println("Tickets activos: "
                + parqueo.obtenerTicketsActivos().size());
        System.out.println("Ingresos totales: ₡"
                + parqueo.calcularIngresosTotales());
    }
}