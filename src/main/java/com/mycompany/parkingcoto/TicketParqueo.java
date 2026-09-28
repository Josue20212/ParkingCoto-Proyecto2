package com.mycompany.parkingcoto;

import java.time.Duration;
import java.time.LocalDateTime;

public class TicketParqueo {

    private int numero;
    private Vehiculo vehiculo;
    private EspacioParqueo espacio;
    private LocalDateTime fechaHoraEntrada;
    private LocalDateTime fechaHoraSalida;
    private EstadoTicket estado;
    private double montoFinal;

    public TicketParqueo(int numero, Vehiculo vehiculo,
            EspacioParqueo espacio, LocalDateTime fechaHoraEntrada) {

        this.numero = numero;
        this.vehiculo = vehiculo;
        this.espacio = espacio;
        this.fechaHoraEntrada = fechaHoraEntrada;
        this.fechaHoraSalida = null;
        this.estado = EstadoTicket.ACTIVO;
        this.montoFinal = 0;
    }

    public boolean estaActivo() {
        return estado == EstadoTicket.ACTIVO;
    }

    public long calcularHorasCobradas() {

        if (fechaHoraSalida == null) {
            throw new IllegalStateException(
                    "El ticket todavía no tiene una fecha de salida."
            );
        }

        long segundos = Duration.between(
                fechaHoraEntrada,
                fechaHoraSalida
        ).getSeconds();

        if (segundos <= 0) {
            throw new IllegalStateException(
                    "La fecha de salida debe ser posterior a la fecha de entrada."
            );
        }

        return (segundos + 3599) / 3600;
    }

    public void cerrar(LocalDateTime fechaHoraSalida) {

        if (estado != EstadoTicket.ACTIVO) {
            throw new IllegalStateException(
                    "Solo se puede cerrar un ticket activo."
            );
        }

        if (!fechaHoraSalida.isAfter(fechaHoraEntrada)) {
            throw new IllegalArgumentException(
                    "La fecha de salida debe ser posterior a la entrada."
            );
        }

        this.fechaHoraSalida = fechaHoraSalida;

        long horasCobradas = calcularHorasCobradas();
        this.montoFinal = vehiculo.calcularMonto(horasCobradas);

        this.estado = EstadoTicket.CERRADO;
    }

    public void marcarPagado() {

        if (estado == EstadoTicket.ACTIVO) {
            throw new IllegalStateException(
                    "No se puede pagar un ticket que todavía está activo."
            );
        }

        if (estado == EstadoTicket.PAGADO) {
            throw new IllegalStateException(
                    "El ticket ya se encuentra pagado."
            );
        }

        estado = EstadoTicket.PAGADO;
    }

    public int getNumero() {
        return numero;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public EspacioParqueo getEspacio() {
        return espacio;
    }

    public LocalDateTime getFechaHoraEntrada() {
        return fechaHoraEntrada;
    }

    public LocalDateTime getFechaHoraSalida() {
        return fechaHoraSalida;
    }

    public EstadoTicket getEstado() {
        return estado;
    }

    public double getMontoFinal() {
        return montoFinal;
    }
}