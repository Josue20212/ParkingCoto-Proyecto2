package com.mycompany.parkingcoto;

public class ParkingCoto {

    public static void main(String[] args) {

        Parqueo parqueo = new Parqueo("Parking Coto");

        MenuConsola menu = new MenuConsola(parqueo);

        menu.iniciar();
    }
}