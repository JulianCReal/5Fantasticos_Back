package com.example.fantasticosback.dto.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TeacherDTO {

    private String id;
    private String name;
    private String lastName;
    private int document;
    private String department;

    public TeacherDTO() {}

    public TeacherDTO(String id, String name, String lastName, int document, String department) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.document = document;
        this.department = department;
    }
}

