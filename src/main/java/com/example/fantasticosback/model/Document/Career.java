package com.example.fantasticosback.model.Document;

import lombok.Getter;

import java.util.ArrayList;

@Getter
public class Career {
    private String id;
    private String name;
    private ArrayList<Subject> subjects;
    private int totalCredits;

    public Career(String name, int totalCredits) {
        this.name = name;
        this.subjects = new ArrayList<>();
        this.totalCredits = totalCredits;
    }

    private void addSubjects() {
    }

    public ArrayList<Subject> getSubjects() {
        return subjects;
    }

    public int getTotalCredits() {
        return totalCredits;
    }
}