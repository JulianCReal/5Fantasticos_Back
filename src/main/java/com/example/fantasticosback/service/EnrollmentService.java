package com.example.fantasticosback.service;

import com.example.fantasticosback.exception.BusinessValidationException;
import com.example.fantasticosback.exception.ResourceNotFoundException;
import com.example.fantasticosback.model.Document.*;
import com.example.fantasticosback.repository.EnrollmentRepository;
import com.example.fantasticosback.repository.SubjectRepository;
import com.example.fantasticosback.util.ClassSession;
import com.example.fantasticosback.util.SubjectCatalog;
import java.util.logging.Logger;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final GroupService groupService;
    private final StudentService studentService;
    private final ScheduleService scheduleService;
    private final SubjectRepository subjectRepository;
    private static final Logger log = Logger.getLogger(EnrollmentService.class.getName());

    public EnrollmentService(
            EnrollmentRepository enrollmentRepository,
            GroupService groupService,
            StudentService studentService,
            ScheduleService scheduleService,
            SubjectRepository subjectRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.groupService = groupService;
        this.studentService = studentService;
        this.scheduleService = scheduleService;
        this.subjectRepository = subjectRepository;
    }

    public Enrollment enrollStudentInGroup(String studentId, String groupId, String semester) {
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

        if (enrollmentRepository.existsByStudentIdAndGroupId(student.getStudentId(), Integer.parseInt(group.getId()))) {
            throw new BusinessValidationException("El estudiante ya está inscrito en este group");
        }
    }

    public List<Enrollment> getEnrollmentsByStudentId(String studentId) {
        return enrollmentRepository.findByStudentId(studentId);
    }

    public List<Enrollment> getEnrollmentsByGroupId(String groupId) {
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

    /**
     * Inscribir estudiante en materia específica y grupo específico usando abreviatura
     */
    // PENDIENTE DE REVISIÓN
    public boolean enrollStudentInSubjectGroupByCode(String studentId, String subjectCode, String groupId) {
        Student student = studentService.findById(studentId);
        Subject catalogSubject = SubjectCatalog.getSubject(subjectCode);
        if (catalogSubject == null) {
            throw new ResourceNotFoundException("Subject in catalog", "code", subjectCode);
        }
        Subject subject = subjectRepository.findById(catalogSubject.getSubjectId()).orElse(null);
        if (subject == null) {
            throw new ResourceNotFoundException("Subject in database", "code", subjectCode);
        }
        Group targetGroup = subject.getAvailableGroups().stream()
                .filter(group -> String.valueOf(group.getId()).equals(groupId))
                .findFirst()
                .orElse(null);
        if (targetGroup == null) {
            throw new ResourceNotFoundException("Group in subject " + subjectCode, "groupId", groupId);
        }
        long enrolledCount = groupService.getAllStudentsInGroup(targetGroup.getId(), studentService.findAll()).size();
        if (enrolledCount >= targetGroup.getCapacity()) {
            throw new BusinessValidationException("Group " + groupId + " is at full capacity");
        }
        if (!targetGroup.isActive()) {
            throw new BusinessValidationException("El grupo no está activo");
        }
        // Usar verifyScheduleConflict para validar conflicto de horario
        if (verifyScheduleConflict(student, targetGroup, subject)) {
            throw new BusinessValidationException("Conflicto de horario con otra materia");
        }
        List<Semester> semesters = student.getSemesters();
        if (semesters.isEmpty()) {
            throw new BusinessValidationException("El estudiante no tiene semestre activo");
        }
        Semester currentSemester = semesters.get(semesters.size() - 1);
        int enrollmentId = (int) (Math.random() * 100000);
        Enrollment newEnrollment = new Enrollment(targetGroup, subject, enrollmentId, "active", 0.0);
        currentSemester.addSubject(newEnrollment);
        studentService.save(student);
        log.info("Student " + studentId + " successfully enrolled in " + subjectCode + " group " + groupId);
        return true;
    }

    /**
     * Verifica si existe conflicto de horario para el estudiante
     */
    public boolean verifyScheduleConflict(Student student, Group desiredGroup, Subject desiredSubject) {
        if (student.getSemesters().isEmpty()) {
            return false;
        }
        Semester currentSemester = student.getSemesters().get(student.getSemesters().size() - 1);
        Enrollment temporaryEnrollment = new Enrollment(desiredGroup, desiredSubject, 0, "studying", 0.0);
        for (Enrollment existingEnrollment : currentSemester.getSubjects()) {
            if (temporaryEnrollment.validateConflict(existingEnrollment)) {
                log.warning("Group " + desiredGroup.getNumber() + " conflicts with " + existingEnrollment.getSubject().getName());
                return true;
            }
        }
        return false;
    }
}
