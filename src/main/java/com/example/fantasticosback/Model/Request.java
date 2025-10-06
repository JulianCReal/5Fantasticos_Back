package com.example.fantasticosback.Model;

import com.example.fantasticosback.Model.RequestStates.RequestState;
import com.example.fantasticosback.Model.RequestStates.PendingState;
import com.example.fantasticosback.util.States;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;

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

    private transient RequestState state;
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

        setState(new PendingState());
    }

    public String getStudentId() {
        return studentId;
    }
    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getRequestId() {
        return id;
    }
    public void setRequestId(String id) {
        this.id = id;
    }

    public Group getSourceGroup() {
        return sourceGroup;
    }

    public void setSourceGroup(Group sourceGroup) {
        this.sourceGroup = sourceGroup;
    }

    public Group getDestinationGroup() {
        return destinationGroup;
    }

    public void setDestinationGroup(Group destinationGroup) {
        this.destinationGroup = destinationGroup;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public String getObservations() {
        return observations;
    }
    public void setObservations(String observations) {
        this.observations = observations;
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

}
