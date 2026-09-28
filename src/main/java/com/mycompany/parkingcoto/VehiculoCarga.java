package com.mycompany.parkingcoto;

public class VehiculoCarga extends Vehiculo {

    public VehiculoCarga(String placa, String marca, String modelo, String color) {
        super(placa, marca, modelo, color, TipoVehiculo.CARGA);
    }

    @Override
    public double getTarifaPorHora() {
        return 1500;
    }

    @Override
    public double getTarifaMaximaDiaria() {
        return 11000;
    }
}