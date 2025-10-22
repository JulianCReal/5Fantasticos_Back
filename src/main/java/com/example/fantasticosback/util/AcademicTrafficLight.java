package com.example.fantasticosback.util;
import com.example.fantasticosback.model.Document.Career;
import com.example.fantasticosback.model.Document.Enrollment;
import com.example.fantasticosback.model.Document.Subject;
import com.example.fantasticosback.model.Document.Semester;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AcademicTrafficLight {
    private int id;
    private int progressPercentage;
    private ArrayList<Enrollment> subjects = new ArrayList<>();
    private double cumulativeAverage;
    private int approvedCredits;
    private ArrayList<Semester> semesters = new ArrayList<>();
    private Career career;


}
