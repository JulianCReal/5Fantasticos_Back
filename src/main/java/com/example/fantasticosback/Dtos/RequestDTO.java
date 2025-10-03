package com.example.fantasticosback.Dtos;

import com.example.fantasticosback.Model.Group;

import java.util.Date;

public class RequestDTO extends BaseDTO {
    private String studentId;
    private Group originGroup;
    private Group destinationGroup;
    private String type;
    private String observations;
    private String stateName;
    private Date requestDate;
    private int priority;
    private Boolean evaluationApproved;

    public RequestDTO(String id, String studentId, Group originGroup, Group destinationGroup, String type,
                      String observations, String stateName, Date requestDate,
                      int priority, Boolean evaluationApproved) {
        super(id);
        this.studentId = studentId;
        this.originGroup = originGroup;
        this.destinationGroup = destinationGroup;
        this.type = type;
        this.observations = observations;
        this.stateName = stateName;
        this.requestDate = requestDate;
        this.priority = priority;
        this.evaluationApproved = evaluationApproved;
    }

    public String getStudentId() {return studentId;}

    public Group getOriginGroup() {return originGroup;}

    public Group getDestinationGroup() {return destinationGroup;}

    public String getType() {return type;}

    public void setStudentId(String studentId) {this.studentId = studentId;}

    public void setOriginGroup(Group groupOrigen) {this.originGroup = groupOrigen;}

    public void setDestinationGroup(Group groupDestino) {this.destinationGroup = groupDestino;}

    public void setType(String type) {this.type = type;}

    public void setObservations(String observations) {this.observations = observations;}

    public void setStateName(String stateName) {this.stateName = stateName;}

    public void setRequestDate(Date requestDate) {this.requestDate = requestDate;}

    public void setPriority(int priority) {this.priority = priority;}

    public void setEvaluationApproved(Boolean evaluationApproved) {this.evaluationApproved = evaluationApproved;}

    public String getObservations() {return observations;}

    public String getStateName() {return stateName;}

    public Date getRequestDate() {return requestDate;}

    public int getPriority() {return priority;}
    public Boolean getEvaluationApproved() {return evaluationApproved;}
}
