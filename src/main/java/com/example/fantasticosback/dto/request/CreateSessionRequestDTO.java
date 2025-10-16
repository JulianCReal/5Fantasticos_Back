package com.example.fantasticosback.dto.request;

public class CreateSessionRequestDTO {
    private String day;
    private String startTime;
    private String endTime;
    private String classroom;

    public CreateSessionRequestDTO() {}

    public CreateSessionRequestDTO(String day, String startTime, String endTime, String classroom) {
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
        this.classroom = classroom;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }
}

