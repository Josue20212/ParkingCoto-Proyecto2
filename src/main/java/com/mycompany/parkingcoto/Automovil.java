package com.mycompany.parkingcoto;

public class Automovil extends Vehiculo {

    public Automovil(String placa, String marca, String modelo, String color) {
        super(placa, marca, modelo, color, TipoVehiculo.AUTOMOVIL);
    }

    @Override
    public double getTarifaPorHora() {
        return 900;
    }

    @Override
    public double getTarifaMaximaDiaria() {
        return 7000;
    }
}