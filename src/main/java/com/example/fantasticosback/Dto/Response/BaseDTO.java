package com.example.fantasticosback.Dto.Response;

public abstract class BaseDTO {
    private String id;

    public BaseDTO(String id) {
        this.id = id;
    }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }
}
