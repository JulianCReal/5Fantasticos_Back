package com.example.fantasticosback.Model.Document;

import java.util.ArrayList;

public class Semester {
    private int id;
    private int year;
    private int academicPeriod;
    private boolean active;
    private int semesterAverage;
    private ArrayList<Enrollment> subjects = new ArrayList<>();

    public Semester() {
        this.subjects = new ArrayList<>();
    }

    public Semester(int id, int year, int academicPeriod, boolean active) {
        this.id = id;
        this.year = year;
        this.academicPeriod = academicPeriod;
        this.active = active;
        this.subjects = new ArrayList<>();
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

    public ArrayList<Enrollment> getSubjects() {
        return subjects;
    }

    public void setSubjects(ArrayList<Enrollment> subjects) {
        this.subjects = subjects;
    }

    public void addSubject(Enrollment subject) {
        this.subjects.add(subject);
    }

    public void removeSubject(Enrollment subject) {
        this.subjects.remove(subject);
    }

    public void cancelSubject(Enrollment subject) {
        subject.cancel();
    }

    public void calculateSemesterAverage() {
        double gradeCreditsSum = 0.0;
        int totalCredits = 0;

        for (Enrollment enrollment : subjects) {
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
