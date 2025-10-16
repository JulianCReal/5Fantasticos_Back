package com.example.fantasticosback.dto.request;

public class CreateGroupRequestDTO {
    private String subjectId;
    private int number;
    private int capacity;
    private boolean active;
    private String teacherId;

    public CreateGroupRequestDTO() {}

    public CreateGroupRequestDTO(String subjectId, int number, int capacity, boolean active, String teacherId) {
        this.subjectId = subjectId;
        this.number = number;
        this.capacity = capacity;
        this.active = active;
        this.teacherId = teacherId;
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
}

