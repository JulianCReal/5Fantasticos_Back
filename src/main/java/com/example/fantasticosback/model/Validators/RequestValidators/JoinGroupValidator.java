package com.example.fantasticosback.model.Validators.RequestValidators;

import com.example.fantasticosback.model.Document.Group;
import com.example.fantasticosback.model.Document.Request;
import org.springframework.stereotype.Component;

@Component
public class JoinGroupValidator extends RequestValidator {


    @Override
    public boolean handle(Request request) {
        return false;
    }

    private boolean verifyGroupExists(Request request) {
        Group group = groupService.getGroupById(request.getDestinationGroup().getId());
        return group == null;
    }
}
