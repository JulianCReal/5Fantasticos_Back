package com.example.fantasticosback.model.Validators.RequestValidators;

import com.example.fantasticosback.exception.BusinessValidationException;
import com.example.fantasticosback.model.Document.Group;
import com.example.fantasticosback.model.Document.Request;
import com.example.fantasticosback.model.Document.Student;
import com.example.fantasticosback.model.Document.Subject;
import org.springframework.stereotype.Component;

@Component
public class JoinGroupValidator extends RequestValidator {
    @Override
    public boolean handle(Request request) {
        if(!verifyGroupExists(request)){
            throw new BusinessValidationException("Validation failed for joining group request. The group does not exist.");
        }else if(verifyDestinationGroupCapacity(request) || !verifyClassRequirments(request)){
            throw new BusinessValidationException("Validation failed for joining group request.");
        }
        return true;
    }

    private boolean verifyGroupExists(Request request) {
        Group group = groupService.getGroupById(request.getDestinationGroup().getId());
        return group != null;
    }
    private boolean verifyClassRequirments(Request request) {
        Group group = groupService.getGroupById(request.getDestinationGroup().getId());
        Subject subject = subjectService.findById(group.getSubjectId());
        Student student = studentService.findById(request.getUserId());
        return student.getApprovedSubjects().containsAll(subject.getRequirements());
    }
}
