package com.example.fantasticosback.Model.Entities;

import com.example.fantasticosback.Model.RequestStates.RequestState;
import com.example.fantasticosback.Model.RequestStates.PendingState;
import com.example.fantasticosback.util.States;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;

@Getter
@Setter
@Document(collection = "Requests")
public class Request {

    @Id
    private String id;

    private String studentId;
    private static int priorityCounter = 0;
    private Group sourceGroup;
    private Group destinationGroup;
    private String type;
    private String observations;
    private String stateName;
    private Date requestDate;
    private int priority;
    private HashMap<LocalDateTime, String> historyResponses;

    @Transient
    private transient RequestState state;
    @Transient
    private transient boolean requestEvaluation;

    public Request(String id, Group sourceGroup, Group destinationGroup, String type, String observations, Date requestDate, String studentId) {
        this.id = id;
        this.sourceGroup = sourceGroup;
        this.destinationGroup = destinationGroup;
        this.type = type;
        this.observations = observations;
        this.requestDate = requestDate;
        this.studentId = studentId;
        this.priority = priorityCounter++;
        this.historyResponses = new HashMap<>();
        setState(new PendingState());
    }

    public void recoverState() {
        RequestState reconstructedState = States.getStates().get(stateName);
        if (reconstructedState == null) {
            throw new IllegalStateException("Unknown state: " + stateName);
        }
        this.state = reconstructedState;
    }
    public RequestState getState() {
        if (state == null) {
            recoverState();
        }
        return state;
    }
    public void setState(RequestState state) {
        this.state = state;
        this.stateName = state.getStateName();
    }

    public void process(String userRole) {
        if (state == null) {
            recoverState();
        }
        state.changeState(this, userRole);
    }
    public void setEvaluationApproved(Boolean evaluationApproved) {
        this.requestEvaluation = evaluationApproved;
    }
    public Boolean getEvaluationApproved() {
        return requestEvaluation;
    }

    public String getRequestId() {
        return id;
    }
    public void setHistoryResponses(String response, LocalDateTime time) {
        this.historyResponses.put(time, response);
    }
}
