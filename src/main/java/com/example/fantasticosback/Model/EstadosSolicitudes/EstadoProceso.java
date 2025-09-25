package com.example.fantasticosback.Model.EstadosSolicitudes;

import com.example.fantasticosback.Model.Solicitud;

public class EstadoProceso implements EstadoSolicitud {

    @Override
    public void changeState(Solicitud solicitud, String rolUsuario) {
        if (!rolUsuario.equals("Decanatura") && !rolUsuario.equals("Administrativo")) {
            throw new IllegalStateException("Solo decanatura o administrativos pueden aceptar o rechazar solicitudes.");
        }
        boolean evaluacion = solicitud.getEvaluacionAprobada();

        if (evaluacion) {
            solicitud.setEstado(new EstadoAceptada());
        } else {
            solicitud.setEstado(new EstadoRechazada());
        }
    }

    @Override
    public String getNombreEstado() {
        return "En Proceso";
    }
}
