package com.example.fantasticosback.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubjectDTO  {
    private String name;
    private int credits;
    private int semester;
    private String code;
    private String deparment;



    // Constructor para crear (sin grupos)
    public SubjectDTO(String code, String name, int credits, int semester, String deparment) {
        this.code = code;
        this.name = name;
        this.credits = credits;
        this.semester = semester;
        this.deparment = deparment;
    }



}
