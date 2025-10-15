package com.example.fantasticosback.dto.response;

public class CreateGroupDTO {
    private int number;
    private int capacity;
    private boolean active;
    private String teacherId; // Solo el ID del profesor, no el objeto completo

    public CreateGroupDTO() {}

    public CreateGroupDTO(int number, int capacity, boolean active, String teacherId) {
        this.number = number;
        this.capacity = capacity;
        this.active = active;
        this.teacherId = teacherId;
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
