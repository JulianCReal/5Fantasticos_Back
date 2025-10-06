package com.example.fantasticosback.Model;

import com.example.fantasticosback.Model.Observers.RequestObserver;
import com.example.fantasticosback.util.AcademicTrafficLight;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.*;
import java.util.logging.Logger;

@Document(collection = "Students")
public class Student extends Person {

    @Id
    private String studentId;
    private String career;
    private String code;
    private int semester;
    private ArrayList<Semester> semesters = new ArrayList<>();
    private static final Logger log = Logger.getLogger(Student.class.getName());
    private ArrayList<Request> requests = new ArrayList<>();
    private AcademicTrafficLight academicTrafficLight;

    private ArrayList<RequestObserver> observers = new ArrayList<>();

    public Student(String name, String lastName, int document, String career, String code, String studentId, int semester, AcademicTrafficLight academicTrafficLight) {
        super(name, lastName, document);
        this.career = career;
        this.code = code;
        this.studentId = studentId;
        this.semester = semester;
        this.semesters = new ArrayList<>();
        this.requests = new ArrayList<>();
        this.academicTrafficLight = academicTrafficLight;
    }

    // Getters y Setters
    public String getStudentId() {
        return studentId;
    }

    public String getCareer() {
        return career;
    }

    public String getCode() {
        return code;
    }

    public int getSemester() {
        return semester;
    }

    public ArrayList<Semester> getSemesters() {
        return semesters;
    }

    public void setSemesters(ArrayList<Semester> semesters) {
        this.semesters = semesters;
    }

    public void addSemester(Semester semester) {
        this.semesters.add(semester);
    }

    public ArrayList<Request> getRequests() {
        return requests;
    }

    public void setRequests(ArrayList<Request> requests) {
        this.requests = requests;
    }

    public void setId(String _id) {
        this.studentId = _id;
    }

    public boolean verifyScheduleConflict(Group desiredGroup) {
        if (semesters.isEmpty()) {
            return false;
        }

        Semester currentSemester = semesters.get(semesters.size() - 1);
        Enrollment temporaryEnrollment = new Enrollment(desiredGroup, 0, "studying", 0.0);

        for (Enrollment existingEnrollment : currentSemester.getSubjects()) {
            if (temporaryEnrollment.validateConflict(existingEnrollment)) {
                log.warning("Group " + desiredGroup.getNumber() +
                        " conflicts with " + existingEnrollment.getGroup().getSubject().getName());
                return true;
            }
        }
        return false;
    }

    public boolean addSubject(Group chosenGroup) {
        if (!chosenGroup.isActive()) {
            log.warning("Group " + chosenGroup.getNumber() + " is not active.");
            return false;
        }

        if (verifyScheduleConflict(chosenGroup)) {
            log.warning("Cannot enroll: schedule conflict.");
            return false;
        }

        int enrollmentId = (int) (Math.random() * 100000);
        Enrollment newEnrollment = new Enrollment(chosenGroup, enrollmentId, "active", 0.0);

        if (!semesters.isEmpty()) {
            semesters.get(semesters.size() - 1).addSubject(newEnrollment);
            log.info("Successful enrollment in group " + chosenGroup.getNumber() +
                    " of " + chosenGroup.getSubject().getName());
            return true;
        }

        log.warning("No active semester to add the subject.");
        return false;
    }

    public void removeSubject(Enrollment enrollment) {
        if (!semesters.isEmpty()) {
            Semester currentSemester = semesters.get(semesters.size() - 1);
            currentSemester.removeSubject(enrollment);
            log.info("Subject " + enrollment.getGroup().getSubject().getName() + " withdrawn successfully.");
        } else {
            log.warning("No active semester to remove the subject.");
        }
    }

    public void cancelSubject(Enrollment enrollment) {
        if (!semesters.isEmpty()) {
            Semester currentSemester = semesters.get(semesters.size() - 1);
            currentSemester.cancelSubject(enrollment);
            log.info("Subject " + enrollment.getGroup().getSubject().getName() + " cancelled successfully.");
        } else {
            log.warning("No active semester to cancel the subject.");
        }
    }

    public void addObserver(RequestObserver observer){
        observers.add(observer);
    }

    private void alertObserver(Request request){
        for(RequestObserver observer: observers){
            observer.notifyRequest(request);
        }
    }

    public Request createRequest(String type, Enrollment currentSubject, Group destinationGroup, String observations) {
        String requestId = String.valueOf((int) (Math.random() * 100000));
        Date currentDate = new Date();

        Request request = new Request(
                requestId,
                currentSubject.getGroup(),
                destinationGroup,
                type,
                observations,
                currentDate,
                this.studentId
        );

        if ("group".equals(type)) {
            log.info("Group change request created: ID " + requestId +
                    " from group " + currentSubject.getGroup().getNumber() + " to group " + destinationGroup.getNumber());
        } else if ("subject".equals(type)) {
            log.info("Subject change request created: ID " + requestId +
                    " from subject " + currentSubject.getGroup().getSubject().getName() +
                    " to subject " + destinationGroup.getSubject().getName());
        }
        alertObserver(request);
        requests.add(request);
        return request;
    }

    public AcademicTrafficLight getAcademicTrafficLight() {
        return academicTrafficLight;
    }

    @Override
    public void showInformation() {
        log.info(() -> "Student: " + name + " " + lastName + ", " +
                "Document: " + document + ", " +
                "Career: " + career + ", " +
                "Semesters: " + semesters.size());
    }
}
