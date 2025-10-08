package com.example.fantasticosback.Model.Entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.*;
import java.util.logging.Logger;

@Document(collection = "Subjects")
public class Subject {

    @Id
    private String subjectId;
    private String name;
    private int credits;
    private int semester;
    private ArrayList<Group> availableGroups = new ArrayList<>();
    private static final Logger logger = Logger.getLogger(Subject.class.getName());
    private static Map<String, Subject> subjectCatalog = new HashMap<>();

    public Subject() {
        this.availableGroups = new ArrayList<>();
    }

    public Subject(String subjectId, String name, int credits, int semester) {
        this.subjectId = subjectId;
        this.name = name;
        this.credits = credits;
        this.semester = semester;
        this.availableGroups = new ArrayList<>();
    }

    // Getters y Setters
    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public ArrayList<Group> getAvailableGroups() {
        return availableGroups;
    }

    public void setAvailableGroups(ArrayList<Group> availableGroups) {
        this.availableGroups = availableGroups;
    }

    public void addGroup(Group group) {
        this.availableGroups.add(group);
    }

    public List<Group> getGroups() {
        return availableGroups.stream()
                .filter(group -> group.isActive())
                .toList();
    }

    public void showInformation() {
        logger.info(() -> "Subject: " + name +
                " (ID: " + subjectId +
                ", Credits: " + credits +
                ", Semester: " + semester +
                ", Available groups: " + availableGroups.size() + ")");
    }
}
