package com.mycompany.parkingcoto;

import java.util.Scanner;

public class MenuConsola {

    private final Parqueo parqueo;
    private final Scanner scanner;

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
                    System.out.println(
                            "Registrar espacio - pendiente"
                    );
                    break;

                case 3:
                    System.out.println(
                            "Consultar espacios disponibles - pendiente"
                    );
                    break;

                case 4:
                    System.out.println(
                            "Registrar ingreso - pendiente"
                    );
                    break;

                case 5:
                    System.out.println(
                            "Consultar vehículos dentro - pendiente"
                    );
                    break;

                case 6:
                    System.out.println(
                            "Registrar salida - pendiente"
                    );
                    break;

                case 7:
                    System.out.println(
                            "Registrar pago - pendiente"
                    );
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

    private void registrarVehiculo() {
        System.out.println("================================");
        System.out.println("       REGISTRAR VEHÍCULO");
        System.out.println("================================");
        System.out.println(
                "Ingrese 0 en la placa para cancelar."
        );
        System.out.println();

        String placa = leerPlaca();

        if (placa == null) {
            System.out.println();
            System.out.println(
                    "Registro de vehículo cancelado."
            );
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
                        placa,
                        marca,
                        modelo,
                        color
                );
                break;

            case 2:
                vehiculo = new Motocicleta(
                        placa,
                        marca,
                        modelo,
                        color
                );
                break;

            case 3:
                vehiculo = new VehiculoCarga(
                        placa,
                        marca,
                        modelo,
                        color
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
            System.out.println(
                    "--------------------------------"
            );
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

        } catch (IllegalArgumentException
                | IllegalStateException e) {

            System.out.println();
            System.out.println(
                    "Error: " + e.getMessage()
            );
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

            if (placa.length() < 3
                    || placa.length() > 15) {

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

            if (!marca.matches(
                    "[\\p{L}0-9 .&'-]+"
            )) {
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

            if (!modelo.matches(
                    "[\\p{L}0-9 .&'/_-]+"
            )) {
                System.out.println(
                        "Error: el modelo contiene "
                        + "caracteres no válidos."
                );
                continue;
            }

            /*
             * El modelo NO se capitaliza automáticamente.
             *
             * Esto permite conservar correctamente nombres como:
             * CX-5, MT-07, YZS, RAV4, i10, 911, etc.
             */
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
            int maximo
    ) {

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