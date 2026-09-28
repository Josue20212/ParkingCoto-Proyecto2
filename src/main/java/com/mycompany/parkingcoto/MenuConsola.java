package com.mycompany.parkingcoto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class MenuConsola {

    private final Parqueo parqueo;
    private final Scanner scanner;

    private static final DateTimeFormatter FORMATO_FECHA =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public MenuConsola(Parqueo parqueo) {
        if (parqueo == null) {
            throw new IllegalArgumentException(
                    "El parqueo no puede ser nulo."
            );
        }

        this.parqueo = parqueo;
        this.scanner = new Scanner(System.in);
    }

    public void iniciar() {
        boolean continuar = true;

        while (continuar) {
            mostrarMenu();

            int opcion = leerEnteroEnRango(
                    "Seleccione una opción: ", 1, 11
            );

            System.out.println();

            switch (opcion) {
                case 1:
                    registrarVehiculo();
                    break;

                case 2:
                    registrarEspacio();
                    break;

                case 3:
                    consultarEspaciosDisponibles();
                    break;

                case 4:
                    registrarIngreso();
                    break;

                case 5:
                    consultarVehiculosDentro();
                    break;

                case 6:
                    registrarSalida();
                    break;

                case 7:
                    registrarPago();
                    break;

                case 8:
                    System.out.println(
                            "Consultar tickets activos - pendiente"
                    );
                    break;

                case 9:
                    System.out.println(
                            "Consultar ocupación por tipo - pendiente"
                    );
                    break;

                case 10:
                    System.out.println(
                            "Consultar ingresos totales - pendiente"
                    );
                    break;

                case 11:
                    continuar = false;
                    System.out.println(
                            "Saliendo de Parking Coto..."
                    );
                    break;

                default:
                    break;
            }

            System.out.println();
        }
    }

    private void mostrarMenu() {
        System.out.println("================================");
        System.out.println("         PARKING COTO");
        System.out.println("================================");
        System.out.println("1. Registrar vehículo");
        System.out.println("2. Registrar espacio");
        System.out.println("3. Consultar espacios disponibles");
        System.out.println("4. Registrar ingreso");
        System.out.println("5. Consultar vehículos dentro");
        System.out.println("6. Registrar salida");
        System.out.println("7. Registrar pago");
        System.out.println("8. Consultar tickets activos");
        System.out.println("9. Consultar ocupación por tipo");
        System.out.println("10. Consultar ingresos totales");
        System.out.println("11. Salir");
        System.out.println("================================");
    }

    // =========================================================
    // 1. REGISTRAR VEHÍCULO
    // =========================================================

    private void registrarVehiculo() {
        System.out.println("================================");
        System.out.println("       REGISTRAR VEHÍCULO");
        System.out.println("================================");
        System.out.println("Ingrese 0 en la placa para cancelar.");
        System.out.println();

        String placa = leerPlaca();

        if (placa == null) {
            System.out.println();
            System.out.println("Registro de vehículo cancelado.");
            return;
        }

        String marca = leerMarca();
        String modelo = leerModelo();
        String color = leerColor();

        System.out.println();
        System.out.println("Tipo de vehículo:");
        System.out.println("1. Automóvil");
        System.out.println("2. Motocicleta");
        System.out.println("3. Vehículo de carga");

        int opcionTipo = leerEnteroEnRango(
                "Seleccione el tipo: ", 1, 3
        );

        Vehiculo vehiculo;

        switch (opcionTipo) {
            case 1:
                vehiculo = new Automovil(
                        placa, marca, modelo, color
                );
                break;

            case 2:
                vehiculo = new Motocicleta(
                        placa, marca, modelo, color
                );
                break;

            case 3:
                vehiculo = new VehiculoCarga(
                        placa, marca, modelo, color
                );
                break;

            default:
                throw new IllegalStateException(
                        "Tipo de vehículo no válido."
                );
        }

        try {
            parqueo.registrarVehiculo(vehiculo);

            System.out.println();
            System.out.println(
                    "Vehículo registrado correctamente."
            );
            System.out.println("--------------------------------");
            System.out.println("Placa: " + vehiculo.getPlaca());
            System.out.println("Marca: " + vehiculo.getMarca());
            System.out.println("Modelo: " + vehiculo.getModelo());
            System.out.println("Color: " + vehiculo.getColor());
            System.out.println("Tipo: " + vehiculo.getTipo());

        } catch (IllegalArgumentException
                | IllegalStateException e) {

            System.out.println();
            System.out.println("Error: " + e.getMessage());
        }
    }

    private String leerPlaca() {
        while (true) {
            System.out.print("Placa: ");

            String placa = scanner.nextLine()
                    .trim()
                    .toUpperCase();

            if (placa.equals("0")) {
                return null;
            }

            if (placa.isEmpty()) {
                System.out.println(
                        "Error: la placa no puede estar vacía."
                );
                continue;
            }

            if (placa.length() < 3 || placa.length() > 15) {
                System.out.println(
                        "Error: la placa debe tener entre "
                        + "3 y 15 caracteres."
                );
                continue;
            }

            if (!placa.matches("[A-Z0-9-]+")) {
                System.out.println(
                        "Error: la placa solo puede contener "
                        + "letras, números y guiones."
                );
                continue;
            }

            if (!contieneLetra(placa)) {
                System.out.println(
                        "Error: la placa debe contener "
                        + "al menos una letra."
                );
                continue;
            }

            return placa;
        }
    }

    private String leerMarca() {
        while (true) {
            System.out.print("Marca: ");

            String marca = normalizarEspacios(
                    scanner.nextLine()
            );

            if (marca.isEmpty()) {
                System.out.println(
                        "Error: la marca no puede estar vacía."
                );
                continue;
            }

            if (marca.length() > 40) {
                System.out.println(
                        "Error: la marca no puede superar "
                        + "los 40 caracteres."
                );
                continue;
            }

            if (!contieneLetra(marca)) {
                System.out.println(
                        "Error: la marca debe contener "
                        + "al menos una letra."
                );
                continue;
            }

            if (!marca.matches("[\\p{L}0-9 .&'-]+")) {
                System.out.println(
                        "Error: la marca contiene "
                        + "caracteres no válidos."
                );
                continue;
            }

            return capitalizarPalabras(marca);
        }
    }

    private String leerModelo() {
        while (true) {
            System.out.print("Modelo: ");

            String modelo = normalizarEspacios(
                    scanner.nextLine()
            );

            if (modelo.isEmpty()) {
                System.out.println(
                        "Error: el modelo no puede estar vacío."
                );
                continue;
            }

            if (modelo.length() > 40) {
                System.out.println(
                        "Error: el modelo no puede superar "
                        + "los 40 caracteres."
                );
                continue;
            }

            if (!modelo.matches("[\\p{L}0-9 .&'/_-]+")) {
                System.out.println(
                        "Error: el modelo contiene "
                        + "caracteres no válidos."
                );
                continue;
            }

            return modelo;
        }
    }

    private String leerColor() {
        while (true) {
            System.out.print("Color: ");

            String color = normalizarEspacios(
                    scanner.nextLine()
            );

            if (color.isEmpty()) {
                System.out.println(
                        "Error: el color no puede estar vacío."
                );
                continue;
            }

            if (color.length() > 30) {
                System.out.println(
                        "Error: el color no puede superar "
                        + "los 30 caracteres."
                );
                continue;
            }

            if (!contieneLetra(color)) {
                System.out.println(
                        "Error: el color debe contener "
                        + "al menos una letra."
                );
                continue;
            }

            if (!color.matches("[\\p{L} -]+")) {
                System.out.println(
                        "Error: el color contiene "
                        + "caracteres no válidos."
                );
                continue;
            }

            return capitalizarPalabras(color);
        }
    }

    // =========================================================
    // 2. REGISTRAR ESPACIO
    // =========================================================

    private void registrarEspacio() {
        System.out.println("================================");
        System.out.println("        REGISTRAR ESPACIO");
        System.out.println("================================");
        System.out.println(
                "Ingrese 0 en el identificador para cancelar."
        );
        System.out.println();

        String id = leerIdentificadorEspacio();

        if (id == null) {
            System.out.println();
            System.out.println("Registro de espacio cancelado.");
            return;
        }

        System.out.println();
        System.out.println("Tipo de espacio:");
        System.out.println("1. Automóvil");
        System.out.println("2. Motocicleta");
        System.out.println("3. Carga");

        int opcionTipo = leerEnteroEnRango(
                "Seleccione el tipo: ", 1, 3
        );

        TipoEspacio tipo;

        switch (opcionTipo) {
            case 1:
                tipo = TipoEspacio.AUTOMOVIL;
                break;

            case 2:
                tipo = TipoEspacio.MOTOCICLETA;
                break;

            case 3:
                tipo = TipoEspacio.CARGA;
                break;

            default:
                throw new IllegalStateException(
                        "Tipo de espacio no válido."
                );
        }

        try {
            EspacioParqueo espacio =
                    new EspacioParqueo(id, tipo);

            parqueo.registrarEspacio(espacio);

            System.out.println();
            System.out.println(
                    "Espacio registrado correctamente."
            );
            System.out.println("--------------------------------");
            System.out.println(
                    "Identificador: " + espacio.getId()
            );
            System.out.println(
                    "Tipo: " + espacio.getTipo()
            );
            System.out.println(
                    "Estado: " + espacio.getEstado()
            );

        } catch (IllegalArgumentException
                | IllegalStateException e) {

            System.out.println();
            System.out.println("Error: " + e.getMessage());
        }
    }

    private String leerIdentificadorEspacio() {
        while (true) {
            System.out.print("Identificador: ");

            String id = scanner.nextLine()
                    .trim()
                    .toUpperCase();

            if (id.equals("0")) {
                return null;
            }

            if (id.isEmpty()) {
                System.out.println(
                        "Error: el identificador no puede "
                        + "estar vacío."
                );
                continue;
            }

            if (id.length() < 2 || id.length() > 15) {
                System.out.println(
                        "Error: el identificador debe tener "
                        + "entre 2 y 15 caracteres."
                );
                continue;
            }

            if (!id.matches("[A-Z0-9-]+")) {
                System.out.println(
                        "Error: el identificador solo puede "
                        + "contener letras, números y guiones."
                );
                continue;
            }

            if (!contieneLetra(id)) {
                System.out.println(
                        "Error: el identificador debe contener "
                        + "al menos una letra."
                );
                continue;
            }

            return id;
        }
    }

    // =========================================================
    // 3. CONSULTAR ESPACIOS DISPONIBLES
    // =========================================================

    private void consultarEspaciosDisponibles() {
        System.out.println("================================");
        System.out.println("      ESPACIOS DISPONIBLES");
        System.out.println("================================");

        List<EspacioParqueo> espacios =
                parqueo.consultarEspaciosDisponibles();

        if (espacios.isEmpty()) {
            System.out.println();
            System.out.println(
                    "No hay espacios disponibles actualmente."
            );
            return;
        }

        System.out.println();

        for (EspacioParqueo espacio : espacios) {
            System.out.println(
                    "Identificador: " + espacio.getId()
            );
            System.out.println(
                    "Tipo: " + espacio.getTipo()
            );
            System.out.println(
                    "Estado: " + espacio.getEstado()
            );
            System.out.println("--------------------------------");
        }

        System.out.println(
                "Total de espacios disponibles: "
                + espacios.size()
        );
    }

    // =========================================================
    // 4. REGISTRAR INGRESO
    // =========================================================

    private void registrarIngreso() {
        System.out.println("================================");
        System.out.println("        REGISTRAR INGRESO");
        System.out.println("================================");

        List<Vehiculo> vehiculos =
                parqueo.obtenerVehiculosRegistrados();

        if (vehiculos.isEmpty()) {
            System.out.println();
            System.out.println(
                    "No hay vehículos registrados."
            );
            System.out.println(
                    "Debe registrar un vehículo antes "
                    + "de realizar un ingreso."
            );
            return;
        }

        System.out.println();
        System.out.println("VEHÍCULOS REGISTRADOS");
        System.out.println("--------------------------------");

        for (Vehiculo vehiculo : vehiculos) {
            System.out.println(
                    "Placa: " + vehiculo.getPlaca()
            );
            System.out.println(
                    "Marca: " + vehiculo.getMarca()
            );
            System.out.println(
                    "Modelo: " + vehiculo.getModelo()
            );
            System.out.println(
                    "Tipo: " + vehiculo.getTipo()
            );
            System.out.println("--------------------------------");
        }

        System.out.println(
                "Total de vehículos registrados: "
                + vehiculos.size()
        );

        List<EspacioParqueo> espacios =
                parqueo.consultarEspaciosDisponibles();

        System.out.println();
        System.out.println("ESPACIOS DISPONIBLES");
        System.out.println("--------------------------------");

        if (espacios.isEmpty()) {
            System.out.println(
                    "No hay espacios disponibles actualmente."
            );
            System.out.println();
            System.out.println(
                    "No es posible registrar un ingreso "
                    + "en este momento."
            );
            return;
        }

        for (EspacioParqueo espacio : espacios) {
            System.out.println(
                    "Identificador: " + espacio.getId()
            );
            System.out.println(
                    "Tipo: " + espacio.getTipo()
            );
            System.out.println(
                    "Estado: " + espacio.getEstado()
            );
            System.out.println("--------------------------------");
        }

        System.out.println(
                "Total de espacios disponibles: "
                + espacios.size()
        );

        System.out.println();
        System.out.println(
                "El sistema asignará automáticamente "
                + "un espacio compatible."
        );
        System.out.println("Ingrese 0 para cancelar.");
        System.out.println();

        String placa = leerPlacaIngreso();

        if (placa == null) {
            System.out.println();
            System.out.println(
                    "Registro de ingreso cancelado."
            );
            return;
        }

        try {
            LocalDateTime fechaHoraEntrada =
                    LocalDateTime.now();

            TicketParqueo ticket =
                    parqueo.registrarIngreso(
                            placa,
                            fechaHoraEntrada
                    );

            System.out.println();
            System.out.println(
                    "Ingreso registrado correctamente."
            );
            System.out.println("================================");

            System.out.println(
                    "Ticket: " + ticket.getNumero()
            );
            System.out.println(
                    "Estado: " + ticket.getEstado()
            );

            System.out.println();
            System.out.println("VEHÍCULO");
            System.out.println("--------------------------------");
            System.out.println(
                    "Placa: "
                    + ticket.getVehiculo().getPlaca()
            );
            System.out.println(
                    "Marca: "
                    + ticket.getVehiculo().getMarca()
            );
            System.out.println(
                    "Modelo: "
                    + ticket.getVehiculo().getModelo()
            );
            System.out.println(
                    "Tipo: "
                    + ticket.getVehiculo().getTipo()
            );

            System.out.println();
            System.out.println("ESPACIO ASIGNADO");
            System.out.println("--------------------------------");
            System.out.println(
                    "Identificador: "
                    + ticket.getEspacio().getId()
            );
            System.out.println(
                    "Tipo: "
                    + ticket.getEspacio().getTipo()
            );
            System.out.println(
                    "Estado: "
                    + ticket.getEspacio().getEstado()
            );

            System.out.println();
            System.out.println(
                    "Hora de entrada: "
                    + ticket.getFechaHoraEntrada()
                            .format(FORMATO_FECHA)
            );

        } catch (IllegalArgumentException
                | IllegalStateException e) {

            System.out.println();
            System.out.println("Error: " + e.getMessage());
        }
    }

    private String leerPlacaIngreso() {
        while (true) {
            System.out.print("Placa del vehículo: ");

            String placa = scanner.nextLine()
                    .trim()
                    .toUpperCase();

            if (placa.equals("0")) {
                return null;
            }

            if (placa.isEmpty()) {
                System.out.println(
                        "Error: la placa no puede estar vacía."
                );
                continue;
            }

            if (placa.length() < 3 || placa.length() > 15) {
                System.out.println(
                        "Error: la placa debe tener entre "
                        + "3 y 15 caracteres."
                );
                continue;
            }

            if (!placa.matches("[A-Z0-9-]+")) {
                System.out.println(
                        "Error: la placa solo puede contener "
                        + "letras, números y guiones."
                );
                continue;
            }

            if (!contieneLetra(placa)) {
                System.out.println(
                        "Error: la placa debe contener "
                        + "al menos una letra."
                );
                continue;
            }

            return placa;
        }
    }

    // =========================================================
    // 5. CONSULTAR VEHÍCULOS DENTRO
    // =========================================================

    private void consultarVehiculosDentro() {
        System.out.println("================================");
        System.out.println("       VEHÍCULOS DENTRO");
        System.out.println("================================");

        List<Vehiculo> vehiculos =
                parqueo.obtenerVehiculosDentro();

        if (vehiculos.isEmpty()) {
            System.out.println();
            System.out.println(
                    "No hay vehículos dentro del parqueo actualmente."
            );
            return;
        }

        System.out.println();

        for (Vehiculo vehiculo : vehiculos) {
            System.out.println(
                    "Placa: " + vehiculo.getPlaca()
            );
            System.out.println(
                    "Marca: " + vehiculo.getMarca()
            );
            System.out.println(
                    "Modelo: " + vehiculo.getModelo()
            );
            System.out.println(
                    "Color: " + vehiculo.getColor()
            );
            System.out.println(
                    "Tipo: " + vehiculo.getTipo()
            );
            System.out.println("--------------------------------");
        }

        System.out.println(
                "Total de vehículos dentro: "
                + vehiculos.size()
        );
    }

    // =========================================================
    // 6. REGISTRAR SALIDA
    // =========================================================

    private void registrarSalida() {
        System.out.println("================================");
        System.out.println("         REGISTRAR SALIDA");
        System.out.println("================================");

        List<Vehiculo> vehiculosDentro =
                parqueo.obtenerVehiculosDentro();

        if (vehiculosDentro.isEmpty()) {
            System.out.println();
            System.out.println(
                    "No hay vehículos dentro del parqueo."
            );
            System.out.println(
                    "No es posible registrar una salida."
            );
            return;
        }

        System.out.println();
        System.out.println("VEHÍCULOS DENTRO");
        System.out.println("--------------------------------");

        for (Vehiculo vehiculo : vehiculosDentro) {
            System.out.println(
                    "Placa: " + vehiculo.getPlaca()
            );
            System.out.println(
                    "Marca: " + vehiculo.getMarca()
            );
            System.out.println(
                    "Modelo: " + vehiculo.getModelo()
            );
            System.out.println(
                    "Tipo: " + vehiculo.getTipo()
            );
            System.out.println("--------------------------------");
        }

        System.out.println(
                "Total de vehículos dentro: "
                + vehiculosDentro.size()
        );

        System.out.println();
        System.out.println("Ingrese 0 para cancelar.");
        System.out.println();

        String placa = leerPlacaSalida();

        if (placa == null) {
            System.out.println();
            System.out.println(
                    "Registro de salida cancelado."
            );
            return;
        }

        try {
            LocalDateTime fechaHoraSalida =
                    LocalDateTime.now();

            TicketParqueo ticket =
                    parqueo.registrarSalida(
                            placa,
                            fechaHoraSalida
                    );

            System.out.println();
            System.out.println(
                    "Salida registrada correctamente."
            );
            System.out.println("================================");

            System.out.println(
                    "Ticket: " + ticket.getNumero()
            );

            System.out.println();
            System.out.println("VEHÍCULO");
            System.out.println("--------------------------------");
            System.out.println(
                    "Placa: "
                    + ticket.getVehiculo().getPlaca()
            );
            System.out.println(
                    "Marca: "
                    + ticket.getVehiculo().getMarca()
            );
            System.out.println(
                    "Modelo: "
                    + ticket.getVehiculo().getModelo()
            );
            System.out.println(
                    "Tipo: "
                    + ticket.getVehiculo().getTipo()
            );

            System.out.println();
            System.out.println("PERMANENCIA");
            System.out.println("--------------------------------");
            System.out.println(
                    "Hora de entrada: "
                    + ticket.getFechaHoraEntrada()
                            .format(FORMATO_FECHA)
            );
            System.out.println(
                    "Hora de salida:  "
                    + ticket.getFechaHoraSalida()
                            .format(FORMATO_FECHA)
            );
            System.out.println(
                    "Horas cobradas: "
                    + ticket.calcularHorasCobradas()
            );

            System.out.println();
            System.out.println("COBRO");
            System.out.println("--------------------------------");
            System.out.printf(
                    "Monto a pagar: CRC %.2f%n",
                    ticket.getMontoFinal()
            );

            System.out.println();
            System.out.println("ESTADO");
            System.out.println("--------------------------------");
            System.out.println(
                    "Estado del ticket: "
                    + ticket.getEstado()
            );
            System.out.println(
                    "Espacio liberado: "
                    + ticket.getEspacio().getId()
            );
            System.out.println(
                    "Estado del espacio: "
                    + ticket.getEspacio().getEstado()
            );

            System.out.println();
            System.out.println(
                    "El ticket quedó pendiente de pago."
            );

        } catch (IllegalArgumentException
                | IllegalStateException e) {

            System.out.println();
            System.out.println("Error: " + e.getMessage());
        }
    }

    private String leerPlacaSalida() {
        while (true) {
            System.out.print(
                    "Placa del vehículo que sale: "
            );

            String placa = scanner.nextLine()
                    .trim()
                    .toUpperCase();

            if (placa.equals("0")) {
                return null;
            }

            if (placa.isEmpty()) {
                System.out.println(
                        "Error: la placa no puede estar vacía."
                );
                continue;
            }

            if (placa.length() < 3 || placa.length() > 15) {
                System.out.println(
                        "Error: la placa debe tener entre "
                        + "3 y 15 caracteres."
                );
                continue;
            }

            if (!placa.matches("[A-Z0-9-]+")) {
                System.out.println(
                        "Error: la placa solo puede contener "
                        + "letras, números y guiones."
                );
                continue;
            }

            if (!contieneLetra(placa)) {
                System.out.println(
                        "Error: la placa debe contener "
                        + "al menos una letra."
                );
                continue;
            }

            return placa;
        }
    }

    // =========================================================
    // 7. REGISTRAR PAGO
    // =========================================================

    private void registrarPago() {
        System.out.println("================================");
        System.out.println("          REGISTRAR PAGO");
        System.out.println("================================");

        List<TicketParqueo> pendientes =
                parqueo.obtenerTicketsPendientesPago();

        if (pendientes.isEmpty()) {
            System.out.println();
            System.out.println(
                    "No hay tickets pendientes de pago."
            );
            return;
        }

        System.out.println();
        System.out.println("TICKETS PENDIENTES DE PAGO");
        System.out.println("--------------------------------");

        for (TicketParqueo ticket : pendientes) {
            mostrarTicketPendiente(ticket);
        }

        System.out.println(
                "Total de tickets pendientes: "
                + pendientes.size()
        );

        System.out.println();
        System.out.println(
                "Ingrese 0 para cancelar."
        );

        TicketParqueo ticket =
                seleccionarTicketPendiente(pendientes);

        if (ticket == null) {
            System.out.println();
            System.out.println(
                    "Registro de pago cancelado."
            );
            return;
        }

        System.out.println();
        System.out.println("MÉTODO DE PAGO");
        System.out.println("--------------------------------");
        System.out.println("1. Efectivo");
        System.out.println("2. Tarjeta");
        System.out.println("3. SINPE Móvil");
        System.out.println("0. Cancelar");

        int opcionPago =
                leerEnteroEnRangoIncluyendoCero(
                        "Seleccione el método de pago: ",
                        0,
                        3
                );

        if (opcionPago == 0) {
            System.out.println();
            System.out.println(
                    "Registro de pago cancelado."
            );
            return;
        }

        TipoPago tipoPago;

        switch (opcionPago) {
            case 1:
                tipoPago = TipoPago.EFECTIVO;
                break;

            case 2:
                tipoPago = TipoPago.TARJETA;
                break;

            case 3:
                tipoPago = TipoPago.SINPE_MOVIL;
                break;

            default:
                throw new IllegalStateException(
                        "Método de pago no válido."
                );
        }

        try {
            LocalDateTime fechaHoraPago =
                    LocalDateTime.now();

            Pago pago =
                    parqueo.registrarPago(
                            ticket,
                            tipoPago,
                            fechaHoraPago
                    );

            System.out.println();
            System.out.println(
                    "Pago registrado correctamente."
            );
            System.out.println("================================");

            System.out.println(
                    "Pago ID: " + pago.getId()
            );
            System.out.println(
                    "Ticket: "
                    + pago.getTicket().getNumero()
            );
            System.out.println(
                    "Placa: "
                    + pago.getTicket()
                            .getVehiculo()
                            .getPlaca()
            );
            System.out.println(
                    "Espacio: "
                    + pago.getTicket()
                            .getEspacio()
                            .getId()
            );
            System.out.println(
                    "Método: "
                    + formatearTipoPago(pago.getTipo())
            );

            System.out.printf(
                    "Monto pagado: CRC %.2f%n",
                    pago.getMonto()
            );

            System.out.println(
                    "Fecha de pago: "
                    + pago.getFechaHoraPago()
                            .format(FORMATO_FECHA)
            );

            System.out.println(
                    "Estado del ticket: "
                    + pago.getTicket().getEstado()
            );

            System.out.println();
            System.out.println(
                    "El ticket ha sido pagado completamente."
            );

        } catch (IllegalArgumentException
                | IllegalStateException e) {

            System.out.println();
            System.out.println(
                    "Error: " + e.getMessage()
            );
        }
    }

    private void mostrarTicketPendiente(
            TicketParqueo ticket) {

        System.out.println(
                "Ticket: " + ticket.getNumero()
        );
        System.out.println(
                "Placa: "
                + ticket.getVehiculo().getPlaca()
        );
        System.out.println(
                "Espacio: "
                + ticket.getEspacio().getId()
        );
        System.out.println(
                "Entrada: "
                + ticket.getFechaHoraEntrada()
                        .format(FORMATO_FECHA)
        );
        System.out.println(
                "Salida: "
                + ticket.getFechaHoraSalida()
                        .format(FORMATO_FECHA)
        );

        System.out.printf(
                "Monto: CRC %.2f%n",
                ticket.getMontoFinal()
        );

        System.out.println(
                "Estado: " + ticket.getEstado()
        );
        System.out.println("--------------------------------");
    }

    private TicketParqueo seleccionarTicketPendiente(
            List<TicketParqueo> pendientes) {

        while (true) {
            System.out.print(
                    "Número de ticket: "
            );

            String entrada =
                    scanner.nextLine().trim();

            if (entrada.isEmpty()) {
                System.out.println(
                        "Error: debe ingresar "
                        + "un número de ticket."
                );
                continue;
            }

            int numero;

            try {
                numero =
                        Integer.parseInt(entrada);

            } catch (NumberFormatException e) {
                System.out.println(
                        "Error: debe ingresar "
                        + "un número válido."
                );
                continue;
            }

            if (numero == 0) {
                return null;
            }

            if (numero < 0) {
                System.out.println(
                        "Error: el número de ticket "
                        + "no puede ser negativo."
                );
                continue;
            }

            for (TicketParqueo ticket : pendientes) {
                if (ticket.getNumero() == numero) {
                    return ticket;
                }
            }

            System.out.println(
                    "Error: no existe un ticket pendiente "
                    + "de pago con el número "
                    + numero
                    + "."
            );
        }
    }

    private String formatearTipoPago(
            TipoPago tipoPago) {

        switch (tipoPago) {
            case EFECTIVO:
                return "EFECTIVO";

            case TARJETA:
                return "TARJETA";

            case SINPE_MOVIL:
                return "SINPE MÓVIL";

            default:
                return tipoPago.toString();
        }
    }

    // =========================================================
    // MÉTODOS AUXILIARES
    // =========================================================

    private String normalizarEspacios(String texto) {
        return texto.trim().replaceAll("\\s+", " ");
    }

    private String capitalizarPalabras(String texto) {
        String textoNormalizado =
                normalizarEspacios(texto).toLowerCase();

        StringBuilder resultado = new StringBuilder();
        boolean convertirMayuscula = true;

        for (int i = 0;
                i < textoNormalizado.length();
                i++) {

            char caracter =
                    textoNormalizado.charAt(i);

            if (Character.isLetter(caracter)
                    && convertirMayuscula) {

                resultado.append(
                        Character.toUpperCase(caracter)
                );

                convertirMayuscula = false;

            } else {
                resultado.append(caracter);
            }

            if (caracter == ' '
                    || caracter == '-') {

                convertirMayuscula = true;
            }
        }

        return resultado.toString();
    }

    private boolean contieneLetra(String texto) {
        for (int i = 0;
                i < texto.length();
                i++) {

            if (Character.isLetter(
                    texto.charAt(i)
            )) {
                return true;
            }
        }

        return false;
    }

    private int leerEnteroEnRango(
            String mensaje,
            int minimo,
            int maximo) {

        while (true) {
            System.out.print(mensaje);

            String entrada =
                    scanner.nextLine().trim();

            if (entrada.isEmpty()) {
                System.out.println(
                        "Error: debe ingresar una opción."
                );
                continue;
            }

            try {
                int numero =
                        Integer.parseInt(entrada);

                if (numero < minimo
                        || numero > maximo) {

                    System.out.println(
                            "Error: ingrese un número entre "
                            + minimo
                            + " y "
                            + maximo
                            + "."
                    );
                    continue;
                }

                return numero;

            } catch (NumberFormatException e) {
                System.out.println(
                        "Error: debe ingresar "
                        + "un número válido."
                );
            }
        }
    }

    private int leerEnteroEnRangoIncluyendoCero(
            String mensaje,
            int minimo,
            int maximo) {

        while (true) {
            System.out.print(mensaje);

            String entrada =
                    scanner.nextLine().trim();

            if (entrada.isEmpty()) {
                System.out.println(
                        "Error: debe ingresar una opción."
                );
                continue;
            }

            try {
                int numero =
                        Integer.parseInt(entrada);

                if (numero < minimo
                        || numero > maximo) {

                    System.out.println(
                            "Error: ingrese un número entre "
                            + minimo
                            + " y "
                            + maximo
                            + "."
                    );
                    continue;
                }

                return numero;

            } catch (NumberFormatException e) {
                System.out.println(
                        "Error: debe ingresar "
                        + "un número válido."
                );
            }
        }
    }
}