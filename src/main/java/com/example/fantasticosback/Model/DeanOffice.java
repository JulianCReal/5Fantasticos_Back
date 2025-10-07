package com.example.fantasticosback.Model;

import com.example.fantasticosback.Model.Observers.RequestObserver;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import com.example.fantasticosback.util.SubjectCatalog;

import java.util.ArrayList;
import java.util.Map;
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

        // Necesitamos encontrar la materia correspondiente al grupo de destino
        Subject destinationSubject = findSubjectByGroup(destinationGroup);
        if (destinationSubject == null) {
            log.warning("Could not find subject for destination group " + destinationGroup.getNumber());
            return true; // Asumimos conflicto si no encontramos la materia
        }

        Enrollment temporaryEnrollment = new Enrollment(destinationGroup, destinationSubject, 0, "temporary", 0.0);

        return currentSemester.getSubjects().stream()
                .filter(enrollment -> !enrollment.getGroup().equals(excludeGroup))
                .anyMatch(temporaryEnrollment::validateConflict);
    }

    /**
     * Método auxiliar para encontrar la materia que contiene un grupo específico
     */
    private Subject findSubjectByGroup(Group targetGroup) {
        // Buscar en el catálogo estático de materias usando getSubjects()
        Map<String, Subject> subjects = SubjectCatalog.getSubjects();

        for (Map.Entry<String, Subject> entry : subjects.entrySet()) {
            Subject catalogSubject = entry.getValue();
            if (catalogSubject != null && catalogSubject.getAvailableGroups() != null) {
                // Verificar si este grupo pertenece a esta materia comparando por ID y número
                for (Group group : catalogSubject.getAvailableGroups()) {
                    if (group.getId() == targetGroup.getId() &&
                        group.getNumber() == targetGroup.getNumber()) {
                        return catalogSubject;
                    }
                }
            }
        }

        log.warning("Subject not found for group ID: " + targetGroup.getId() + ", number: " + targetGroup.getNumber());
        return null;
    }

    public ArrayList<Request> getRequestsByFaculty() {
        ArrayList<Request> facultyRequests = new ArrayList<>();
        for (Student student : students) {
            facultyRequests.addAll(student.getRequests());
        }
        return facultyRequests;
    }
}
