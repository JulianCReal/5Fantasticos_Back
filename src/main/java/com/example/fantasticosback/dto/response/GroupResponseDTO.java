package com.example.fantasticosback.dto.response;

import java.util.ArrayList;
import java.util.List;

public class GroupResponseDTO {
    private String id;
    private String subjectId;
    private int number;
    private int capacity;
    private boolean active;
    private String teacherId;
    private String teacherName;
    private List<String> studentIds;
    private List<SessionResponseDTO> sessions;

    public GroupResponseDTO() {
        this.studentIds = new ArrayList<>();
        this.sessions = new ArrayList<>();
    }

    public GroupResponseDTO(String id, String subjectId, int number, int capacity, boolean active,
                           String teacherId, String teacherName, List<String> studentIds,
                           List<SessionResponseDTO> sessions) {
        this.id = id;
        this.subjectId = subjectId;
        this.number = number;
        this.capacity = capacity;
        this.active = active;
        this.teacherId = teacherId;
        this.teacherName = teacherName;
        this.studentIds = studentIds != null ? studentIds : new ArrayList<>();
        this.sessions = sessions != null ? sessions : new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public List<String> getStudentIds() {
        return studentIds;
    }

    public void setStudentIds(List<String> studentIds) {
        this.studentIds = studentIds;
    }

    public List<SessionResponseDTO> getSessions() {
        return sessions;
    }

    public void setSessions(List<SessionResponseDTO> sessions) {
        this.sessions = sessions;
    }
}

