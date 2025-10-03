package com.example.fantasticosback.Model.EstadosSolicitudes;

import com.example.fantasticosback.Model.Request;

public class RejectedState implements RequestState {

    @Override
    public void changeState(Request request, String rolUsuario) {
        throw new IllegalStateException("La solicitud fue rechazada. No puede cambiar de estado.");
    }

    @Override
    public String getStateName() {
        return "Rechazada";
    }
}
