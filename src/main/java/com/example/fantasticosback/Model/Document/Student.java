package com.example.fantasticosback.Model.Document;

import com.example.fantasticosback.util.AcademicTrafficLight;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.*;
import java.util.logging.Logger;
@Data
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

    public Student(String name, String lastName, int document, String career, String code, String studentId, int semester, AcademicTrafficLight academicTrafficLight) {
        super(name, lastName, document);
        this.career = career;
        this.code = code;
        this.studentId = studentId;
        this.semester = semester;
        this.semesters = new ArrayList<>();
        this.requests = new ArrayList<>();
        this.academicTrafficLight = academicTrafficLight;

        // Inicializar automáticamente los semestres hasta el semestre actual
        initializeSemesters(semester);
    }

    /**
     * Inicializa los semestres desde el primero hasta el semestre especificado
     *
     * @param currentSemester Semestre actual del estudiante
     */
    private void initializeSemesters(int currentSemester) {
        int currentYear = java.time.Year.now().getValue();

        for (int i = 1; i <= currentSemester; i++) {
            boolean isCurrentSemester = (i == currentSemester);
            int academicPeriod = ((i - 1) % 2) + 1;
            int year = currentYear - ((currentSemester - i) / 2);
            Semester newSemester = new Semester(i, year, academicPeriod, isCurrentSemester);
            this.semesters.add(newSemester);
        }
        log.info("Initialized " + currentSemester + " semesters for student " + this.studentId);
    }

    /*public Request createRequest(String type, Enrollment currentSubject, Group destinationGroup, Subject destinationSubject, String observations) {
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
                    " from subject " + currentSubject.getSubject().getName() +
                    " to subject " + destinationSubject.getName());
        }

        requests.add(request);

        // Notify observers about the new request
        notifyObservers(request);

        return request;
    }*/

    @Override
    public void showInformation() {
        log.info(() -> "Student: " + name + " " + lastName + ", " +
                "Document: " + document + ", " +
                "Career: " + career + ", " +
                "Semesters: " + semesters.size());
    }
}
