package com.example.fantasticosback.model.Validators.RequestValidators;

import com.example.fantasticosback.controller.GroupController;
import com.example.fantasticosback.model.Document.Group;
import com.example.fantasticosback.model.Document.Request;
import com.example.fantasticosback.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class RequestValidator {
    @Autowired
    protected GroupService groupService;

    public abstract boolean handle(Request request);

    public boolean response(Request request){
        return handle(request);
    }
}
