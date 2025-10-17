package com.example.fantasticosback.model.Validators.RequestValidators;

import com.example.fantasticosback.controller.GroupController;
import com.example.fantasticosback.model.Document.Group;
import com.example.fantasticosback.model.Document.Request;
import com.example.fantasticosback.service.EnrollmentService;
import com.example.fantasticosback.service.GroupService;
import com.example.fantasticosback.service.StudentService;
import com.example.fantasticosback.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class RequestValidator {
    @Autowired
    protected GroupService groupService;
    @Autowired
    protected SubjectService subjectService;
    @Autowired
    protected StudentService studentService;
    @Autowired
    protected EnrollmentService enrollmentService;

    public abstract boolean handle(Request request);

    public boolean response(Request request){
        return handle(request);
    }

    public boolean verifyDestinationGroupCapacity(Request request) {
        Group group = groupService.getGroupById(request.getDestinationGroup().getId());
        return group.getGroupStudents().size() >= group.getCapacity();
    }

}
