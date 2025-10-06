package com.example.fantasticosback.Model;

import com.example.fantasticosback.util.ClassSession;

import java.util.ArrayList;

public class Group {
    private int id;
    private int number;
    private int capacity;
    private boolean active;
    private Subject subject;
    private Teacher teacher;
    private ArrayList<Student> groupStudents;
    private ArrayList<ClassSession> sessions = new ArrayList<>();

    public Group(int id, int number, int capacity, boolean active, Subject subject, Teacher teacher) {
        this.id = id;
        this.number = number;
        this.capacity = capacity;
        this.active = active;
        this.subject = subject;
        this.teacher = teacher;
        this.groupStudents = new ArrayList<>();
        this.sessions = new ArrayList<>();
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumber() {
        return number;
    }

    public int getCapacity() {
        return capacity;
    }

    public boolean isActive() {
        return active;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public ArrayList<Student> getGroupStudents() {
        return groupStudents;
    }

    public void setGroupStudents(ArrayList<Student> groupStudents) {
        this.groupStudents = groupStudents;
    }

    public ArrayList<ClassSession> getSessions() {
        return sessions;
    }

    public void setSessions(ArrayList<ClassSession> sessions) {
        this.sessions = sessions;
    }

    public void addSession(ClassSession session) {
        this.sessions.add(session);
    }
}
