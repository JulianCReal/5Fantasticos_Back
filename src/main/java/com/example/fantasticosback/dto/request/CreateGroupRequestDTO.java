package com.example.fantasticosback.dto.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateGroupRequestDTO {
    private String id;
    private String subjectId;
    private int number;
    private int capacity;
    private boolean active;
    private String teacherId;

    public CreateGroupRequestDTO() {}

    public CreateGroupRequestDTO(String subjectId, int number, int capacity, boolean active, String teacherId) {
        this.subjectId = subjectId;
        this.number = number;
        this.capacity = capacity;
        this.active = active;
        this.teacherId = teacherId;
    }


}

