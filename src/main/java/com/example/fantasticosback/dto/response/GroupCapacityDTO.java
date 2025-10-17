package com.example.fantasticosback.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para mostrar información de capacidad de grupos por materia
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupCapacityDTO {
    private String subjectCode;
    private String subjectName;
    private int groupNumber;
    private int maxCapacity;
    private int enrolledStudents;
    private double occupancyPercentage;
}
