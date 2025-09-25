package com.example.fantasticosback.Model.Observers;


import com.example.fantasticosback.Model.Solicitud;

public class StudentObserver implements ObserverSolicitud {
    @Override
    public void notificarSolicitud(Solicitud solicitud) {
        System.out.println("Tu solicitud ha sido modificada a: " + solicitud.getEstado().getNombreEstado());
    }
}
