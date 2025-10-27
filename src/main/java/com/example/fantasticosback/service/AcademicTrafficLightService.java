package com.example.fantasticosback.service;

import com.example.fantasticosback.model.Document.Career;
import com.example.fantasticosback.model.Document.Enrollment;
import com.example.fantasticosback.model.Document.Semester;
import com.example.fantasticosback.repository.CareerRepository;
import com.example.fantasticosback.repository.EnrollmentRepository;
import com.example.fantasticosback.repository.SemesterRepository;
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

    /**
     * 🔹 Genera el semáforo académico de un estudiante a partir de sus datos académicos.
     *
     * @param studentId ID del estudiante
     * @return objeto AcademicTrafficLight con toda la información calculada
     */
    public AcademicTrafficLight getAcademicTrafficLightByStudent(String studentId) {

        // 1️⃣ Obtener todas las matrículas (enrollments) del estudiante
        List<Enrollment> enrollments = enrollmentRepository.findByStudentId(studentId);

        if (enrollments.isEmpty()) {
            throw new RuntimeException("El estudiante no tiene matrículas registradas.");
        }

        // 2️⃣ Calcular promedio acumulado y créditos aprobados
        double totalWeighted = 0;
        int totalCredits = 0;
        int approvedCredits = 0;

        for (Enrollment e : enrollments) {
            if (e.getSubject() == null) continue;

            int credits = e.getSubject().getCredits();
            totalWeighted += e.getFinalGrade() * credits;
            totalCredits += credits;

            if (e.getFinalGrade() >= 3.0) {
                approvedCredits += credits;
            }
        }

        double cumulativeAverage = totalCredits > 0 ? totalWeighted / totalCredits : 0;

        // 3️⃣ Obtener los semestres cursados
        List<Semester> semesters = semesterRepository.findByStudentId(studentId);

        // 4️⃣ Obtener la carrera del estudiante (asumiendo que está en la primera matrícula)
        Career career = enrollments.get(0).getCareer();

        // 5️⃣ Calcular porcentaje de progreso
        int totalCareerCredits = career.getTotalCredits(); // asegúrate que este campo exista
        int progressPercentage = (int) ((approvedCredits * 100.0) / totalCareerCredits);

        // 6️⃣ Construir el semáforo académico
        AcademicTrafficLight trafficLight = new AcademicTrafficLight();
        trafficLight.setId(career.getId()); // opcional
        trafficLight.setApprovedCredits(approvedCredits);
        trafficLight.setCumulativeAverage(Math.round(cumulativeAverage * 100.0) / 100.0);
        trafficLight.setProgressPercentage(progressPercentage);
        trafficLight.setSubjects(new ArrayList<>(enrollments));
        trafficLight.setSemesters(new ArrayList<>(semesters));
        trafficLight.setCareer(career);

        return trafficLight;
    }
}
