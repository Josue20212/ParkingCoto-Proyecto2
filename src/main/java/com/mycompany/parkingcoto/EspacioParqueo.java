package com.mycompany.parkingcoto;

public class EspacioParqueo {

    private String id;
    private TipoEspacio tipo;
    private EstadoEspacio estado;

    public EspacioParqueo(String id, TipoEspacio tipo) {
        this.id = id;
        this.tipo = tipo;
        this.estado = EstadoEspacio.DISPONIBLE;
    }

    public boolean estaDisponible() {
        return estado == EstadoEspacio.DISPONIBLE;
    }

    public boolean esCompatible(Vehiculo vehiculo) {
        return switch (vehiculo.getTipo()) {
            case AUTOMOVIL -> tipo == TipoEspacio.AUTOMOVIL;
            case MOTOCICLETA -> tipo == TipoEspacio.MOTOCICLETA;
            case CARGA -> tipo == TipoEspacio.CARGA;
        };
    }

    public void ocupar() {
        if (estado == EstadoEspacio.OCUPADO) {
            throw new IllegalStateException("El espacio ya se encuentra ocupado.");
        }

        if (estado == EstadoEspacio.FUERA_DE_SERVICIO) {
            throw new IllegalStateException("El espacio se encuentra fuera de servicio.");
        }

        estado = EstadoEspacio.OCUPADO;
    }

    public void liberar() {
        estado = EstadoEspacio.DISPONIBLE;
    }

    public void ponerFueraDeServicio() {
        if (estado == EstadoEspacio.OCUPADO) {
            throw new IllegalStateException(
                    "No se puede poner fuera de servicio un espacio ocupado."
            );
        }

        estado = EstadoEspacio.FUERA_DE_SERVICIO;
    }

    public String getId() {
        return id;
    }

    public TipoEspacio getTipo() {
        return tipo;
    }

    public EstadoEspacio getEstado() {
        return estado;
    }
}