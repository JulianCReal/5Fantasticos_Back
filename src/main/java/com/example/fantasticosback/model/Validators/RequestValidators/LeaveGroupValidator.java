package com.example.fantasticosback.model.Validators.RequestValidators;

import com.example.fantasticosback.exception.BusinessValidationException;
import com.example.fantasticosback.model.Document.Enrollment;
import com.example.fantasticosback.model.Document.Group;
import com.example.fantasticosback.model.Document.Request;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class LeaveGroupValidator extends RequestValidator {
    @Override
    public boolean handle(Request request) {
        if(!verifyGroupExists(request)) {
            throw new BusinessValidationException("Validation failed for leaving group request. The group does not exist.");
        }else if(!verifyInscription(request)) {
            throw new BusinessValidationException("Validation failed for leaving group request.");
        }
        return true;
    }

    private boolean verifyGroupExists(Request request) {
        Group group = groupService.getGroupById(request.getSourceGroup().getId());
        return group != null;
    }
    private boolean verifyInscription(Request request) {
        Group group = groupService.getGroupById(request.getSourceGroup().getId());
        List<Enrollment> enrollments = enrollmentService.getEnrollmentsByGroupId(group.getId());
        return enrollments.stream().anyMatch(e -> e.getStudentId().equals(request.getUserId()));
    }
}
