package com.example.fantasticosback.Persistence.Server;

import com.example.fantasticosback.Exception.BusinessValidationException;
import com.example.fantasticosback.Exception.ResourceNotFoundException;
import com.example.fantasticosback.Model.Entities.*;
import com.example.fantasticosback.Persistence.Repository.EnrollmentRepository;
import com.example.fantasticosback.enums.UserRole;
import com.example.fantasticosback.util.ClassSession;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final GroupService groupService;
    private final StudentService studentService;
    private final ScheduleService scheduleService;

    public EnrollmentService(
            EnrollmentRepository enrollmentRepository,
            GroupService groupService,
            StudentService studentService,
            ScheduleService scheduleService) {
        this.enrollmentRepository = enrollmentRepository;
        this.groupService = groupService;
        this.studentService = studentService;
        this.scheduleService = scheduleService;
    }

    public Enrollment enrollStudentInGroup(String studentId, int groupId, String semester) {
        Student student = studentService.findById(studentId);
        Group group = groupService.getGroupById(groupId);

        validateEnrollment(student, group);

        // Verificar si hay conflictos de horario con el horario actual del estudiante
        for (ClassSession newSession : group.getSessions()) {
            try {
                if (scheduleService.hasTimeConflict(studentId, newSession)) {
                    throw new BusinessValidationException("Existe conflicto de horario con otras materias inscritas");
                }
            } catch (IllegalAccessException e) {
                throw new BusinessValidationException("Error al verificar conflictos de horario");
            }
        }

        Enrollment enrollment = new Enrollment(
            generateEnrollmentId(),
            group,
            null,
            "ACTIVE",
            0.0
        );

        // Agregar estudiante al grupo
        groupService.addStudentToGroup(groupId, student);

        return enrollmentRepository.save(enrollment);
    }

    public void cancelEnrollment(String studentId, String enrollmentId) {
        Student student = studentService.findById(studentId);
        Enrollment enrollment = findEnrollmentById(enrollmentId);

        validateCancellation(enrollment);

        enrollment.setStatus("CANCELLED");
        Group group = enrollment.getGroup();
        groupService.removeStudentFromGroup(group.getId(), student);

        enrollmentRepository.save(enrollment);
    }

    private void validateCancellation(Enrollment enrollment) {
        if (!enrollment.getStatus().equals("ACTIVE")) {
            throw new BusinessValidationException("La inscripción ya está cancelada o finalizada");
        }

        if (!isWithinEnrollmentPeriod()) {
            throw new BusinessValidationException("No se pueden cancelar inscripciones fuera del período establecido");
        }
    }

    private void validateEnrollment(Student student, Group group) {
        if (!isWithinEnrollmentPeriod()) {
            throw new BusinessValidationException("Fuera del período de inscripciones");
        }

        if (!group.isActive()) {
            throw new BusinessValidationException("El grupo no está activo para inscripciones");
        }

        if (group.getGroupStudents().size() >= group.getCapacity()) {
            throw new BusinessValidationException("El grupo ha alcanzado su capacidad máxima");
        }

        if (group.getTeacher() == null) {
            throw new BusinessValidationException("El grupo no tiene profesor asignado");
        }

        if (enrollmentRepository.existsByStudentIdAndGroupId(student.getStudentId(), group.getId())) {
            throw new BusinessValidationException("El estudiante ya está inscrito en este grupo");
        }
    }

    public List<Enrollment> getEnrollmentsByStudentId(String studentId) {
        return enrollmentRepository.findByStudentId(studentId);
    }

    public List<Enrollment> getEnrollmentsByGroupId(int groupId) {
        return enrollmentRepository.findByGroupId(groupId);
    }

    public Enrollment updateGrade(String enrollmentId, double grade) {
        validateGrade(grade);

        Enrollment enrollment = findEnrollmentById(enrollmentId);
        enrollment.setFinalGrade(grade);
        return enrollmentRepository.save(enrollment);
    }

    private void validateGrade(double grade) {
        if (grade < 0.0 || grade > 5.0) {
            throw new BusinessValidationException("La calificación debe estar entre 0.0 y 5.0");
        }
    }

    private String generateEnrollmentId() {
        return "ENR-" + System.currentTimeMillis();
    }

    private Enrollment findEnrollmentById(String enrollmentId) {
        return enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment", "id", enrollmentId));
    }

    private boolean isWithinEnrollmentPeriod() {
        return true; // Por ahora retorna true para pruebas
    }
}
