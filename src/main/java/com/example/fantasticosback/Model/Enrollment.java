package com.example.fantasticosback.Model;

import com.example.fantasticosback.util.ClassSession;

public class Enrollment {
    private Group group;
    private int id;
    private String status;
    private double finalGrade;


    public Enrollment(Group group, int id, String status, double finalGrade) {
        this.group = group;
        this.id = id;
        this.status = status;
        this.finalGrade = finalGrade;
    }

    // Getters y Setters
    public Group getGroup() {
        return group;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public double getFinalGrade() {
        return finalGrade;
    }


    public void cancel() {
        this.status = "cancelled";
    }

    public void evaluate() {
        if (finalGrade >= 30) {
            this.status = "approved";
        } else {
            this.status = "failed";
        }
    }


    public void changeGroup(Group newGroup) {
        this.group = newGroup;
    }

    public boolean validateConflict(Enrollment other) {
        for (ClassSession session1 : this.group.getSessions()) {
            for (ClassSession session2 : other.group.getSessions()) {
                if (session1.verifyConflict(session2)) {
                    return true;
                }
            }
        }
        return false;
    }
}
