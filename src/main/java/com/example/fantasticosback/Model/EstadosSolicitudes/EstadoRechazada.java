package com.example.fantasticosback.Model.EstadosSolicitudes;

import com.example.fantasticosback.Model.Solicitud;

public class EstadoRechazada implements EstadoSolicitud {

    @Override
    public void changeState(Solicitud solicitud, String rolUsuario) {
        throw new IllegalStateException("La solicitud fue rechazada. No puede cambiar de estado.");
    }

    @Override
    public String getNombreEstado() {
        return "Rechazada";
    }
}
