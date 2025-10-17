package com.example.fantasticosback.model.Document;

import com.example.fantasticosback.util.AcademicTrafficLight;
import com.example.fantasticosback.util.ClassRequirement;
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
    private ArrayList<ClassRequirement> approvedSubjects;
    private ArrayList<Group> enrolledGroups;

    private ArrayList<Semester> semesters = new ArrayList<>();
    private static final Logger log = Logger.getLogger(Student.class.getName());
    private ArrayList<Request> requests = new ArrayList<>();
    private AcademicTrafficLight academicTrafficLight;

    public Student(String name, String lastName, String document, String career, String code, String studentId, int semester, AcademicTrafficLight academicTrafficLight) {
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


    @Override
    public void showInformation() {
        log.info(() -> "Student: " + name + " " + lastName + ", " +
                "Document: " + document + ", " +
                "Career: " + career + ", " +
                "Semesters: " + semesters.size());
    }
}
