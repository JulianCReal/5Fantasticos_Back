package com.example.fantasticosback.Dtos;

public class TeacherDTO extends BaseDTO {
    private String name;
    private String lastName;
    private int document;
    private String department;

    public TeacherDTO(String id, String name, String lastName, int document, String department) {
        super(id);
        this.name = name;
        this.lastName = lastName;
        this.document = document;
        this.department = department;
    }

    // Getters & Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public int getDocument() { return document; }
    public void setDocument(int document) { this.document = document; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
}
