package com.example.fantasticosback.dto.request;

import com.example.fantasticosback.enums.RequestType;
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
