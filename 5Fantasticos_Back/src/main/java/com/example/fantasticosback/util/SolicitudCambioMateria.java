package com.example.fantasticosback.util;

import java.util.Date;
public class SolicitudCambioMateria  extends Solicitud {
    private Materia materiaCambio;
    public SolicitudCambioMateria(int id, int grupoMateria, String observaciones, String estado, Date fechaSolicitud) {
        super(id, grupoMateria, observaciones, estado, fechaSolicitud);
    }
}
