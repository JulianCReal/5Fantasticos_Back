package com.example.fantasticosback.util;

import com.example.fantasticosback.model.Document.Career;
import com.example.fantasticosback.model.Document.Enrollment;
import com.example.fantasticosback.model.Document.Semester;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "academic_traffic_lights")
public class AcademicTrafficLight {

    @Id
    private String id;

    @Field("student_id")
    private String studentId;

    @Field("progress_percentage")
    private int progressPercentage;

    @Field("cumulative_average")
    private double cumulativeAverage;

    @Field("approved_credits")
    private int approvedCredits;

    @Field("total_credits_attempted")
    private int totalCreditsAttempted;

    @Field("overall_status")
    private AcademicStatus overallStatus;

    // Cambiar nombre a enrollments para coincidir con el service
    @DocumentReference
    private List<Enrollment> enrollments = new ArrayList<>();

    @DocumentReference
    private List<Semester> semesters = new ArrayList<>();

    @DocumentReference
    private Career career;

    @Field("last_updated")
    private LocalDateTime lastUpdated;

    // Enum para los estados académicos
    public enum AcademicStatus {
        ON_TRACK,
        AT_RISK,
        DELAYED
    }

    public AcademicTrafficLight(String studentId, int progressPercentage,
                                double cumulativeAverage, int approvedCredits,
                                int totalCreditsAttempted, AcademicStatus overallStatus) {
        this.studentId = studentId;
        this.progressPercentage = progressPercentage;
        this.cumulativeAverage = cumulativeAverage;
        this.approvedCredits = approvedCredits;
        this.totalCreditsAttempted = totalCreditsAttempted;
        this.overallStatus = overallStatus;
        this.lastUpdated = LocalDateTime.now();
    }

    // Método para actualizar la fecha de modificación
    public void updateLastUpdated() {
        this.lastUpdated = LocalDateTime.now();
    }
}