package com.example.fantasticosback.Model.EstadosSolicitudes;

import com.example.fantasticosback.Model.Solicitud;

public class EstadoPendiente implements EstadoSolicitud {

    @Override
    public void changeState(Solicitud solicitud, String rolUsuario) {
        if (!rolUsuario.equals("Decanatura") && !rolUsuario.equals("Administrativo")) {
            throw new IllegalStateException("Solo decanatura o administrativos pueden mover una solicitud a 'En Proceso'");
        }
        solicitud.setEstado(new EstadoProceso());
    }

    @Override
    public String getNombreEstado() {
        return "Pendiente";
    }
}
