package com.example.fantasticosback.Model.EstadosSolicitudes;

import com.example.fantasticosback.Model.Request;

public interface RequestState {
    void changeState(Request request, String rolUsuario);
    String getStateName();
}
