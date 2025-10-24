package com.example.fantasticosback.service;

import com.example.fantasticosback.model.Document.*;
import com.example.fantasticosback.repository.*;
import com.example.fantasticosback.enums.UserRole;
import com.example.fantasticosback.util.ClassSession;
import com.example.fantasticosback.exception.ResourceNotFoundException;
import com.example.fantasticosback.exception.BusinessValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.logging.Logger;


@Service
@RequiredArgsConstructor
public class ScheduleService {

    private static final Logger log = Logger.getLogger(ScheduleService.class.getName());
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final DeanOfficeRepository deanOfficeRepository;
    private final ScheduleRepository scheduleRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final GroupService groupService;
    private final SubjectRepository subjectRepository;


    public Map<String, Object> getStudentSchedule(String studentId, String userId, UserRole userRole) {

        log.info("Querying schedule for student: " + studentId + " by user: " + userId + " with role: " + userRole);


        Student student = studentRepository.findById(studentId).orElse(null);
        if (student == null) {
            throw new ResourceNotFoundException("Student", "id", studentId);
        }

        switch (userRole) {
            case STUDENT:
                validateStudentAccess(studentId, userId);
                break;
            case TEACHER:
                validateTeacherAccess(student, userId);
                break;
            case DEAN_OFFICE:
                validateDeanOfficeAccess(student, userId);
                break;
            default:
                throw new BusinessValidationException("Invalid user role");
        }

        return generateSchedule(student);
    }

    public Map<String, Object> getStudentSchedule(String studentId, String userId, String userRoleStr) {
        if (userRoleStr == null || userRoleStr.trim().isEmpty()) {
            throw new BusinessValidationException("Missing user role header");
        }
        UserRole userRole;
        try {
            userRole = UserRole.valueOf(userRoleStr.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new BusinessValidationException("Invalid user role. Valid roles: STUDENT, TEACHER, DEAN_OFFICE");
        }
        return getStudentSchedule(studentId, userId, userRole);
    }


    private void validateStudentAccess(String studentId, String userId) {
        if (!studentId.equals(userId)) {
            log.warning("Access denied: Student " + userId + " tried to query schedule of " + studentId);
            throw new BusinessValidationException("Students can only query their own schedule");
        }
    }


    private void validateTeacherAccess(Student student, String teacherId) {
        Teacher teacher = teacherRepository.findById(teacherId).orElse(null);
        if (teacher == null) {
            throw new ResourceNotFoundException("Teacher", "id", teacherId);
        }

        boolean hasStudent = false;

        // Buscar en todas las schedules del estudiante
        List<Schedule> schedules = scheduleRepository.findByStudentId(student.getStudentId());
        for (Schedule schedule : schedules) {
            if (schedule.getEnrollmentIds() == null) continue;
            for (String enrollmentId : schedule.getEnrollmentIds()) {
                Enrollment enrollment = enrollmentRepository.findById(enrollmentId).orElse(null);
                if (enrollment == null) continue;
                try {
                    Group group = groupService.getGroupById(enrollment.getGroupId());
                    if (group.getTeacher() != null && teacherId.equals(group.getTeacher().getId())) {
                        hasStudent = true;
                        break;
                    }
                } catch (Exception ex) {
                    // si el grupo no existe o hay error, continuar
                    log.fine("Skipping enrollment " + enrollmentId + " while validating teacher access: " + ex.getMessage());
                }
            }
            if (hasStudent) break;
        }

        if (!hasStudent) {
            log.warning("Access denied: Teacher " + teacherId + " is not assigned to student " + student.getStudentId());
            throw new BusinessValidationException("Teachers can only query schedules of their assigned students");
        }
    }


    private void validateDeanOfficeAccess(Student student, String deanOfficeId) {
        DeanOffice deanOffice = deanOfficeRepository.findById(deanOfficeId).orElse(null);
        if (deanOffice == null) {
            throw new ResourceNotFoundException("DeanOffice", "id", deanOfficeId);
        }


        if (!student.getCareer().equals(deanOffice.getFaculty())) {
            log.warning("Access denied: Dean office " + deanOfficeId + " tried to query student from another faculty");
            throw new BusinessValidationException("Dean office can only query students from their faculty");
        }
    }


    private Map<String, Object> generateSchedule(Student student) {
        List<Map<String, Object>> classes = new ArrayList<>();

        // Buscar todos los schedules del estudiante
        List<Schedule> schedules = scheduleRepository.findByStudentId(student.getStudentId());

        for (Schedule currentSchedule : schedules) {
            if (currentSchedule.getEnrollmentIds() != null) {
                for (String enrollmentId : currentSchedule.getEnrollmentIds()) {
                    Enrollment enrollment = enrollmentRepository.findById(enrollmentId).orElse(null);
                    if (enrollment == null) continue;
                    if (!"CANCELLED".equalsIgnoreCase(enrollment.getStatus())) {
                        classes.addAll(generateEnrollmentClasses(enrollment));
                    }
                }
            }
        }

        Map<String, Object> schedule = new HashMap<>();
        schedule.put("studentId", student.getStudentId());
        schedule.put("studentName", student.getName() + " " + student.getLastName());
        schedule.put("career", student.getCareer());
        schedule.put("semester", student.getCurrentSemester());
        schedule.put("classes", classes);

        return schedule;
    }


    private List<Map<String, Object>> generateEnrollmentClasses(Enrollment enrollment) {
        List<Map<String, Object>> classes = new ArrayList<>();
        Group group = null;
        Subject subject = null;

        try {
            group = groupService.getGroupById(enrollment.getGroupId());
        } catch (Exception ex) {
            log.warning("Group not found for enrollment " + enrollment.getId() + ": " + ex.getMessage());
        }
        if (enrollment.getSubjectId() != null) {
            subject = subjectRepository.findById(enrollment.getSubjectId()).orElse(null);
        }

        String teacherName = "Unassigned";
        if (group != null && group.getTeacher() != null) {
            teacherName = group.getTeacher().getName() + " " + group.getTeacher().getLastName();
        }

        if (group != null && group.getSessions() != null) {
            for (ClassSession session : group.getSessions()) {
                Map<String, Object> classInfo = new HashMap<>();
                classInfo.put("subject", subject != null ? subject.getName() : "Unknown");
                classInfo.put("subjectCode", subject != null ? String.valueOf(subject.getSubjectId()) : "");
                classInfo.put("credits", subject != null ? subject.getCredits() : 0);
                classInfo.put("groupNumber", group.getNumber());
                classInfo.put("teacherName", teacherName);
                classInfo.put("day", session.getDay());
                classInfo.put("startTime", session.getStartTime());
                classInfo.put("endTime", session.getEndTime());
                classInfo.put("classroom", session.getClassroom());

                classes.add(classInfo);
            }
        }

        return classes;
    }

    public boolean hasTimeConflict(String studentId, ClassSession newSession) {
        // Buscar todos los schedules del estudiante
        List<Schedule> schedules = scheduleRepository.findByStudentId(studentId);

        for (Schedule activeSchedule : schedules) {
            if (activeSchedule.getEnrollmentIds() == null) continue;
            for (String enrollmentId : activeSchedule.getEnrollmentIds()) {
                Enrollment enrollment = enrollmentRepository.findById(enrollmentId).orElse(null);
                if (enrollment == null) continue;
                try {
                    Group group = groupService.getGroupById(enrollment.getGroupId());
                    for (ClassSession existingSession : group.getSessions()) {
                        if (existingSession.verifyConflict(newSession)) {
                            return true;
                        }
                    }
                } catch (Exception ex) {
                    // ignorar grupos faltantes
                }
            }
        }
        return false;
    }


    /**
     * Elimina una inscripción (Enrollment) de un horario (Schedule) por ID y guarda el cambio
     */
    public void removeEnrollment(Schedule schedule, String enrollmentId) {
        if (schedule == null || enrollmentId == null) return;
        if (schedule.getEnrollmentIds() != null) {
            schedule.getEnrollmentIds().remove(enrollmentId);
            scheduleRepository.save(schedule);
            log.info("Enrollment ID " + enrollmentId + " removido correctamente del horario.");
        }
    }
}