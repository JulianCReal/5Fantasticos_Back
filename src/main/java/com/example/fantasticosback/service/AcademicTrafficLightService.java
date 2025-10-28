package com.example.fantasticosback.service;

import com.example.fantasticosback.util.AcademicTrafficLight;
import com.example.fantasticosback.repository.AcademicTrafficLightRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AcademicTrafficLightService {

    private final AcademicTrafficLightRepository academicTrafficLightRepository;

    public AcademicTrafficLight findByStudentId(String studentId) {
        return academicTrafficLightRepository.findByStudentId(studentId)
                .orElseThrow(() -> new RuntimeException("Semáforo académico no encontrado para el estudiante: " + studentId));
    }

    public List<AcademicTrafficLight> findAll() {
        return academicTrafficLightRepository.findAll();
    }

    public AcademicTrafficLight save(AcademicTrafficLight academicTrafficLight) {
        academicTrafficLight.setLastUpdated(LocalDateTime.now());
        return academicTrafficLightRepository.save(academicTrafficLight);
    }

    public AcademicTrafficLight update(String id, AcademicTrafficLight academicTrafficLight) {
        AcademicTrafficLight existing = academicTrafficLightRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Semáforo académico no encontrado con ID: " + id));

        // Actualizar campos
        existing.setProgressPercentage(academicTrafficLight.getProgressPercentage());
        existing.setCumulativeAverage(academicTrafficLight.getCumulativeAverage());
        existing.setApprovedCredits(academicTrafficLight.getApprovedCredits());
        existing.setTotalCreditsAttempted(academicTrafficLight.getTotalCreditsAttempted());
        existing.setOverallStatus(academicTrafficLight.getOverallStatus());
        existing.setEnrollments(academicTrafficLight.getEnrollments());
        existing.setSemesters(academicTrafficLight.getSemesters());
        existing.setCareer(academicTrafficLight.getCareer());
        existing.updateLastUpdated();

        return academicTrafficLightRepository.save(existing);
    }

    public void delete(String id) {
        academicTrafficLightRepository.deleteById(id);
    }

    public AcademicTrafficLight recalculateAcademicStatus(String studentId) {
        AcademicTrafficLight trafficLight = findByStudentId(studentId);

        // Lógica para recalcular el estado académico
        double progress = calculateProgress(trafficLight);
        AcademicTrafficLight.AcademicStatus status = determineAcademicStatus(progress, trafficLight.getCumulativeAverage());

        trafficLight.setProgressPercentage((int) progress);
        trafficLight.setOverallStatus(status);
        trafficLight.updateLastUpdated();

        return academicTrafficLightRepository.save(trafficLight);
    }

    private double calculateProgress(AcademicTrafficLight trafficLight) {
        // Lógica para calcular progreso basado en créditos
        // Esto debería venir de la carrera o tener un valor por defecto
        int totalCreditsForDegree = 180; // Ejemplo
        return (double) trafficLight.getApprovedCredits() / totalCreditsForDegree * 100;
    }

    private AcademicTrafficLight.AcademicStatus determineAcademicStatus(double progress, double average) {
        if (progress >= 70 && average >= 3.5) {
            return AcademicTrafficLight.AcademicStatus.ON_TRACK;
        } else if (progress >= 50 && average >= 3.0) {
            return AcademicTrafficLight.AcademicStatus.AT_RISK;
        } else {
            return AcademicTrafficLight.AcademicStatus.DELAYED;
        }
    }
}