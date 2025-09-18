package com.example._Fantasticos_Back.util;
import java.util.Date;

public abstract class Solicitud {
    private static int contadorPrioridad =0;
    private int id;
    private int grupoMateria;
    private String  observaciones;
    private String estado;
    private Date fechaSolicitud;
    private int prioridad;

    public Solicitud(int id, int grupoMateria, String observaciones, String estado, Date fechaSolicitud) {
        this.id = id;
        this.grupoMateria = grupoMateria;
        this.observaciones = observaciones;
        this.estado = estado;
        this.fechaSolicitud = fechaSolicitud;
        this.prioridad = contadorPrioridad++; 
    }

    public void cambiarEstado(String nuevoEstado) {
        this.estado = nuevoEstado;
    }

    public int getPrioridad() {
        return this.prioridad;
    }

}
