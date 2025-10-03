package com.example.fantasticosback.Dtos;

public class StudentDTO extends BaseDTO {
    private String name;
    private String career;
    private int semester;

    public StudentDTO(String id, String name, String career, int semester) {
        super(id);
        this.name = name;
        this.career = career;
        this.semester = semester;
    }

    // Getters y Setters
    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getCareer() { return career; }

    public void setCareer(String career) { this.career = career; }

    public int getSemester() { return semester; }

    public void setSemester(int semester) { this.semester = semester; }
}
