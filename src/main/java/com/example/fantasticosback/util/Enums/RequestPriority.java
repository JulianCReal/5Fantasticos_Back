package com.example.fantasticosback.util.Enums;

public enum RequestPriority {
    LOW ("Low"),
    MEDIUM ("Medium"),
    HIGH ("High");

    private final String description;
    RequestPriority(String description) {
        this.description = description;
    }
    public String getDescription() {
        return description;
    }
}
