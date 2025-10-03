package com.example.fantasticosback.Model.EstadosSolicitudes;

import com.example.fantasticosback.Model.Request;

public class InProcessState implements RequestState {

    @Override
    public void changeState(Request request, String rolUsuario) {
        if (!rolUsuario.equals("Decanatura") && !rolUsuario.equals("Administrativo")) {
            throw new IllegalStateException("Solo decanatura o administrativos pueden aceptar o rechazar solicitudes.");
        }
        boolean evaluacion = request.getEvaluacionAprobada();

        if (evaluacion) {
            request.setState(new AcceptedState());
        } else {
            request.setState(new RejectedState());
        }
    }

    @Override
    public String getStateName() {
        return "En Proceso";
    }
}
