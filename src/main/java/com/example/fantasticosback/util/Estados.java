package com.example.fantasticosback.util;

import com.example.fantasticosback.Model.EstadosSolicitudes.*;
import com.example.fantasticosback.Model.Materia;

import java.util.HashMap;
import java.util.Map;

public class Estados {
    private static Map<String, EstadoSolicitud> estados = new HashMap<>();

    static {
        estados.put("Pendiente", new EstadoPendiente());
        estados.put("En Proceso", new EstadoProceso());
        estados.put("Aceptada", new EstadoAceptada());
        estados.put("Rechazada", new EstadoRechazada());

    }

    public static Map<String, EstadoSolicitud> getEstados() {
        return estados;
    }
}
