package com.mycompany.parkingcoto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Parqueo {

    private String nombre;
    private List<Vehiculo> vehiculos;
    private List<EspacioParqueo> espacios;
    private List<TicketParqueo> tickets;
    private List<Pago> pagos;
    private int siguienteNumeroTicket;
    private int siguienteIdPago;

    public Parqueo(String nombre) {
        this.nombre = nombre;
        this.vehiculos = new ArrayList<>();
        this.espacios = new ArrayList<>();
        this.tickets = new ArrayList<>();
        this.pagos = new ArrayList<>();
        this.siguienteNumeroTicket = 1;
        this.siguienteIdPago = 1;
    }

    public void registrarVehiculo(Vehiculo vehiculo) {
        if (vehiculo == null) {
            throw new IllegalArgumentException(
                    "El vehículo no puede ser nulo."
            );
        }

        for (Vehiculo registrado : vehiculos) {
            if (registrado.getPlaca()
                    .equalsIgnoreCase(vehiculo.getPlaca())) {

                throw new IllegalArgumentException(
                        "Ya existe un vehículo registrado con esa placa."
                );
            }
        }

        vehiculos.add(vehiculo);
    }

    public void registrarEspacio(EspacioParqueo espacio) {
        if (espacio == null) {
            throw new IllegalArgumentException(
                    "El espacio no puede ser nulo."
            );
        }

        for (EspacioParqueo registrado : espacios) {
            if (registrado.getId()
                    .equalsIgnoreCase(espacio.getId())) {

                throw new IllegalArgumentException(
                        "Ya existe un espacio registrado "
                        + "con ese identificador."
                );
            }
        }

        espacios.add(espacio);
    }

    public List<EspacioParqueo> consultarEspaciosDisponibles() {
        List<EspacioParqueo> disponibles = new ArrayList<>();

        for (EspacioParqueo espacio : espacios) {
            if (espacio.estaDisponible()) {
                disponibles.add(espacio);
            }
        }

        return disponibles;
    }

    public EspacioParqueo buscarEspacioCompatible(
            Vehiculo vehiculo) {

        for (EspacioParqueo espacio : espacios) {
            if (espacio.estaDisponible()
                    && espacio.esCompatible(vehiculo)) {

                return espacio;
            }
        }

        throw new IllegalStateException(
                "No existe un espacio disponible "
                + "compatible con el vehículo."
        );
    }

    public TicketParqueo registrarIngreso(
            String placa,
            LocalDateTime fechaHoraEntrada) {

        Vehiculo vehiculo =
                buscarVehiculoPorPlaca(placa);

        for (TicketParqueo ticket : tickets) {
            if (ticket.estaActivo()
                    && ticket.getVehiculo()
                            .getPlaca()
                            .equalsIgnoreCase(placa)) {

                throw new IllegalStateException(
                        "El vehículo ya tiene un ticket activo."
                );
            }
        }

        EspacioParqueo espacio =
                buscarEspacioCompatible(vehiculo);

        espacio.ocupar();

        TicketParqueo ticket =
                new TicketParqueo(
                        siguienteNumeroTicket,
                        vehiculo,
                        espacio,
                        fechaHoraEntrada
                );

        siguienteNumeroTicket++;
        tickets.add(ticket);

        return ticket;
    }

    public TicketParqueo registrarSalida(
            String placa,
            LocalDateTime fechaHoraSalida) {

        TicketParqueo ticket =
                buscarTicketActivo(placa);

        ticket.cerrar(fechaHoraSalida);

        ticket.getEspacio().liberar();

        return ticket;
    }

    public Pago registrarPago(
            TicketParqueo ticket,
            TipoPago tipo,
            LocalDateTime fechaHoraPago) {

        if (ticket == null) {
            throw new IllegalArgumentException(
                    "El ticket no puede ser nulo."
            );
        }

        if (!tickets.contains(ticket)) {
            throw new IllegalArgumentException(
                    "El ticket no pertenece a este parqueo."
            );
        }

        if (ticket.estaActivo()) {
            throw new IllegalStateException(
                    "No se puede pagar un ticket activo."
            );
        }

        if (ticket.getEstado()
                == EstadoTicket.PAGADO) {

            throw new IllegalStateException(
                    "El ticket ya fue pagado."
            );
        }

        Pago pago =
                new Pago(
                        siguienteIdPago,
                        ticket,
                        fechaHoraPago,
                        ticket.getMontoFinal(),
                        tipo
                );

        siguienteIdPago++;
        pagos.add(pago);

        ticket.marcarPagado();

        return pago;
    }

    public List<TicketParqueo> obtenerTicketsActivos() {
        List<TicketParqueo> activos =
                new ArrayList<>();

        for (TicketParqueo ticket : tickets) {
            if (ticket.estaActivo()) {
                activos.add(ticket);
            }
        }

        return activos;
    }

    /*
     * Devuelve una copia de la lista de vehículos
     * registrados para evitar exponer directamente
     * la colección interna de Parqueo.
     */
    public List<Vehiculo> obtenerVehiculosRegistrados() {
        return new ArrayList<>(vehiculos);
    }

    public List<Vehiculo> obtenerVehiculosDentro() {
        List<Vehiculo> dentro =
                new ArrayList<>();

        for (TicketParqueo ticket : tickets) {
            if (ticket.estaActivo()) {
                dentro.add(
                        ticket.getVehiculo()
                );
            }
        }

        return dentro;
    }

    public double calcularIngresosTotales() {
        double total = 0;

        for (Pago pago : pagos) {
            total += pago.getMonto();
        }

        return total;
    }

    public int obtenerOcupacion(
            TipoEspacio tipo) {

        int ocupados = 0;

        for (EspacioParqueo espacio : espacios) {
            if (espacio.getTipo() == tipo
                    && espacio.getEstado()
                            == EstadoEspacio.OCUPADO) {

                ocupados++;
            }
        }

        return ocupados;
    }

    private Vehiculo buscarVehiculoPorPlaca(
            String placa) {

        for (Vehiculo vehiculo : vehiculos) {
            if (vehiculo.getPlaca()
                    .equalsIgnoreCase(placa)) {

                return vehiculo;
            }
        }

        throw new IllegalArgumentException(
                "No existe un vehículo registrado "
                + "con la placa " + placa + "."
        );
    }

    private TicketParqueo buscarTicketActivo(
            String placa) {

        for (TicketParqueo ticket : tickets) {
            if (ticket.estaActivo()
                    && ticket.getVehiculo()
                            .getPlaca()
                            .equalsIgnoreCase(placa)) {

                return ticket;
            }
        }

        throw new IllegalStateException(
                "El vehículo no tiene un ticket activo."
        );
    }

    public String getNombre() {
        return nombre;
    }
}