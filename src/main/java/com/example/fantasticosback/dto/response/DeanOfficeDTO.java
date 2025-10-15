package com.example.fantasticosback.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class DeanOfficeDTO {
    private String id;
    private String faculty;
    private String deanName;
    private List<String> students;
    private List<String> professors;
    private List<String> subjects;

    public DeanOfficeDTO() {}

    public DeanOfficeDTO(String id, String faculty, String deanName, List<String> students, List<String> professors, List<String> subjects) {
        this.id = id;
        this.faculty = faculty;
        this.deanName = deanName;
        this.students = students;
        this.professors = professors;
        this.subjects=subjects;
    }
}
