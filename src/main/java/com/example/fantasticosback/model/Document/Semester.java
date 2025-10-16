package com.example.fantasticosback.model.Document;

public class Semester {
    private int id;
    private int year;
    private int academicPeriod;
    private boolean active;
    private int semesterAverage;
    private Schedule schedule;

    public Semester() {
        this.schedule = new Schedule();
    }

    public Semester(int id, int year, int academicPeriod, boolean active) {
        this.id = id;
        this.year = year;
        this.academicPeriod = academicPeriod;
        this.active = active;
        this.schedule = new Schedule();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getAcademicPeriod() {
        return academicPeriod;
    }

    public void setAcademicPeriod(int academicPeriod) {
        this.academicPeriod = academicPeriod;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getSemesterAverage() {
        return semesterAverage;
    }

    public void setSemesterAverage(int semesterAverage) {
        this.semesterAverage = semesterAverage;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public void calculateSemesterAverage() {
        double gradeCreditsSum = 0.0;
        int totalCredits = 0;

        for (Enrollment enrollment : schedule.getEnrollments()) {
            int credits = enrollment.getSubject().getCredits();
            double grade = enrollment.getFinalGrade();

            gradeCreditsSum += grade * credits;
            totalCredits += credits;
        }

        if (totalCredits > 0) {
            this.semesterAverage = (int) (gradeCreditsSum / totalCredits);
        }
    }
}
