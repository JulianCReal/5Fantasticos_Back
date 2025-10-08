package com.example.fantasticosback.Model.Entities;

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

    // Métodos para consulta de horarios

    /**
     * Obtiene el horario del semestre actual
     * @return Lista de materias inscritas en el semestre actual con sus horarios
     */
    public ArrayList<Enrollment> getCurrentSchedule() {
        if (semesters.isEmpty()) {
            log.info("No hay semestres registrados para el estudiante.");
            return new ArrayList<>();
        }

        Semester currentSemester = semesters.get(semesters.size() - 1);
        log.info("Consultando horario actual - Semestre: " + (semesters.size()));
        return currentSemester.getSubjects();
    }

    /**
     * Obtiene el horario de un semestre específico
     * @param semesterIndex Índice del semestre (0 para el primer semestre)
     * @return Lista de materias del semestre especificado
     */
    public ArrayList<Enrollment> getScheduleBySemester(int semesterIndex) {
        if (semesterIndex < 0 || semesterIndex >= semesters.size()) {
            log.warning("Índice de semestre inválido: " + semesterIndex +
                       ". Semestres disponibles: 0-" + (semesters.size() - 1));
            return new ArrayList<>();
        }

        Semester targetSemester = semesters.get(semesterIndex);
        log.info("Consultando horario del semestre " + (semesterIndex + 1));
        return targetSemester.getSubjects();
    }

    /**
     * Obtiene todos los horarios de semestres anteriores (excluyendo el actual)
     * @return Map con el número de semestre como clave y las materias como valor
     */
    public Map<Integer, ArrayList<Enrollment>> getPreviousSemestersSchedules() {
        Map<Integer, ArrayList<Enrollment>> previousSchedules = new HashMap<>();

        if (semesters.size() <= 1) {
            log.info("No hay semestres anteriores para consultar.");
            return previousSchedules;
        }

        // Obtener todos los semestres excepto el último (actual)
        for (int i = 0; i < semesters.size() - 1; i++) {
            previousSchedules.put(i + 1, semesters.get(i).getSubjects());
        }

        log.info("Consultando horarios de " + (semesters.size() - 1) + " semestres anteriores.");
        return previousSchedules;
    }

    /**
     * Obtiene un resumen completo de todos los horarios del estudiante
     * @return Map con todos los semestres y sus materias
     */
    public Map<Integer, ArrayList<Enrollment>> getAllSchedules() {
        Map<Integer, ArrayList<Enrollment>> allSchedules = new HashMap<>();

        if (semesters.isEmpty()) {
            log.info("No hay semestres registrados.");
            return allSchedules;
        }

        for (int i = 0; i < semesters.size(); i++) {
            allSchedules.put(i + 1, semesters.get(i).getSubjects());
        }

        log.info("Consultando todos los horarios - Total semestres: " + semesters.size());
        return allSchedules;
    }

    /**
     * Verifica si el estudiante tiene materias activas en el semestre actual
     * @return true si tiene materias activas, false en caso contrario
     */
    public boolean hasActiveSchedule() {
        ArrayList<Enrollment> currentSchedule = getCurrentSchedule();
        return !currentSchedule.isEmpty() &&
               currentSchedule.stream().anyMatch(enrollment -> "active".equals(enrollment.getStatus()));
    }

    @Override
    public void showInformation() {
        log.info(() -> "Student: " + name + " " + lastName + ", " +
                "Document: " + document + ", " +
                "Career: " + career + ", " +
                "Semesters: " + semesters.size());
    }
}
