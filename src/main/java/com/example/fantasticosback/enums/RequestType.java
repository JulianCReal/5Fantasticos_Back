package com.example.fantasticosback.enums;

public enum RequestType {
    CHANGE_GROUP ("Change Group"),
    JOIN_GROUP ("Join Group"),
    LEAVE_GROUP ("Leave Group"),
    SPECIAL ("Exceptional Request");

    private final String description;


    RequestType(String description) {
        this.description = description;
    }
    public String getDescription() {
        return description;
    }
    @Override
    public String toString() {
        return description;
    }
}
