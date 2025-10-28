package com.example.fantasticosback.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum RequestType {
    CHANGE_GROUP ("Change Group"),
    JOIN_GROUP ("Join Group"),
    LEAVE_GROUP ("Leave Group"),
    SPECIAL ("Exceptional Request");

    private final String description;

    RequestType(String description) {
        this.description = description;
    }
    
    @JsonValue
    public String getDescription() {
        return description;
    }
    
    @JsonCreator
    public static RequestType fromString(String value) {
        for (RequestType type : RequestType.values()) {
            if (type.description.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown RequestType: " + value);
    }
    
    @Override
    public String toString() {
        return description;
    }
}
