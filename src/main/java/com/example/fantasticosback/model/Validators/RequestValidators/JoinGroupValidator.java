package com.example.fantasticosback.model.Validators.RequestValidators;

import com.example.fantasticosback.exception.BusinessValidationException;
import com.example.fantasticosback.model.Document.Group;
import com.example.fantasticosback.model.Document.Request;
import com.example.fantasticosback.model.Document.Student;
import com.example.fantasticosback.model.Document.Subject;
import com.example.fantasticosback.util.ClassRequirement;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

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
        if (request == null || request.getDestinationGroup() == null || request.getDestinationGroup().getId() == null) {
            return false;
        }
        Group group = groupService.getGroupById(request.getDestinationGroup().getId());
        return group != null;
    }
    private boolean verifyClassRequirments(Request request) {
        if (request == null || request.getDestinationGroup() == null || request.getUserId() == null) {
            return false;
        }
        Group group = groupService.getGroupById(request.getDestinationGroup().getId());
        if (group == null || group.getSubjectId() == null) {
            return false;
        }
        Subject subject = subjectService.findById(group.getSubjectId());
        if (subject == null) {
            return false;
        }
        Student student = studentService.findById(request.getUserId());
        if (student == null) {
            return false;
        }

        List<String> approved = student.getApprovedSubjectIds();
        if (approved == null) {
            return false;
        }

        List<ClassRequirement> requirements = subject.getRequirements();
        if (requirements == null || requirements.isEmpty()) {
            // Si la materia no tiene requisitos, la validación pasa
            return true;
        }

        List<String> reqIds = requirements.stream()
            .map(ClassRequirement::getSubjectId)
            .filter(Objects::nonNull)
            .toList();

        // mejorar performance convirtiendo approved a Set para containsAll
        Set<String> approvedSet = new HashSet<>(approved);
        return approvedSet.containsAll(reqIds);
    }
}
