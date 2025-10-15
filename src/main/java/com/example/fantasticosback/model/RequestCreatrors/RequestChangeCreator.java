package com.example.fantasticosback.model.RequestCreatrors;

import com.example.fantasticosback.dto.request.RequestDTO;
import com.example.fantasticosback.model.Document.Group;
import com.example.fantasticosback.model.Document.Request;
import com.example.fantasticosback.enums.RequestType;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class RequestChangeCreator extends RequestCreator {
    @Override
    public Request createRequest(RequestDTO requestDTO) {
        Request request = new Request();
        request.setId(UUID.randomUUID().toString());
        request.setUserId(requestDTO.getUserId());
        request.setType(requestDTO.getRequestType());
        request.setSourceGroup((Group) requestDTO.getDetails().get("sourceGroup"));
        request.setDestinationGroup((Group) requestDTO.getDetails().get("destinationGroup"));
        request.setObservations(requestDTO.getObservations());
        request.setRequestDate(LocalDateTime.now());
        request.setRequestPriority(assignPriority(requestDTO));
        request.setDeanOffice(requestDTO.getDeanOffice());
        request.setCreatorRole(assignCreationRole(requestDTO));
        return request;
    }
    @Override
    public RequestType getRequestType(RequestType requestType) {
        return RequestType.CHANGE_GROUP;
    }
}
