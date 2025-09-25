package com.example.fantasticosback.Model.EstadosSolicitudes;

import com.example.fantasticosback.Model.Solicitud;

public class EstadoAceptada implements EstadoSolicitud {

    @Override
    public void changeState(Solicitud solicitud, String rolUsuario) {
        throw new IllegalStateException("La solicitud ya fue aceptada. No puede cambiar de estado.");
    }

    @Override
    public String getNombreEstado() {
        return "Aceptada";
    }
}
