package com.example.fantasticosback.Dtos.RequestDTOs;

import com.example.fantasticosback.Model.Document.DeanOffice;
import com.example.fantasticosback.util.Enums.RequestType;
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
