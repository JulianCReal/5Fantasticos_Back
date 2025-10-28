package com.example.fantasticosback.model.Document;

import com.example.fantasticosback.model.RequestStates.RequestState;
import com.example.fantasticosback.model.RequestStates.PendingState;
import com.example.fantasticosback.enums.RequestPriority;
import com.example.fantasticosback.enums.RequestType;
import com.example.fantasticosback.enums.Role;
import com.example.fantasticosback.util.States;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

@Getter
@Setter
@Document(collection = "Requests")
public class Request {

    @Id
    private String id;

    private String userId;
    private Group sourceGroup;
    private Group destinationGroup;
    private String observations;
    private String stateName;
    private LocalDateTime requestDate;
    private String deanOffice;
    private String responseMessage;
    private Object aditionalData;

    private RequestType type;
    private Role creatorRole;
    private LocalDateTime requestResponseTime;
    private RequestPriority requestPriority;
    private HashMap<String, String> historyResponses;

    @Transient
    private transient RequestState state;
    @Transient
    private transient boolean requestEvaluation;

    public Request() {
        setState(new PendingState());
        this.historyResponses = new HashMap<>();
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

    public void setEvaluationApproved(Boolean evaluationApproved) {
        this.requestEvaluation = evaluationApproved;
    }
    public Boolean getEvaluationApproved() {
        return requestEvaluation;
    }
    public void setHistoryResponses(String response) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        String fechaSinPuntos = requestResponseTime.format(formatter);
        this.historyResponses.put(fechaSinPuntos, response);
    }
}
