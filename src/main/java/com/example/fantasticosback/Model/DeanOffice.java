package com.example.fantasticosback.Model;

import com.example.fantasticosback.Model.Observers.RequestObserver;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.logging.Logger;

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

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getFaculty() {
        return faculty;
    }

    public ArrayList<Student> getStudents() {
        return students;
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
        switch (request.getType()) {
            case "group":
                return evaluateChange(student, request, "group");
            case "subject":
                return evaluateChange(student, request, "subject");
            default:
                log.warning("Unrecognized request type: " + request.getType());
                return false;
        }
    }

    private boolean evaluateChange(Student student, Request request, String type) {
        Group source = request.getSourceGroup();
        Group destination = request.getDestinationGroup();

        if (!destination.isActive()) {
            log.warning("Request rejected: Destination group " + destination.getNumber() + " is not active");
            return false;
        }
        if (destination.getCapacity() <= 0) {
            log.warning("Request rejected: Destination group " + destination.getNumber() + " has no available slots");
            return false;
        }
        if (hasScheduleConflict(student, destination, source)) {
            log.warning("Request rejected: Schedule conflict with destination group " + destination.getNumber());
            return false;
        }

        log.info("Change request for " + type + " approved: from " + source.getNumber() + " to " + destination.getNumber());
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
