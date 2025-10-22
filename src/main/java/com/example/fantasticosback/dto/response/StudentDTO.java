package com.example.fantasticosback.dto.response;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentDTO extends BaseDTO {
    private String name;
    private String lastName;
    private String document;
    private String career;
    private int semester;

    public StudentDTO(String id, String name, String lastName, String document, String career, int semester) {
        super(id);
        this.name = name;
        this.lastName = lastName;
        this.document = document;
        this.career = career;
        this.semester = semester;
    }
}
