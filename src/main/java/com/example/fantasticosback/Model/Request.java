package com.example.fantasticosback.Model;

import com.example.fantasticosback.Model.EstadosSolicitudes.PendingState;
import com.example.fantasticosback.Model.EstadosSolicitudes.RequestState;
import com.example.fantasticosback.util.States;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;

@Document(collection = "Solicitudes")
public class Request {

    @Id
    private String id;

    private String studentId;
    private static int priorityCounter = 0;
    private Group originGroup;
    private Group targetGroup;
    private String type;
    private String remarks;
    private String statusName;
    private Date requestDate;
    private int priority;

    private transient RequestState state;
    private transient boolean requestEvaluation;

    public Request(String id, Group originGroup, Group targetGroup, String type, String remarks, Date requestDate, String studentId) {
        this.id = id;
        this.originGroup = originGroup;
        this.targetGroup = targetGroup;
        this.type = type;
        this.remarks = remarks;
        this.requestDate = requestDate;
        this.studentId = studentId;
        this.priority = priorityCounter++;

        setState(new PendingState());
    }

    public String getStudentId() {
        return studentId;
    }
    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }


    public String getSolicitudId() {
        return id;
    }
    public void setSolicitudId(String id) {
        this.id = id;
    }

    public Group getGrupoOrigen() {
        return originGroup;
    }

    public void setGrupoOrigen(Group groupOrigen) {
        this.originGroup = groupOrigen;
    }

    public Group getGrupoDestino() {
        return targetGroup;
    }

    public void setGrupoDestino(Group groupDestino) {
        this.targetGroup = groupDestino;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public String getRemarks() {
        return remarks;
    }
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Date getRequestDate() {
        return requestDate;
    }
    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

    public int getPriority() {
        return priority;
    }
    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void recoverState() {
        RequestState estadoReconstruido = States.getEstados().get(statusName);
        if (estadoReconstruido == null) {
            throw new IllegalStateException("Estado desconocido: " + statusName);
        }
        this.state = estadoReconstruido;
    }
    public RequestState getState() {
        if (state == null) {
            recoverState();
        }
        return state;
    }

    public void setState(RequestState state) {
        this.state = state;
        this.statusName = state.getStateName();
    }

    public void procesar(String rolUsuario) {
        if (state == null) {
            recoverState();
        }
        state.changeState(this, rolUsuario);
    }
    public void setEvaluacionAprobada(Boolean evaluacionAprobada) {
        this.requestEvaluation = evaluacionAprobada;
    }
    public Boolean getEvaluacionAprobada() {
        return requestEvaluation;
    }

}
