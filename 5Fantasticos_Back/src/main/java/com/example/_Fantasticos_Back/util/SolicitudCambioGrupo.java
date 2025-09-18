package com.example._Fantasticos_Back.util;

public class SolicitudCambioGrupo extends Solicitud {
    private int grupoDestino;

    public SolicitudCambioGrupo(int id, int grupoMateria, String observaciones, String estado, java.util.Date fechaSolicitud) {
        super(id, grupoMateria, observaciones, estado, fechaSolicitud);
    }
}
