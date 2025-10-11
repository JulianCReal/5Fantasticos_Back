package com.example.fantasticosback.Persistence.Service;

import com.example.fantasticosback.Model.Document.*;
import com.example.fantasticosback.Persistence.Repository.DeanOfficeRepository;
import com.example.fantasticosback.Persistence.Repository.StudentRepository;
import com.example.fantasticosback.Persistence.Repository.TeacherRepository;
import com.example.fantasticosback.enums.UserRole;
import com.example.fantasticosback.util.ClassSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.logging.Logger;

@Service
public class ScheduleService {

    private static final Logger log = Logger.getLogger(ScheduleService.class.getName());

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private DeanOfficeRepository deanOfficeRepository;


    public Map<String, Object> getStudentSchedule(String studentId, String userId, UserRole userRole)
            throws IllegalAccessException, IllegalArgumentException {
        
        log.info("Querying schedule for student: " + studentId + " by user: " + userId + " with role: " + userRole);


        Student student = studentRepository.findById(studentId).orElse(null);
        if (student == null) {
            throw new IllegalArgumentException("Student not found with ID: " + studentId);
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
                throw new IllegalAccessException("Invalid user role");
        }

        return generateSchedule(student);
    }


    private void validateStudentAccess(String studentId, String userId) throws IllegalAccessException {
        if (!studentId.equals(userId)) {
            log.warning("Access denied: Student " + userId + " tried to query schedule of " + studentId);
            throw new IllegalAccessException("Students can only query their own schedule");
        }
    }


    private void validateTeacherAccess(Student student, String teacherId) throws IllegalAccessException {
        Teacher teacher = teacherRepository.findById(teacherId).orElse(null);
        if (teacher == null) {
            throw new IllegalArgumentException("Teacher not found with ID: " + teacherId);
        }

        boolean hasStudent = false;

        for (Semester semester : student.getSemesters()) {
            for (Enrollment enrollment : semester.getSubjects()) {
                Group group = enrollment.getGroup();
                if (group.getTeacher() != null && teacherId.equals(group.getTeacher().getId())) {
                    hasStudent = true;
                    break;
                }
            }
            if (hasStudent) break;
        }

        if (!hasStudent) {
            log.warning("Access denied: Teacher " + teacherId + " is not assigned to student " + student.getStudentId());
            throw new IllegalAccessException("Teachers can only query schedules of their assigned students");
        }
    }


    private void validateDeanOfficeAccess(Student student, String deanOfficeId) throws IllegalAccessException {
        DeanOffice deanOffice = deanOfficeRepository.findById(deanOfficeId).orElse(null);
        if (deanOffice == null) {
            throw new IllegalArgumentException("Dean office not found with ID: " + deanOfficeId);
        }


        if (!student.getCareer().equals(deanOffice.getFaculty())) {
            log.warning("Access denied: Dean office " + deanOfficeId + " tried to query student from another faculty");
            throw new IllegalAccessException("Dean office can only query students from their faculty");
        }
    }


    private Map<String, Object> generateSchedule(Student student) {
        List<Map<String, Object>> classes = new ArrayList<>();

        Semester currentSemester = null;
        for (Semester semester : student.getSemesters()) {
            if (semester.isActive()) {
                currentSemester = semester;
                break;
            }
        }

        if (currentSemester == null && !student.getSemesters().isEmpty()) {
            currentSemester = student.getSemesters().get(student.getSemesters().size() - 1);
        }

        if (currentSemester != null) {
            for (Enrollment enrollment : currentSemester.getSubjects()) {
                if (!"cancelled".equals(enrollment.getStatus())) {
                    classes.addAll(generateEnrollmentClasses(enrollment));
                }
            }
        }

        Map<String, Object> schedule = new HashMap<>();
        schedule.put("studentId", student.getStudentId());
        schedule.put("studentName", student.getName() + " " + student.getLastName());
        schedule.put("career", student.getCareer());
        schedule.put("semester", student.getSemester());
        schedule.put("classes", classes);

        return schedule;
    }


    private List<Map<String, Object>> generateEnrollmentClasses(Enrollment enrollment) {
        List<Map<String, Object>> classes = new ArrayList<>();
        Group group = enrollment.getGroup();
        Subject subject = enrollment.getSubject();
        Teacher teacher = group.getTeacher();

        String teacherName = (teacher != null) ?
            teacher.getName() + " " + teacher.getLastName() : "Unassigned";

        for (ClassSession session : group.getSessions()) {
            Map<String, Object> classInfo = new HashMap<>();
            classInfo.put("subject", subject.getName());
            classInfo.put("subjectCode", String.valueOf(subject.getSubjectId()));
            classInfo.put("credits", subject.getCredits());
            classInfo.put("groupNumber", group.getNumber());
            classInfo.put("teacherName", teacherName);
            classInfo.put("day", session.getDay());
            classInfo.put("startTime", session.getStartTime());
            classInfo.put("endTime", session.getEndTime());
            classInfo.put("classroom", session.getClassroom());

            classes.add(classInfo);
        }

        return classes;
    }
}