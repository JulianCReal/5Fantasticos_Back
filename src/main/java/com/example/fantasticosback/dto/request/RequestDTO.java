package com.example.fantasticosback.dto.request;

import com.example.fantasticosback.enums.RequestType;
import lombok.Data;

import java.util.HashMap;

@Data
public class RequestDTO {
    private String userId;
    private RequestType requestType;
    private HashMap<String, Object> details;
    private String observations;
    private String deanOffice;
}
