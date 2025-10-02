package com.example.fantasticosback.Dtos;

import com.example.fantasticosback.Model.Grupo;

import java.util.Date;

public class RequestDTO extends BaseDTO {
    private String studentId;
    private Grupo grupoOrigen;
    private Grupo grupoDestino;
    private String tipo;
    private String observaciones;
    private String nombreEstado;
    private Date fechaSolicitud;
    private int prioridad;
    private Boolean evaluacionAprobada;

    public RequestDTO(String id, String studentId, Grupo grupoOrigen, Grupo grupoDestino, String tipo,
                      String observaciones, String nombreEstado, Date fechaSolicitud,
                      int prioridad, Boolean evaluacionAprobada) {
        super(id);
        this.studentId = studentId;
        this.grupoOrigen = grupoOrigen;
        this.grupoDestino = grupoDestino;
        this.tipo = tipo;
        this.observaciones = observaciones;
        this.nombreEstado = nombreEstado;
        this.fechaSolicitud = fechaSolicitud;
        this.prioridad = prioridad;
        this.evaluacionAprobada = evaluacionAprobada;
    }

    public String getStudentId() {return studentId;}

    public Grupo getGrupoOrigen() {return grupoOrigen;}

    public Grupo getGrupoDestino() {return grupoDestino;}

    public String getTipo() {return tipo;}

    public void setStudentId(String studentId) {this.studentId = studentId;}

    public void setGrupoOrigen(Grupo grupoOrigen) {this.grupoOrigen = grupoOrigen;}

    public void setGrupoDestino(Grupo grupoDestino) {this.grupoDestino = grupoDestino;}

    public void setTipo(String tipo) {this.tipo = tipo;}

    public void setObservaciones(String observaciones) {this.observaciones = observaciones;}

    public void setNombreEstado(String nombreEstado) {this.nombreEstado = nombreEstado;}

    public void setFechaSolicitud(Date fechaSolicitud) {this.fechaSolicitud = fechaSolicitud;}

    public void setPrioridad(int prioridad) {this.prioridad = prioridad;}

    public void setEvaluacionAprobada(Boolean evaluacionAprobada) {this.evaluacionAprobada = evaluacionAprobada;}

    public String getObservaciones() {return observaciones;}

    public String getNombreEstado() {return nombreEstado;}

    public Date getFechaSolicitud() {return fechaSolicitud;}

    public int getPrioridad() {return prioridad;}
    public Boolean getEvaluacionAprobada() {return evaluacionAprobada;}
}
