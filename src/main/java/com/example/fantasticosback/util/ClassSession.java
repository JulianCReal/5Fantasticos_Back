package com.example.fantasticosback.util;

public class ClassSession {
    private String day;
    private String startTime;
    private String endTime;
    private String classroom;

    public ClassSession() {
    }

    public ClassSession(String day, String startTime, String endTime, String classroom) {
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
        this.classroom = classroom;
    }

    // Getters y Setters
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

    public boolean verifyConflict(ClassSession otherSession) {
        System.out.println("=== DEBUG CONFLICT ===");
        System.out.println("This session: " + this.day + " " + this.startTime + "-" + this.endTime);
        System.out.println("Other session: " + otherSession.day + " " + otherSession.startTime + "-" + otherSession.endTime);

        if (!this.day.equals(otherSession.day)) {
            System.out.println("No conflict: different days");
            return false;
        } else if (this.startTime.equals(otherSession.startTime) || this.endTime.equals(otherSession.endTime)) {
            System.out.println("CONFLICT FOUND: same times");
            return true;
        }
        System.out.println("No conflict: different times");
        return false;
    }
}
