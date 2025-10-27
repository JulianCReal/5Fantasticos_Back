package com.example.fantasticosback.util;

import com.example.fantasticosback.model.Document.Career;
import com.example.fantasticosback.model.Document.Enrollment;
import com.example.fantasticosback.model.Document.Semester;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "academic_traffic_lights")
public class AcademicTrafficLight {

    @Id
    private String id;

    private String studentId;

    private int progressPercentage;
    private ArrayList<Enrollment> subjects = new ArrayList<>();
    private double cumulativeAverage;
    private int approvedCredits;
    private ArrayList<Semester> semesters = new ArrayList<>();
    private Career career;
}
