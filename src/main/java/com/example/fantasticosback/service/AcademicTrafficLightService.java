package com.example.fantasticosback.service;

import com.example.fantasticosback.dto.request.AcademicTrafficLightDTO;
import com.example.fantasticosback.exception.ResourceNotFoundException;
import com.example.fantasticosback.model.Document.*;
import com.example.fantasticosback.repository.*;
import com.example.fantasticosback.util.AcademicTrafficLight;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AcademicTrafficLightService {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private SemesterRepository semesterRepository;

    @Autowired
    private CareerRepository careerRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private StudentRepository studentRepository;

    /**
     * 🔹 Método para el Controller que retorna el DTO
     */
    public AcademicTrafficLightDTO getAcademicTrafficLight(String studentId) {
        AcademicTrafficLight trafficLight = getAcademicTrafficLightByStudent(studentId);
        return convertToDTO(trafficLight);
    }

    /**
     * 🔹 Genera el semáforo académico de un estudiante a partir de sus datos académicos.
     */
    public AcademicTrafficLight getAcademicTrafficLightByStudent(String studentId) {

        // 1️⃣ Obtener todas las matrículas (enrollments) del estudiante
        List<Enrollment> enrollments = enrollmentRepository.findByStudentId(studentId);

        if (enrollments.isEmpty()) {
            throw new ResourceNotFoundException("Enrollment", "studentId", studentId);
        }

        // 2️⃣ Calcular promedio acumulado y créditos aprobados
        double totalWeighted = 0;
        int totalCredits = 0;
        int approvedCredits = 0;
        int totalCreditsAttempted = 0;

        for (Enrollment e : enrollments) {
            // Obtener la materia asociada
            Subject subject = null;
            if (e.getSubject() != null) {
                subject = e.getSubject();
            } else if (e.getSubjectId() != null) {
                subject = subjectRepository.findById(e.getSubjectId()).orElse(null);
            }

            if (subject == null) continue;

            int credits = subject.getCredits();
            totalWeighted += e.getFinalGrade() * credits;
            totalCreditsAttempted += credits;

            if (e.getFinalGrade() >= 3.0) {
                approvedCredits += credits;
            }
        }

        double cumulativeAverage = totalCreditsAttempted > 0 ? totalWeighted / totalCreditsAttempted : 0;

        // 3️⃣ Obtener los semestres cursados
        List<Semester> semesters = semesterRepository.findByStudentId(studentId);

        // 4️⃣ Obtener la carrera del estudiante
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", studentId));

        String careerId = student.getCareer();
        Career career = careerRepository.findById(careerId)
                .orElseThrow(() -> new ResourceNotFoundException("Career", "id", careerId));

        // 5️⃣ Calcular porcentaje de progreso
        int totalCareerCredits = career != null ? career.getTotalCredits() : 0;
        int progressPercentage = totalCareerCredits > 0
                ? (int) ((approvedCredits * 100.0) / totalCareerCredits)
                : 0;

        // 6️⃣ Determinar el estado académico
        AcademicTrafficLight.AcademicStatus status = calculateAcademicStatus(progressPercentage, cumulativeAverage);

        // 7️⃣ Construir el semáforo académico
        AcademicTrafficLight trafficLight = new AcademicTrafficLight();
        trafficLight.setStudentId(studentId);
        trafficLight.setApprovedCredits(approvedCredits);
        trafficLight.setCumulativeAverage(Math.round(cumulativeAverage * 100.0) / 100.0);
        trafficLight.setProgressPercentage(progressPercentage);
        trafficLight.setTotalCreditsAttempted(totalCreditsAttempted);
        trafficLight.setOverallStatus(status);
        trafficLight.setEnrollments(new ArrayList<>(enrollments)); // ✅ CAMBIAR AQUÍ
        trafficLight.setSemesters(new ArrayList<>(semesters));
        trafficLight.setCareer(career);
        trafficLight.updateLastUpdated();

        return trafficLight;
    }

    /**
     * 🔹 Convierte la entidad a DTO
     */
    private AcademicTrafficLightDTO convertToDTO(AcademicTrafficLight trafficLight) {
        AcademicTrafficLightDTO dto = new AcademicTrafficLightDTO();
        dto.setStudentId(trafficLight.getStudentId());
        dto.setProgressPercentage(trafficLight.getProgressPercentage());
        dto.setCumulativeAverage(trafficLight.getCumulativeAverage());
        dto.setApprovedCredits(trafficLight.getApprovedCredits());
        dto.setTotalCredits(trafficLight.getTotalCreditsAttempted());
        dto.setStatusColor(mapStatusToColor(trafficLight.getOverallStatus()));

        return dto;
    }

    /**
     * 🔹 Determina el estado académico basado en progreso y promedio
     */
    private AcademicTrafficLight.AcademicStatus calculateAcademicStatus(int progressPercentage, double cumulativeAverage) {
        if (progressPercentage >= 75 && cumulativeAverage >= 3.5) {
            return AcademicTrafficLight.AcademicStatus.ON_TRACK;
        } else if (progressPercentage >= 50 && cumulativeAverage >= 3.0) {
            return AcademicTrafficLight.AcademicStatus.AT_RISK;
        } else {
            return AcademicTrafficLight.AcademicStatus.DELAYED;
        }
    }

    /**
     * 🔹 Mapea el estado académico a color para el semáforo
     */
    private String mapStatusToColor(AcademicTrafficLight.AcademicStatus status) {
        switch (status) {
            case ON_TRACK:
                return "GREEN";
            case AT_RISK:
                return "YELLOW";
            case DELAYED:
                return "RED";
            default:
                return "RED";
        }
    }
}