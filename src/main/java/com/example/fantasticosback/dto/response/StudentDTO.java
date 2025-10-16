package com.example.fantasticosback.dto.response;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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
}
