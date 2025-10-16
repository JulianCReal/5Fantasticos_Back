package com.example.fantasticosback.model.Document;

import com.example.fantasticosback.util.ClassSession;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;


@Data
@Document(collection = "Groups")
public class Group {
    @Id
    private String id;

    private String subjectId;
    private int number;
    private int capacity;
    private boolean active;
    private Teacher teacher;
    private ArrayList<Student> groupStudents;
    private ArrayList<ClassSession> sessions = new ArrayList<>();

    public Group(String id, int number, int capacity, boolean active, Teacher teacher) {
        this.id = id;
        this.number = number;
        this.capacity = capacity;
        this.active = active;
        this.teacher = teacher;
        this.groupStudents = new ArrayList<>();
        this.sessions = new ArrayList<>();
    }


    public void addSession(ClassSession session) {
        this.sessions.add(session);
    }

    public String getId() {
        return id;
    }
}
