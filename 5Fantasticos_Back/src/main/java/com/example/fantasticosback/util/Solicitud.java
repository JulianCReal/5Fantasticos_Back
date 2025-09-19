package com.example.fantasticosback.util;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;

@Document(collection = "Solicitudes")
public class Solicitud {

    @Id
    private String id;
    private static int contadorPrioridad = 0;
    private int solicitudId;
    private Grupo grupoOrigen;
    private Grupo grupoDestino;
    private String tipo; // "grupo" o "materia"
    private String observaciones;
    private String estado;
    private Date fechaSolicitud;
    private int prioridad;

    public Solicitud(int solicitudId, Grupo grupoOrigen, Grupo grupoDestino, String tipo, String observaciones, String estado, Date fechaSolicitud) {
        this.solicitudId = solicitudId;
        this.grupoOrigen = grupoOrigen;
        this.grupoDestino = grupoDestino;
        this.tipo = tipo;
        this.observaciones = observaciones;
        this.estado = estado;
        this.fechaSolicitud = fechaSolicitud;
        this.prioridad = contadorPrioridad++;
    }

    public void cambiarEstado(String nuevoEstado) {
        this.estado = nuevoEstado;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public int getSolicitudId() { return solicitudId; }

    public Grupo getGrupoOrigen() { return grupoOrigen; }

    public Grupo getGrupoDestino() { return grupoDestino; }

    public String getTipo() { return tipo; }

}
