package com.example.fantasticosback.model.Document;

import com.example.fantasticosback.util.ClassSession;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "enrollments")
public class Enrollment {
    @Id
    private String id;

    private String studentId;
    
    @DBRef
    private Group group;
    
    @DBRef
    private Subject subject;
    
    private String status;
    private double finalGrade;

    public Enrollment() {}

    public Enrollment(Group group, Subject subject, int id, String status, double finalGrade) {
        this.id = String.valueOf(id);
        this.group = group;
        this.subject = subject;
        this.status = status;
        this.finalGrade = finalGrade;
    }

    public Enrollment(String id, Group group, Subject subject, String status, double finalGrade) {
        this.id = id;
        this.group = group;
        this.subject = subject;
        this.status = status;
        this.finalGrade = finalGrade;
    }

    // Getters y Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    // Método para mantener compatibilidad con los tests
    public int getIdAsInt() {
        try {
            return Integer.parseInt(id);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    // Método para mantener compatibilidad con los tests
    public void setId(int id) {
        this.id = String.valueOf(id);
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getFinalGrade() {
        return finalGrade;
    }

    public void setFinalGrade(double finalGrade) {
        this.finalGrade = finalGrade;
    }

    // Métodos de negocio
    public void cancel() {
        this.status = "cancelled";
    }

    public void evaluate() {
        if (this.finalGrade >= 30.0) {
            this.status = "approved";
        } else {
            this.status = "failed";
        }
    }

    public void changeGroup(Group newGroup) {
        this.group = newGroup;
    }

    public boolean validateConflict(Enrollment other) {
        if (this.group == null || other.group == null) {
            return false;
        }

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
