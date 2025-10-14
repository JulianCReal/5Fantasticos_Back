package com.example.fantasticosback.Model.RequestCreatrors;

import com.example.fantasticosback.Dtos.RequestDTOs.RequestDTO;
import com.example.fantasticosback.Model.Document.Group;
import com.example.fantasticosback.Model.Document.Request;
import com.example.fantasticosback.util.Enums.RequestType;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class RequestLeaveCreator extends RequestCreator {
    @Override
    public Request createRequest(RequestDTO requestDTO) {
        Request request = new Request();
        request.setRequestId(UUID.randomUUID().toString());
        request.setUserId(requestDTO.getUserId());
        request.setType(requestDTO.getRequestType());
        request.setSourceGroup((Group) requestDTO.getDetails().get("sourceGroup"));
        request.setObservations(requestDTO.getObservations());
        request.setRequestDate(LocalDateTime.now());
        request.setRequestPriority(assignPriority(requestDTO));
        request.setDeanOffice(requestDTO.getDeanOffice());
        request.setCreatorRole(assignCreationRole(requestDTO));
        return request;
    }
    @Override
    public RequestType getRequestType(RequestType requestType) {
        return RequestType.LEAVE_GROUP;
    }
}
