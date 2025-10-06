package com.example.fantasticosback.Dtos;

import com.example.fantasticosback.Model.Student;

import java.util.ArrayList;

public class DeanOfficeDTO extends BaseDTO {
    private String faculty;
    private ArrayList<Student> students;

    public DeanOfficeDTO(String id, String faculty, ArrayList<Student> students) {
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

    public ArrayList<Student> getStudents() {
        return students;
    }

    public void setStudents(ArrayList<Student> students) {
        this.students = students;
    }
}
