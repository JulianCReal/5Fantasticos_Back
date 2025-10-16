package com.example.fantasticosback.model.Document;

import com.example.fantasticosback.util.ClassRequirement;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.*;
import java.util.logging.Logger;

@Data
@Document(collection = "Subjects")
public class Subject {

    @Id
    private String subjectId;
    private String name;
    private int credits;
    private int semester;
    private LinkedList<ClassRequirement> requirements;

    private ArrayList<Group> availableGroups;
    private static final Logger logger = Logger.getLogger(Subject.class.getName());
    private static Map<String, Subject> subjectCatalog = new HashMap<>();

    public Subject() {
        this.availableGroups = new ArrayList<>();
    }

    public Subject(String subjectId, String name, int credits, int semester, LinkedList<ClassRequirement> requirements) {
        this.subjectId = subjectId;
        this.name = name;
        this.credits = credits;
        this.semester = semester;
        this.availableGroups = new ArrayList<>();
        this.requirements = requirements;
    }

    public Subject (String subjectId, String name, int credits, int semester) {
        this.subjectId = subjectId;
        this.name = name;
        this.credits = credits;
        this.semester = semester;
        this.availableGroups = new ArrayList<>();
        this.requirements = new LinkedList<>();
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
