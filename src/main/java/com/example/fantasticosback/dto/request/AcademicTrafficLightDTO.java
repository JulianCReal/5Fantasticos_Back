package com.example.fantasticosback.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AcademicTrafficLightDTO {
    private String studentId;
    private int progressPercentage;
    private double cumulativeAverage;
    private int approvedCredits;
    private int totalCredits;
    private String statusColor;
}
