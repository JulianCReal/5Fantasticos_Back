package com.example.fantasticosback.model.Validators.RequestValidators;

import com.example.fantasticosback.exception.BusinessValidationException;
import com.example.fantasticosback.model.Document.Enrollment;
import com.example.fantasticosback.model.Document.Group;
import com.example.fantasticosback.model.Document.Request;
import com.example.fantasticosback.model.Document.Student;
import com.example.fantasticosback.util.ClassSession;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.List;

@Component
public class ChangeGroupValidator extends RequestValidator {
    
    private static final DateTimeFormatter TIME_FORMATTER = new DateTimeFormatterBuilder()
            .appendPattern("[H:mm][HH:mm]")
            .toFormatter();
    
    @Override
    public boolean handle(Request request) {
        if(!verifyGroupChange(request)){
            throw new BusinessValidationException("Validation failed for changing group request. One of the groups does not exist.");
        } else if (verifyDestinationGroupCapacity(request) || verifyClashWithOtherSubjects(request)) {
            throw new BusinessValidationException("Validation failed for changing group request.");
        }
        return true;
    }

    private boolean verifyGroupChange(Request request) {
        if (request == null || request.getSourceGroup() == null || request.getDestinationGroup() == null) {
            return false;
        }
        if (request.getSourceGroup().getId() == null || request.getDestinationGroup().getId() == null) {
            return false;
        }
        Group currentGroup = groupService.getGroupById(request.getSourceGroup().getId());
        Group newGroup = groupService.getGroupById(request.getDestinationGroup().getId());
        return (currentGroup != null && newGroup != null);
    }
    private boolean verifyClashWithOtherSubjects(Request request) {
        if (request == null || request.getDestinationGroup() == null || request.getUserId() == null) {
            return true;
        }
        Group newGroup = groupService.getGroupById(request.getDestinationGroup().getId());
        Student student = studentService.findById(request.getUserId());
        if (newGroup == null || student == null) {
            return true; // fallo seguro si no se puede validar
        }

        List<ClassSession> newGroupSessions = newGroup.getSessions();
        List<ClassSession> studentSessions = getStudentEnrolledSessions(student);

        if (newGroupSessions == null || studentSessions.isEmpty()) {
            return false;
        }

        for (ClassSession newSession : newGroupSessions) {
            for (ClassSession existingSession : studentSessions) {
                if (sessionsConflict(newSession, existingSession)) {
                    return true;
                }
            }
        }
        return false;
    }

    private List<ClassSession> getStudentEnrolledSessions(Student student){
        List<ClassSession> sessions = new ArrayList<>();
        if (student == null || student.getStudentId() == null) {
            return sessions;
        }
        List<Enrollment> enrollments = enrollmentService.getEnrollmentsByStudentId(student.getStudentId());
        if (enrollments == null) {
            return sessions;
        }
        for (Enrollment e : enrollments) {
            if (!"ACTIVE".equalsIgnoreCase(e.getStatus())) {
                continue;
            }
            Group g = groupService.getGroupById(e.getGroupId());
            if (g == null || g.getSessions() == null) {
                continue;
            }
            sessions.addAll(g.getSessions());
        }
        return sessions;
    }
    private boolean sessionsConflict(ClassSession session1, ClassSession session2) {
        if (!session1.getDay().equals(session2.getDay())) {
            return false;
        }
        LocalTime start1 = LocalTime.parse(session1.getStartTime(), TIME_FORMATTER);
        LocalTime end1 = LocalTime.parse(session1.getEndTime(), TIME_FORMATTER);
        LocalTime start2 = LocalTime.parse(session2.getStartTime(), TIME_FORMATTER);
        LocalTime end2 = LocalTime.parse(session2.getEndTime(), TIME_FORMATTER);

        return start1.isBefore(end2) && start2.isBefore(end1);
    }
}