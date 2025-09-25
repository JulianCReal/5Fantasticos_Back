package com.example.fantasticosback.Model.Observers;

import com.example.fantasticosback.Model.Solicitud;

public class DeanObserver implements ObserverSolicitud{

    @Override
    public void notificarSolicitud(Solicitud solicitud) {
        System.out.println("Se ha creado una nueva solicitud");
    }
}
