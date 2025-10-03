package com.example.fantasticosback.util;

import com.example.fantasticosback.Model.EstadosSolicitudes.*;

import java.util.HashMap;
import java.util.Map;

public class States {
    private static Map<String, RequestState> estados = new HashMap<>();

    static {
        estados.put("Pendiente", new PendingState());
        estados.put("En Proceso", new InProcessState());
        estados.put("Aceptada", new AcceptedState());
        estados.put("Rechazada", new RejectedState());

    }

    public static Map<String, RequestState> getEstados() {
        return estados;
    }
}
