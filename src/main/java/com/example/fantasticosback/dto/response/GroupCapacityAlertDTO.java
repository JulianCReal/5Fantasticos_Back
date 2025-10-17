package com.example.fantasticosback.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO para las alertas de capacidad de grupos
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupCapacityAlertDTO {
    private String alertId;
    private String groupId;
    private String subjectCode;
    private String subjectName;
    private int groupNumber;
    private int currentCapacity;
    private int maxCapacity;
    private double occupancyPercentage;
    private String alertLevel; // WARNING, CRITICAL
    private String message;
    private LocalDateTime timestamp;
    private boolean acknowledged; // Si la alerta ha sido vista/reconocida
}
