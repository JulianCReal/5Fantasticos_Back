package com.example.fantasticosback.mapper;

import com.example.fantasticosback.dto.request.RequestAddDataDTO;
import com.example.fantasticosback.dto.request.RequestResponseDTO;
import com.example.fantasticosback.model.Document.Request;
import org.springframework.stereotype.Component;

@Component
public class RequestResponseMapper {
    public RequestResponseDTO convertToResponseDTO(Request request){
        RequestResponseDTO responseDTO = new RequestResponseDTO();
        responseDTO.setRequestId(request.getId());
        responseDTO.setUserId(request.getUserId());
        responseDTO.setRequestType(request.getType());
        responseDTO.setHistoryResponses(request.getHistoryResponses());
        responseDTO.setRequestStateName(request.getStateName());
        responseDTO.setDeanOffice(request.getDeanOffice());
        responseDTO.setObservations(request.getObservations());
        return responseDTO;
    }
    public RequestAddDataDTO convertToAddDataDTO(Request request){
        RequestAddDataDTO addDataDTO = new RequestAddDataDTO();
        addDataDTO.setRequestId(request.getId());
        addDataDTO.setUserId(request.getUserId());
        addDataDTO.setRequestType(request.getType());
        addDataDTO.setHistoryResponses(request.getHistoryResponses());
        addDataDTO.setRequestStateName(request.getStateName());
        addDataDTO.setDeanOffice(request.getDeanOffice());
        addDataDTO.setObservations(request.getObservations());
        addDataDTO.setAdditionalData(request.getAditionalData());
        return addDataDTO;
    }
}
