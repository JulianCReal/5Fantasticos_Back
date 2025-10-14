package com.example.fantasticosback.Dtos.RequestDTOs;

import com.example.fantasticosback.Model.Document.DeanOffice;
import com.example.fantasticosback.util.Enums.RequestType;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashMap;

@Data
public class RequestResponseDTO {
    private String requestId;
    private String userId;
    private RequestType requestType;
    private String requestStateName;
    private HashMap<LocalDateTime, String> historyResponses;
    private String observations;
    private String deanOffice;

}
