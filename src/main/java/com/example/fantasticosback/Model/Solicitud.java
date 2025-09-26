package com.example.fantasticosback.Model;

import com.example.fantasticosback.Model.EstadosSolicitudes.EstadoPendiente;
import com.example.fantasticosback.Model.EstadosSolicitudes.EstadoSolicitud;
import com.example.fantasticosback.util.Estados;
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
    private String tipo;
    private String observaciones;
    private String nombreEstado;
    private Date fechaSolicitud;
    private int prioridad;

    private transient EstadoSolicitud estado;
    private transient boolean evaluacionSolicitud;

    public Solicitud(int solicitudId, Grupo grupoOrigen, Grupo grupoDestino, String tipo, String observaciones, Date fechaSolicitud, String idEstudiante) {
        this.solicitudId = solicitudId;
        this.grupoOrigen = grupoOrigen;
        this.grupoDestino = grupoDestino;
        this.tipo = tipo;
        this.observaciones = observaciones;
        this.fechaSolicitud = fechaSolicitud;
        this.id = idEstudiante;
        this.prioridad = contadorPrioridad++;

        setEstado(new EstadoPendiente());
    }

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

    public void recoverState() {
        EstadoSolicitud estadoReconstruido = Estados.getEstados().get(nombreEstado);
        if (estadoReconstruido == null) {
            throw new IllegalStateException("Estado desconocido: " + nombreEstado);
        }
        this.estado = estadoReconstruido;
    }
    public EstadoSolicitud getEstado() {
        if (estado == null) {
            recoverState();
        }
        return estado;
    }

    public void setEstado(EstadoSolicitud estado) {
        this.estado = estado;
        this.nombreEstado = estado.getNombreEstado();
    }

    public void procesar(String rolUsuario) {
        if (estado == null) {
            recoverState();
        }
        estado.changeState(this, rolUsuario);
    }
    public void setEvaluacionAprobada(Boolean evaluacionAprobada) {
        this.evaluacionSolicitud = evaluacionAprobada;
    }
    public Boolean getEvaluacionAprobada() {
        return evaluacionSolicitud;
    }

}
