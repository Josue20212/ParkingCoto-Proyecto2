package com.mycompany.parkingcoto;

import java.util.Scanner;

public class MenuConsola {

    private final Parqueo parqueo;
    private final Scanner scanner;

    public MenuConsola(Parqueo parqueo) {
        if (parqueo == null) {
            throw new IllegalArgumentException("El parqueo no puede ser nulo.");
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
                    System.out.println("Registrar vehículo - pendiente");
                    break;
                case 2:
                    System.out.println("Registrar espacio - pendiente");
                    break;
                case 3:
                    System.out.println("Consultar espacios disponibles - pendiente");
                    break;
                case 4:
                    System.out.println("Registrar ingreso - pendiente");
                    break;
                case 5:
                    System.out.println("Consultar vehículos dentro - pendiente");
                    break;
                case 6:
                    System.out.println("Registrar salida - pendiente");
                    break;
                case 7:
                    System.out.println("Registrar pago - pendiente");
                    break;
                case 8:
                    System.out.println("Consultar tickets activos - pendiente");
                    break;
                case 9:
                    System.out.println("Consultar ocupación por tipo - pendiente");
                    break;
                case 10:
                    System.out.println("Consultar ingresos totales - pendiente");
                    break;
                case 11:
                    continuar = false;
                    System.out.println("Saliendo de Parking Coto...");
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

    private int leerEnteroEnRango(String mensaje, int minimo, int maximo) {
        while (true) {
            System.out.print(mensaje);
            String entrada = scanner.nextLine().trim();

            if (entrada.isEmpty()) {
                System.out.println("Error: debe ingresar una opción.");
                continue;
            }

            try {
                int numero = Integer.parseInt(entrada);

                if (numero < minimo || numero > maximo) {
                    System.out.println(
                            "Error: ingrese un número entre "
                            + minimo + " y " + maximo + "."
                    );
                    continue;
                }

                return numero;

            } catch (NumberFormatException e) {
                System.out.println("Error: debe ingresar un número válido.");
            }
        }
    }
}