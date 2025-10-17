package com.example.fantasticosback.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class DeanOfficeDTO {
    private String id;
    private String faculty;
    private String deanId;
    private List<String> students;
    private List<String> professors;
    private List<String> subjects;
    private List<String> requests;

    public DeanOfficeDTO() {}

    public DeanOfficeDTO(String id, String faculty, String deanId, List<String> students, List<String> professors, List<String> subjects, List<String> requests) {
        this.id = id;
        this.faculty = faculty;
        this.deanId = deanId;
        this.students = students;
        this.professors = professors;
        this.subjects=subjects;
        this.requests=requests;
    }
}