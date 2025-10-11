package com.example.fantasticosback.Dtos;

import com.example.fantasticosback.Model.Document.Group;

import java.util.Date;

public class RequestDTO extends BaseDTO {
    private String studentId;
    private Group sourceGroup;
    private Group destinationGroup;
    private String type;
    private String observations;
    private String statusName;
    private Date requestDate;
    private int priority;
    private Boolean evaluationApproved;

    public RequestDTO(String id, String studentId, Group sourceGroup, Group destinationGroup, String type,
                      String observations, String statusName, Date requestDate,
                      int priority, Boolean evaluationApproved) {
        super(id);
        this.studentId = studentId;
        this.sourceGroup = sourceGroup;
        this.destinationGroup = destinationGroup;
        this.type = type;
        this.observations = observations;
        this.statusName = statusName;
        this.requestDate = requestDate;
        this.priority = priority;
        this.evaluationApproved = evaluationApproved;
    }

    public String getStudentId() {return studentId;}

    public Group getSourceGroup() {return sourceGroup;}

    public Group getDestinationGroup() {return destinationGroup;}

    public String getType() {return type;}

    public void setStudentId(String studentId) {this.studentId = studentId;}

    public void setSourceGroup(Group sourceGroup) {this.sourceGroup = sourceGroup;}

    public void setDestinationGroup(Group destinationGroup) {this.destinationGroup = destinationGroup;}

    public void setType(String type) {this.type = type;}

    public void setObservations(String observations) {this.observations = observations;}

    public void setStatusName(String statusName) {this.statusName = statusName;}

    public void setRequestDate(Date requestDate) {this.requestDate = requestDate;}

    public void setPriority(int priority) {this.priority = priority;}

    public void setEvaluationApproved(Boolean evaluationApproved) {this.evaluationApproved = evaluationApproved;}

    public String getObservations() {return observations;}

    public String getStatusName() {return statusName;}

    public Date getRequestDate() {return requestDate;}

    public int getPriority() {return priority;}

    public Boolean getEvaluationApproved() {return evaluationApproved;}
}
