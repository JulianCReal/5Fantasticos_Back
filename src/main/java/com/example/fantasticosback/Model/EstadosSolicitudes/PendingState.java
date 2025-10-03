package com.example.fantasticosback.Model.EstadosSolicitudes;

import com.example.fantasticosback.Model.Request;

public class PendingState implements RequestState {

    @Override
    public void changeState(Request request, String rolUsuario) {
        if (!rolUsuario.equals("Decanatura") && !rolUsuario.equals("Administrativo")) {
            throw new IllegalStateException("Solo decanatura o administrativos pueden mover una solicitud a 'En Proceso'");
        }
        request.setState(new InProcessState());
    }

    @Override
    public String getStateName() {
        return "Pendiente";
    }
}
