package com.example.fantasticosback.Model.EstadosSolicitudes;

import com.example.fantasticosback.Model.Solicitud;

public interface EstadoSolicitud {
    void changeState(Solicitud solicitud, String rolUsuario);
    String getNombreEstado();
}
