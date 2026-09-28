package com.mycompany.parkingcoto;

import java.time.LocalDateTime;

public class Pago {

    private int id;
    private TicketParqueo ticket;
    private LocalDateTime fechaHoraPago;
    private double monto;
    private TipoPago tipo;

    public Pago(int id, TicketParqueo ticket,
            LocalDateTime fechaHoraPago, double monto, TipoPago tipo) {

        if (ticket == null) {
            throw new IllegalArgumentException("El ticket no puede ser nulo.");
        }

        if (ticket.estaActivo()) {
            throw new IllegalStateException(
                    "No se puede registrar un pago para un ticket activo."
            );
        }

        if (monto <= 0) {
            throw new IllegalArgumentException(
                    "El monto del pago debe ser mayor que cero."
            );
        }

        this.id = id;
        this.ticket = ticket;
        this.fechaHoraPago = fechaHoraPago;
        this.monto = monto;
        this.tipo = tipo;
    }

    public int getId() {
        return id;
    }

    public TicketParqueo getTicket() {
        return ticket;
    }

    public LocalDateTime getFechaHoraPago() {
        return fechaHoraPago;
    }

    public double getMonto() {
        return monto;
    }

    public TipoPago getTipo() {
        return tipo;
    }
}