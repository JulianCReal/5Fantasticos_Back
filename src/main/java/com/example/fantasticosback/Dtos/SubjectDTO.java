package com.example.fantasticosback.Dtos;


import com.example.fantasticosback.Model.Group;

import java.util.ArrayList;

public class SubjectDTO extends BaseDTO {
    private String name;
    private int credits;
    private int semester;
    private ArrayList<Group> availableGroups;

    public SubjectDTO(String id, String name, int credits, int semester, ArrayList<Group> availableGroups) {
        super(id);
        this.name = name;
        this.credits = credits;
        this.semester = semester;
        this.availableGroups = availableGroups;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getCredits() { return credits; }
    public void setCredits(int credits) { this.credits = credits; }

    public int getSemester() { return semester; }
    public void setSemester(int semester) { this.semester = semester; }

    public ArrayList<Group> getAvailableGroups() { return availableGroups; }
    public void setAvailableGroups(ArrayList<Group> availableGroups) { this.availableGroups = availableGroups; }
}
