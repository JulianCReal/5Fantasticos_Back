package com.example.fantasticosback.model.Validators.RequestValidators;

import com.example.fantasticosback.exception.BusinessValidationException;
import com.example.fantasticosback.model.Document.Group;
import com.example.fantasticosback.model.Document.Request;
import com.example.fantasticosback.model.Document.Student;
import com.example.fantasticosback.util.ClassSession;

import java.time.LocalTime;
import java.util.ArrayList;

public class ChangeGroupValidator extends RequestValidator {
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
        Group currentGroup = groupService.getGroupById(request.getSourceGroup().getId());
        Group newGroup = groupService.getGroupById(request.getDestinationGroup().getId());
        return (currentGroup != null && newGroup != null);
    }
    private boolean verifyClashWithOtherSubjects(Request request) {
        Group newGroup = groupService.getGroupById(request.getDestinationGroup().getId());
        Student student = studentService.findById(request.getUserId());
        ArrayList<ClassSession> newGroupSessions = newGroup.getSessions();
        ArrayList<ClassSession> studentSessions = getStudentEnrolledSessions(student);

        for (ClassSession newSession : newGroupSessions) {
            for (ClassSession existingSession : studentSessions) {
                if (sessionsConflict(newSession, existingSession)) {
                    return true;
                }
            }
        }
        return false;
    }

    private ArrayList<ClassSession> getStudentEnrolledSessions(Student student){
        ArrayList<ClassSession> sessions = new ArrayList<>();
        student.getEnrolledGroups().forEach(g -> sessions.addAll(g.getSessions()));
        return sessions;
    }
    private boolean sessionsConflict(ClassSession session1, ClassSession session2) {
        if (!session1.getDay().equals(session2.getDay())) {
            return false;
        }
        LocalTime start1 = LocalTime.parse(session1.getStartTime());
        LocalTime end1 = LocalTime.parse(session1.getEndTime());
        LocalTime start2 = LocalTime.parse(session2.getStartTime());
        LocalTime end2 = LocalTime.parse(session2.getEndTime());

        return start1.isBefore(end2) && start2.isBefore(end1);
    }
}
