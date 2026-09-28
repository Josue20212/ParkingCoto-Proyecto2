package com.mycompany.parkingcoto;

public abstract class Vehiculo {

    private String placa;
    private String marca;
    private String modelo;
    private String color;
    private TipoVehiculo tipo;

    public Vehiculo(String placa, String marca, String modelo, String color, TipoVehiculo tipo) {
        this.placa = placa;
        this.marca = marca;
        this.modelo = modelo;
        this.color = color;
        this.tipo = tipo;
    }

    public String getPlaca() {
        return placa;
    }

    public String getMarca() {
        return marca;
    }

    public String getModelo() {
        return modelo;
    }

    public String getColor() {
        return color;
    }

    public TipoVehiculo getTipo() {
        return tipo;
    }

    public abstract double getTarifaPorHora();

    public abstract double getTarifaMaximaDiaria();

    public double calcularMonto(long horas) {
        if (horas <= 0) {
            return 0;
        }

        long diasCompletos = horas / 24;
        long horasRestantes = horas % 24;

        double monto = diasCompletos * getTarifaMaximaDiaria();

        if (horasRestantes >= 10) {
            monto += getTarifaMaximaDiaria();
        } else {
            monto += horasRestantes * getTarifaPorHora();
        }

        return monto;
    }
}