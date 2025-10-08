package com.example.fantasticosback.Persistence.Server;

import com.example.fantasticosback.Dtos.StudentDTO;
import com.example.fantasticosback.Persistence.Repository.StudentRepository;
import com.example.fantasticosback.Persistence.Repository.SubjectRepository;
import com.example.fantasticosback.Model.Entities.Student;
import com.example.fantasticosback.Model.Entities.Subject;
import com.example.fantasticosback.Model.Entities.Group;
import com.example.fantasticosback.Model.Entities.Enrollment;
import com.example.fantasticosback.util.SubjectCatalog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    public Student save(Student student) {
        return studentRepository.save(student);
    }

    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    public Student findById(String id) {
        return studentRepository.findById(id).orElse(null);
    }

    public Student update(Student student) {
        return studentRepository.save(student);
    }

    public void delete(String id) {
        studentRepository.deleteById(id);
    }

    public List<Student> findByCareer(String career) {
        return studentRepository.findByCareer(career);
    }

    public List<Student> findBySemester(int semester) {
        return studentRepository.findBySemester(semester);
    }

    public StudentDTO convertToStudentDTO(Student student) {
        return new StudentDTO(
                student.getStudentId(),
                student.getName(),
                student.getCareer(),
                student.getSemester()
        );
    }

    public List<StudentDTO> convertList(List<Student> students) {
        return students.stream().map(this::convertToStudentDTO).collect(Collectors.toList());
    }

    public Student convertToDomain(StudentDTO dto) {
        return new Student(
                dto.getName(),
                "",
                0,
                dto.getCareer(),
                "",
                dto.getId(),
                dto.getSemester(),
                null
        );
    }


    public boolean addSubjectToStudent(String studentId, String groupId) {
        Student student = findById(studentId);
        if (student == null) {
            throw new IllegalArgumentException("Student not found with ID: " + studentId);
        }

        Group targetGroup = findGroupById(groupId);
        if (targetGroup == null) {
            throw new IllegalArgumentException("Group not found with ID: " + groupId);
        }

        boolean success = student.addSubject(targetGroup);
        if (success) {
            studentRepository.save(student);
        }
        return success;
    }

    public boolean removeSubjectFromStudent(String studentId, String enrollmentId) {
        Student student = findById(studentId);
        if (student == null) {
            throw new IllegalArgumentException("Student not found with ID: " + studentId);
        }

        // Buscar la matrícula por ID
        Enrollment enrollmentToRemove = findEnrollmentById(student, enrollmentId);
        if (enrollmentToRemove == null) {
            throw new IllegalArgumentException("Enrollment not found with ID: " + enrollmentId);
        }

        student.removeSubject(enrollmentToRemove);
        studentRepository.save(student);
        return true;
    }

    public boolean cancelSubjectFromStudent(String studentId, String enrollmentId) {
        Student student = findById(studentId);
        if (student == null) {
            throw new IllegalArgumentException("Student not found with ID: " + studentId);
        }

        Enrollment enrollmentToCancel = findEnrollmentById(student, enrollmentId);
        if (enrollmentToCancel == null) {
            throw new IllegalArgumentException("Enrollment not found with ID: " + enrollmentId);
        }

        student.cancelSubject(enrollmentToCancel);
        studentRepository.save(student);
        return true;
    }

    public List<Object> getCurrentSubjects(String studentId) {
        Student student = findById(studentId);
        if (student == null) {
            throw new IllegalArgumentException("Student not found with ID: " + studentId);
        }

        // Usar el método real implementado en el modelo Student
        return student.getCurrentSchedule().stream()
                .map(enrollment -> Map.of(
                    "enrollmentId", enrollment.getId(),
                    "subjectName", enrollment.getGroup().getSubject().getName(),
                    "subjectId", enrollment.getGroup().getSubject().getSubjectId(),
                    "groupNumber", enrollment.getGroup().getNumber(),
                    "status", enrollment.getStatus(),
                    "credits", enrollment.getGroup().getSubject().getCredits(),
                    "teacherName", enrollment.getGroup().getTeacher() != null ?
                        enrollment.getGroup().getTeacher().getName() + " " + enrollment.getGroup().getTeacher().getLastName() : "No asignado"
                ))
                .collect(Collectors.toList());
    }

    /**
     * 1. Obtiene materias disponibles para que el estudiante se inscriba
     */
    public List<Object> getAvailableSubjectsForStudent(String studentId) {
        Student student = findById(studentId);
        if (student == null) {
            throw new IllegalArgumentException("Student not found with ID: " + studentId);
        }

        List<Subject> allSubjects = subjectRepository.findAll();

        return allSubjects.stream()
                .filter(subject -> subject.getSemester() == student.getSemester()) // Materias del semestre actual
                .filter(subject -> !isStudentAlreadyEnrolled(student, subject)) // No está inscrito
                .map(subject -> Map.of(
                    "subjectId", subject.getSubjectId(),
                    "name", subject.getName(),
                    "credits", subject.getCredits(),
                    "semester", subject.getSemester(),
                    "availableGroups", subject.getAvailableGroups().size()
                ))
                .collect(Collectors.toList());
    }

    /**
     * 2. Obtiene grupos disponibles de una materia específica
     */
    public List<Object> getAvailableGroupsForSubject(String studentId, String subjectId) {
        Student student = findById(studentId);
        if (student == null) {
            throw new IllegalArgumentException("Student not found with ID: " + studentId);
        }

        Subject subject = subjectRepository.findById(subjectId).orElse(null);
        if (subject == null) {
            throw new IllegalArgumentException("Subject not found with ID: " + subjectId);
        }

        return subject.getAvailableGroups().stream()
                .filter(Group::isActive) // Solo grupos activos
                .map(group -> Map.of(
                    "groupId", group.getId(),
                    "groupNumber", group.getNumber(),
                    "capacity", group.getCapacity(),
                    "enrolled", group.getGroupStudents().size(),
                    "available", group.getCapacity() - group.getGroupStudents().size(),
                    "teacherName", group.getTeacher() != null ?
                        group.getTeacher().getName() + " " + group.getTeacher().getLastName() : "No asignado",
                    "schedule", group.getSessions().stream()
                        .map(session -> session.getDay() + " " + session.getStartTime() + "-" + session.getEndTime())
                        .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());
    }

    /**
     * 3. Inscribir estudiante en materia específica y grupo específico
     */
    public boolean enrollStudentInSubjectGroup(String studentId, String subjectId, String groupId) {
        Student student = findById(studentId);
        if (student == null) {
            throw new IllegalArgumentException("Student not found with ID: " + studentId);
        }

        Subject subject = subjectRepository.findById(subjectId).orElse(null);
        if (subject == null) {
            throw new IllegalArgumentException("Subject not found with ID: " + subjectId);
        }

        // Buscar el grupo específico dentro de la materia
        Group targetGroup = subject.getAvailableGroups().stream()
                .filter(group -> String.valueOf(group.getId()).equals(groupId))
                .findFirst()
                .orElse(null);

        if (targetGroup == null) {
            throw new IllegalArgumentException("Group not found with ID: " + groupId + " in subject: " + subjectId);
        }

        // Usar el método real del modelo Student
        boolean success = student.addSubject(targetGroup);
        if (success) {
            studentRepository.save(student);
        }
        return success;
    }

    // Métodos mejorados para gestión de materias - FLUJO INTUITIVO con abreviaturas

    /**
     * 2. Obtiene grupos disponibles de una materia específica usando abreviatura
     */
    public List<Object> getAvailableGroupsForSubjectByCode(String studentId, String subjectCode) {
        Student student = findById(studentId);
        if (student == null) {
            throw new IllegalArgumentException("Student not found with ID: " + studentId);
        }

        // Obtener la materia del catálogo usando la abreviatura
        Subject catalogSubject = SubjectCatalog.getSubject(subjectCode);
        if (catalogSubject == null) {
            throw new IllegalArgumentException("Subject not found in catalog with code: " + subjectCode);
        }

        // Buscar la materia en la BD usando el ID del catálogo
        Subject subject = subjectRepository.findById(catalogSubject.getSubjectId()).orElse(null);
        if (subject == null) {
            throw new IllegalArgumentException("Subject not found in database with code: " + subjectCode);
        }

        return subject.getAvailableGroups().stream()
                .filter(Group::isActive) // Solo grupos activos
                .map(group -> Map.of(
                    "groupId", group.getId(),
                    "groupNumber", group.getNumber(),
                    "capacity", group.getCapacity(),
                    "enrolled", group.getGroupStudents().size(),
                    "available", group.getCapacity() - group.getGroupStudents().size(),
                    "teacherName", group.getTeacher() != null ?
                        group.getTeacher().getName() + " " + group.getTeacher().getLastName() : "No asignado",
                    "schedule", group.getSessions().stream()
                        .map(session -> session.getDay() + " " + session.getStartTime() + "-" + session.getEndTime())
                        .collect(Collectors.toList()),
                    "subjectCode", subjectCode,
                    "subjectName", subject.getName()
                ))
                .collect(Collectors.toList());
    }

    /**
     * 3. Inscribir estudiante en materia específica y grupo específico usando abreviatura
     */
    public boolean enrollStudentInSubjectGroupByCode(String studentId, String subjectCode, String groupId) {
        Student student = findById(studentId);
        if (student == null) {
            throw new IllegalArgumentException("Student not found with ID: " + studentId);
        }

        // Obtener la materia del catálogo usando la abreviatura
        Subject catalogSubject = SubjectCatalog.getSubject(subjectCode);
        if (catalogSubject == null) {
            throw new IllegalArgumentException("Subject not found in catalog with code: " + subjectCode);
        }

        // Buscar la materia en la BD usando el ID del catálogo
        Subject subject = subjectRepository.findById(catalogSubject.getSubjectId()).orElse(null);
        if (subject == null) {
            throw new IllegalArgumentException("Subject not found in database with code: " + subjectCode);
        }

        // Buscar el grupo específico dentro de la materia
        Group targetGroup = subject.getAvailableGroups().stream()
                .filter(group -> String.valueOf(group.getId()).equals(groupId))
                .findFirst()
                .orElse(null);

        if (targetGroup == null) {
            throw new IllegalArgumentException("Group not found with ID: " + groupId + " in subject: " + subjectCode);
        }

        // Usar el método real del modelo Student
        boolean success = student.addSubject(targetGroup);
        if (success) {
            studentRepository.save(student);
        }
        return success;
    }

    // Métodos auxiliares

    private boolean isStudentAlreadyEnrolled(Student student, Subject subject) {
        return student.getCurrentSchedule().stream()
                .anyMatch(enrollment ->
                    enrollment.getGroup().getSubject().getSubjectId().equals(subject.getSubjectId()));
    }

    private Group findGroupById(String groupId) {
        List<Subject> allSubjects = subjectRepository.findAll();

        for (Subject subject : allSubjects) {
            for (Group group : subject.getAvailableGroups()) {
                if (String.valueOf(group.getId()).equals(groupId)) {
                    return group;
                }
            }
        }
        return null;
    }

    private Enrollment findEnrollmentById(Student student, String enrollmentId) {
        for (int i = 0; i < student.getSemesters().size(); i++) {
            for (Enrollment enrollment : student.getSemesters().get(i).getSubjects()) {
                if (String.valueOf(enrollment.getId()).equals(enrollmentId)) {
                    return enrollment;
                }
            }
        }
        return null;
    }
}
