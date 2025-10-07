package com.example.fantasticosback.Model;

import com.example.fantasticosback.Model.Observers.RequestObserver;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.logging.Logger;
@Getter
@Setter
@Document(collection = "DeanOffices")
public class DeanOffice {

    @Id
    private String id;
    private String faculty;
    private static final Logger log = Logger.getLogger(DeanOffice.class.getName());
    private ArrayList<Student> students = new ArrayList<>();

    private ArrayList<RequestObserver> observers = new ArrayList<>();

    public DeanOffice(String id, String faculty) {
        this.id = id;
        this.faculty = faculty;
    }

    public void addObserver(RequestObserver observer){
        observers.add(observer);
    }

    private void alertObserver(Request request) {
        for(RequestObserver observer: observers){
            observer.notifyRequest(request);
        }
    }

    public void addStudent(Student student) {
        students.add(student);
    }

    public void manageRequest(Student student, Request request) {
        try {
            request.recoverState();

            if (request.getState().getStateName().equals("Pending")) {
                request.process("DeanOffice");
            }

            boolean approved = evaluate(student, request);
            request.setEvaluationApproved(approved);

            request.process("DeanOffice");
            alertObserver(request);

        } catch (Exception e) {
            log.severe("Error managing request: " + e.getMessage());
            throw new IllegalStateException("Could not process the request.");
        }
    }

    private boolean evaluate(Student student, Request request) {
        return switch (request.getType()) {
            case "group" -> evaluateChange(student, request, "group");
            case "subject" -> evaluateChange(student, request, "subject");
            default -> {
                log.warning("Unrecognized request type: " + request.getType());
                yield false;
            }
        };
    }

    private boolean evaluateChange(Student student, Request request, String type) {
        Group source = request.getSourceGroup();
        Group destination = request.getDestinationGroup();

        if (!destination.isActive()) {
            String message = "Request rejected: Destination group " + destination.getNumber() + " is not active";
            log.warning(message);
            request.setHistoryResponses(message, LocalDateTime.now());
            return false;
        }
        if (destination.getCapacity() <= 0) {
            String message = "Request rejected: Destination group " + destination.getNumber() + " has no available slots";
            request.setHistoryResponses(message, LocalDateTime.now());
            log.warning(message);
            return false;
        }
        if (hasScheduleConflict(student, destination, source)) {
            String message = "Request rejected: Schedule conflict with destination group " + destination.getNumber();
            request.setHistoryResponses(message, LocalDateTime.now());
            log.warning(message);
            return false;
        }

        String message = "Request approved: Change from group " + source.getNumber() + " to " + destination.getNumber();
        request.setHistoryResponses(message, LocalDateTime.now());
        log.info(message);
        return true;
    }

    private boolean hasScheduleConflict(Student student, Group destinationGroup, Group excludeGroup) {
        if (student.getSemesters().isEmpty()) {
            return false;
        }

        Semester currentSemester = student.getSemesters().get(student.getSemesters().size() - 1);
        Enrollment temporaryEnrollment = new Enrollment(destinationGroup, 0, "temporary", 0.0);

        return currentSemester.getSubjects().stream()
                .filter(enrollment -> !enrollment.getGroup().equals(excludeGroup))
                .anyMatch(temporaryEnrollment::validateConflict);
    }

    public ArrayList<Request> getRequestsByFaculty() {
        ArrayList<Request> facultyRequests = new ArrayList<>();
        for (Student student : students) {
            facultyRequests.addAll(student.getRequests());
        }
        return facultyRequests;
    }
}
