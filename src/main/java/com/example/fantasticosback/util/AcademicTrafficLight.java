package com.example.fantasticosback.util;
import com.example.fantasticosback.Model.Entities.Career;
import com.example.fantasticosback.Model.Entities.Enrollment;
import com.example.fantasticosback.Model.Entities.Subject;
import com.example.fantasticosback.Model.Entities.Semester;

import java.util.ArrayList;

public class AcademicTrafficLight {
    private int id;
    private int progressPercentage;
    private ArrayList<Enrollment> subjects = new ArrayList<>();
    private double cumulativeAverage;
    private int approvedCredits;
    private ArrayList<Semester> semesters = new ArrayList<>();
    private Career career;

    public AcademicTrafficLight(int id, int progressPercentage, Career career) {
        this.id = id;
        this.progressPercentage = progressPercentage;
        this.cumulativeAverage = 0.0;
        this.approvedCredits = 0;
        this.career = career;
    }

    public void addSemester(Semester semester) {
        this.semesters.add(semester);
    }

    public void updateProgress(ArrayList<Enrollment> subjects) {
        this.subjects = subjects;
        this.approvedCredits = 0; // Reset counter
        for (Enrollment enrollment : subjects) {
            if (enrollment.getStatus().equals("approved")) {
                approvedCredits += enrollment.getGroup().getSubject().getCredits();
            }
        }
        calculateCumulativeAverage();
        double calculatedProgress = (double) approvedCredits / career.getTotalCredits() * 100;
        this.progressPercentage = (int) calculatedProgress;
    }

    private void calculateCumulativeAverage() {
        double gradeCreditsSum = 0.0;
        int coursedCredits = 0;

        for (Enrollment enrollment : subjects) {
            int credits = enrollment.getGroup().getSubject().getCredits();
            double grade = enrollment.getFinalGrade();
            gradeCreditsSum += grade * credits;
            coursedCredits += credits;
        }

        if (coursedCredits > 0) {
            double calculatedAverage = gradeCreditsSum / coursedCredits;
            this.cumulativeAverage = Math.round(calculatedAverage * 10.0) / 10.0;
        } else {
            this.cumulativeAverage = 0.0;
        }
    }

    public int getId() {
        return id;
    }

    public int getProgressPercentage() {
        return progressPercentage;
    }

    public double getCumulativeAverage() {
        return cumulativeAverage;
    }

    public ArrayList<Semester> getSemesters() {
        return semesters;
    }

    public ArrayList<Subject> getAllSubjects() {
        if (career != null && career.getSubjects() != null) {
            return career.getSubjects();
        }
        return new ArrayList<>();
    }

    public ArrayList<Enrollment> getSubjects() {
        return subjects;
    }

    public int getApprovedCredits() {
        return approvedCredits;
    }


}
