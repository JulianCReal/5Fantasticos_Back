package com.example.fantasticosback.model.Document;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.logging.Logger;

@Document(collection = "Teachers")
public class Teacher extends Person {

    @Id
    private String id;
    private String department;
    private HashMap<String, Subject> assignedSubjects;
    private static final Logger log = Logger.getLogger(Teacher.class.getName());

    public Teacher() {
        super();
        this.assignedSubjects = new HashMap<>();
    }

    public Teacher(String name, String lastName, String document, String department) {
        super(name, lastName, document);
        this.department = department;
        this.assignedSubjects = new HashMap<>();
    }

    // Getters y Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public HashMap<String, Subject> getAssignedSubjects() {
        return assignedSubjects;
    }

    public void setAssignedSubjects(HashMap<String, Subject> assignedSubjects) {
        this.assignedSubjects = assignedSubjects;
    }

    public void assignSubject(Subject subject) {
        String code = String.valueOf(subject.getSubjectId());
        if (assignedSubjects.containsKey(code)) {
            log.warning("Subject " + subject.getName() + " is already assigned to teacher " + this.name + " " + this.lastName);
        } else {
            assignedSubjects.put(code, subject);
            log.info("Subject " + subject.getName() + " assigned to teacher " + this.name + " " + this.lastName);
        }
    }

    @Override
    public void showInformation() {
        log.info(() -> "Teacher: " + name + " " + lastName +
                ", Document: " + document +
                ", Department: " + department +
                ", Assigned subjects: " + assignedSubjects.size());
    }
}