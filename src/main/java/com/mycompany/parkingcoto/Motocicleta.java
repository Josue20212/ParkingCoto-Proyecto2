package com.mycompany.parkingcoto;

public class Motocicleta extends Vehiculo {

    public Motocicleta(String placa, String marca, String modelo, String color) {
        super(placa, marca, modelo, color, TipoVehiculo.MOTOCICLETA);
    }

    @Override
    public double getTarifaPorHora() {
        return 500;
    }

    @Override
    public double getTarifaMaximaDiaria() {
        return 4000;
    }
}