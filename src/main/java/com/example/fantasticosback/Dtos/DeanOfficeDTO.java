package com.example.fantasticosback.Dtos;

import com.example.fantasticosback.Model.Estudiante;

import java.util.ArrayList;

public class DeanOfficeDTO extends BaseDTO {
    private String faculty;
    private ArrayList<Estudiante> students;

    public DeanOfficeDTO(String id, String faculty, ArrayList<Estudiante> students) {
        super(id);
        this.faculty = faculty;
        this.students = students;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public ArrayList<Estudiante> getStudents() {
        return students;
    }

    public void setStudents(ArrayList<Estudiante> students) {
        this.students = students;
    }
}
