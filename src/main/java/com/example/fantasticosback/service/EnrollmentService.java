package com.example.fantasticosback.service;

import com.example.fantasticosback.exception.BusinessValidationException;
import com.example.fantasticosback.exception.ResourceNotFoundException;
import com.example.fantasticosback.model.Document.*;
import com.example.fantasticosback.repository.EnrollmentRepository;
import com.example.fantasticosback.repository.StudentRepository;
import com.example.fantasticosback.repository.ScheduleRepository;
import com.example.fantasticosback.util.ClassSession;
import java.util.logging.Logger;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final GroupService groupService;
    private final StudentRepository studentRepository;
    private final ScheduleService scheduleService;
    private final ScheduleRepository scheduleRepository;
    private static final Logger log = Logger.getLogger(EnrollmentService.class.getName());

    public EnrollmentService(
            EnrollmentRepository enrollmentRepository,
            GroupService groupService,
            StudentRepository studentRepository,
            ScheduleService scheduleService,
            ScheduleRepository scheduleRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.groupService = groupService;
        this.studentRepository = studentRepository;
        this.scheduleService = scheduleService;
        this.scheduleRepository = scheduleRepository;
    }

    public Enrollment enrollStudentInGroup(String studentId, String groupId, String semester) {
        log.info("Enrolling student " + studentId + " to group " + groupId + " for semester " + semester);
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", studentId));
        Group group = groupService.getGroupById(groupId);

        validateEnrollment(student, group);

        // Verificar si hay conflictos de horario con el horario actual del estudiante
        for (ClassSession newSession : group.getSessions()) {
            if (scheduleService.hasTimeConflict(studentId, newSession)) {
                throw new BusinessValidationException("Existe conflicto de horario con otras materias inscritas");
            }
        }

        // Obtener el id de la materia asociada al grupo
        String subjectId = group.getSubjectId();

        Enrollment enrollment = new Enrollment();
        enrollment.setId(generateEnrollmentId());
        enrollment.setStudentId(studentId);
        enrollment.setGroupId(groupId);
        enrollment.setSubjectId(subjectId);
        enrollment.setStatus("ACTIVE");
        enrollment.setFinalGrade(0.0);

        // Agregar estudiante al grupo
        groupService.addStudentToGroup(groupId, student);

        // Guardar la enrollment
        Enrollment saved = enrollmentRepository.save(enrollment);

        // Agregar la referencia de la enrollment al Schedule activo del estudiante (o crear uno nuevo)
        List<Schedule> schedules = scheduleRepository.findByStudentId(studentId);
        Schedule activeSchedule = schedules.stream()
                .filter(s -> s.getSemester() != null && s.getSemester().isActive())
                .findFirst()
                .orElse(null);

        if (activeSchedule == null) {
            activeSchedule = new Schedule();
            activeSchedule.setStudentId(studentId);
            Semester sem = parseSemester(semester, studentId);
            activeSchedule.setSemester(sem);
        }

        if (activeSchedule.getEnrollmentIds() == null) {
            activeSchedule.setEnrollmentIds(new java.util.ArrayList<>());
        }
        activeSchedule.getEnrollmentIds().add(saved.getId());
        scheduleRepository.save(activeSchedule);

        return saved;
    }

    public void cancelEnrollment(String studentId, String enrollmentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", studentId));
        Enrollment enrollment = findEnrollmentById(enrollmentId);

        validateCancellation(enrollment);

        enrollment.setStatus("CANCELLED");
        Group group = groupService.getGroupById(enrollment.getGroupId());
        groupService.removeStudentFromGroup(group.getId(), student.getStudentId());

        // Remover referencia de la enrollment del schedule si existe
        List<Schedule> schedules = scheduleRepository.findByStudentId(studentId);
        Schedule activeSchedule = schedules.stream()
                .filter(s -> s.getEnrollmentIds() != null && s.getEnrollmentIds().contains(enrollmentId))
                .findFirst()
                .orElse(null);
        if (activeSchedule != null && activeSchedule.getEnrollmentIds() != null) {
            activeSchedule.getEnrollmentIds().remove(enrollmentId);
            scheduleRepository.save(activeSchedule);
        }

        enrollmentRepository.save(enrollment);
    }

    private void validateCancellation(Enrollment enrollment) {
        if (!enrollment.getStatus().equals("ACTIVE")) {
            throw new BusinessValidationException("La inscripción ya está cancelada o finalizada");
        }

    }

    private void validateEnrollment(Student student, Group group) {

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



    /**
     * Verifica si existe conflicto de horario para el estudiante al intentar inscribirlo en un grupo.
     * Compara las sesiones (ClassSession) del grupo deseado con las de los grupos ya inscritos.
     */

    public boolean verifyScheduleConflict(String studentId, String desiredGroupId) {
         Group desiredGroup = groupService.getGroupById(desiredGroupId);
         List<ClassSession> desiredSessions = desiredGroup.getSessions();

         // Agrupar todas las sesiones inscritas por día
         List<Enrollment> activeEnrollments = enrollmentRepository.findByStudentId(studentId)
                 .stream()
                 .filter(e -> "ACTIVE".equalsIgnoreCase(e.getStatus()))
                 .toList();
         java.util.Map<String, java.util.List<ClassSession>> sessionsByDay = new java.util.HashMap<>();
         for (Enrollment enrollment : activeEnrollments) {
             Group enrolledGroup = groupService.getGroupById(enrollment.getGroupId());
             for (ClassSession session : enrolledGroup.getSessions()) {
                 sessionsByDay.computeIfAbsent(session.getDay(), k -> new java.util.ArrayList<>()).add(session);
             }
         }

         // Para cada sesión del grupo deseado, solo comparar con las sesiones del mismo día
         for (ClassSession desiredSession : desiredSessions) {
             List<ClassSession> sameDaySessions = sessionsByDay.getOrDefault(desiredSession.getDay(), java.util.Collections.emptyList());
             for (ClassSession enrolledSession : sameDaySessions) {
                 if (sessionsConflict(desiredSession, enrolledSession)) {
                     log.warning("Conflicto de horario: " + desiredSession + " con " + enrolledSession);
                     return true;
                 }
             }
         }
         return false;
     }

    /**
     * Verifica si dos sesiones se solapan en día y hora.
     */
    private boolean sessionsConflict(ClassSession s1, ClassSession s2) {
        if (!s1.getDay().equalsIgnoreCase(s2.getDay())) return false;
        // Asume formato "HH:mm" para horaInicio y horaFin
        int start1 = parseHour(s1.getStartTime());
        int end1 = parseHour(s1.getEndTime());
        int start2 = parseHour(s2.getStartTime());
        int end2 = parseHour(s2.getEndTime());
        // Hay conflicto si los intervalos se solapan
        return start1 < end2 && start2 < end1;
    }

    private int parseHour(String hour) {
        // Convierte "HH:mm" a minutos desde medianoche
        String[] parts = hour.split(":");
        return Integer.parseInt(parts[0]) * 60 + Integer.parseInt(parts[1]);
    }

    private Semester parseSemester(String semesterStr, String studentId) {
        String[] parts = semesterStr.split("-");
        if (parts.length != 2) {
            throw new BusinessValidationException("Invalid semester format. Expected 'YYYY-P'");
        }
        int year = Integer.parseInt(parts[0]);
        int period = Integer.parseInt(parts[1]);
        return new Semester(semesterStr, studentId, year, period, true, 0, null);
    }
}
