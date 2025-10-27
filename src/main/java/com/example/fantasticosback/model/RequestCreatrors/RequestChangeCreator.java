package com.example.fantasticosback.model.RequestCreatrors;

import com.example.fantasticosback.dto.request.RequestDTO;
import com.example.fantasticosback.model.Document.Group;
import com.example.fantasticosback.model.Document.Request;
import com.example.fantasticosback.enums.RequestType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class RequestChangeCreator extends RequestCreator {
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public Request createRequest(RequestDTO requestDTO) {
        Request request = new Request();
        request.setId(UUID.randomUUID().toString());
        request.setUserId(requestDTO.getUserId());
        request.setType(requestDTO.getRequestType());

        Object sourceGroupObj = requestDTO.getDetails().get("sourceGroup");
        Object destinationGroupObj = requestDTO.getDetails().get("destinationGroup");

        Group sourceGroup = objectMapper.convertValue(sourceGroupObj, Group.class);
        Group destinationGroup = objectMapper.convertValue(destinationGroupObj, Group.class);

        request.setSourceGroup(sourceGroup);
        request.setDestinationGroup(destinationGroup);
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
    @Override
    public boolean canHandle(RequestDTO requestDTO) {
        return requestDTO.getRequestType() == RequestType.CHANGE_GROUP;
    }
}
