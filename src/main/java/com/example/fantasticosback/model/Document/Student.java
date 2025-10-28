package com.example.fantasticosback.model.Document;

import com.example.fantasticosback.util.AcademicTrafficLight;
import com.example.fantasticosback.util.ClassRequirement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.*;
import java.util.logging.Logger;
@Data
@Document(collection = "Students")
@AllArgsConstructor
@NoArgsConstructor
public class Student extends Person {

    @Id
    private String studentId;
    private String career;

    private int currentSemester;
    private List<String> approvedSubjectIds;
    private List<String> scheduleIds;
    private List<String> requestIds;
    private AcademicTrafficLight academicTrafficLight;
    private static final Logger log = Logger.getLogger(Student.class.getName());

    @Override
    public void showInformation() {
        log.info(() -> "Student: " + name + " " + lastName + ", " +
                "Document: " + document + ", " +
                "Career: " + career + ", " +
                "Current Semester: " + currentSemester);
    }
}


