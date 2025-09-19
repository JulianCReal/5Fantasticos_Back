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

    // Getters y Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getSolicitudId() {
        return solicitudId;
    }

    public void setSolicitudId(int solicitudId) {
        this.solicitudId = solicitudId;
    }

    public Grupo getGrupoOrigen() {
        return grupoOrigen;
    }

    public void setGrupoOrigen(Grupo grupoOrigen) {
        this.grupoOrigen = grupoOrigen;
    }

    public Grupo getGrupoDestino() {
        return grupoDestino;
    }

    public void setGrupoDestino(Grupo grupoDestino) {
        this.grupoDestino = grupoDestino;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Date getFechaSolicitud() {
        return fechaSolicitud;
    }

    public void setFechaSolicitud(Date fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }

    public int getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(int prioridad) {
        this.prioridad = prioridad;
    }

    public void cambiarEstado(String nuevoEstado) {
        this.estado = nuevoEstado;
    }
}
