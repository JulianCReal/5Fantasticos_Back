// Solo guardar el estudiante - la materia no cambia
package com.example.fantasticosback.service;

import com.example.fantasticosback.dto.request.EnrollmentRequestDTO;
import com.example.fantasticosback.dto.response.StudentDTO;
import com.example.fantasticosback.exception.ResourceNotFoundException;
import com.example.fantasticosback.exception.BusinessValidationException;
import com.example.fantasticosback.mapper.StudentMapper;
import com.example.fantasticosback.model.Document.*;
import com.example.fantasticosback.repository.StudentRepository;
import com.example.fantasticosback.repository.SubjectRepository;
import com.example.fantasticosback.util.AcademicTrafficLight;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class StudentService {

    private static final Logger log = Logger.getLogger(StudentService.class.getName());

    private final StudentRepository studentRepository;
    private final SubjectRepository subjectRepository;


    public Student save(Student student) {
        return studentRepository.save(student);
    }

    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    public Student findById(String id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", id));
    }

    public Student update(String id, Student student) {
        // Verificar que el student existe antes de actualizar
        Student existing = findById(id);
        student.setStudentId(id);

        return studentRepository.save(student);
    }

    public void delete(String id) {


        // Validar que no tenga inscripciones activas antes de eliminar
        if (this.getCurrentSchedule(id) != null && !this.getCurrentSchedule(id).isEmpty()) {
            throw new BusinessValidationException("Cannot delete student with active enrollments. Please cancel all subjects first.");
        }

        studentRepository.deleteById(id);
    }

    public List<Student> findByCareer(String career) {
        // Validar que la carrera no esté vacía
        if (career == null || career.trim().isEmpty()) {
            throw new BusinessValidationException("Career cannot be null or empty");
        }
        return studentRepository.findByCareer(career);
    }

    public List<Student> findBySemester(int semester) {
        // Validar que el semestre sea válido
        if (semester < 1 || semester > 10) {
            throw new BusinessValidationException("Semester must be between 1 and 10");
        }
        return studentRepository.findBySemester(semester);
    }

    public StudentDTO convertToStudentDTO(Student student) {
        return StudentMapper.toDTO(student);
    }

    public List<StudentDTO> convertList(List<Student> students) {
        return StudentMapper.toDTOList(students);
    }

    public Student convertToDomain(StudentDTO dto) {
        return StudentMapper.toDomain(dto);
    }


    /**
     * Cancela una inscripción del semestre actual del estudiante
     */
    public void cancelSubject(String studentId, Enrollment enrollment) {
        Student student = findById(studentId);
        if (!student.getSemesters().isEmpty()) {
            Semester currentSemester = student.getSemesters().get(student.getSemesters().size() - 1);
            currentSemester.getSchedule().removeEnrollment(enrollment);
            studentRepository.save(student);
            log.info("Subject " + enrollment.getSubject().getName() + " cancelled successfully.");
        } else {
            log.warning("No active semester to cancel the subject.");
        }
    }

    public AcademicTrafficLight getAcademicTrafficLight(Student student) {
        return student.getAcademicTrafficLight();
    }

    /**
     * Retorna el horario actual del estudiante (del semestre activo)
     */
    public Schedule getCurrentSchedule(String studentId) {
        Student student = findById(studentId);
        return student.getSemesters().stream()
            .filter(Semester::isActive)
            .findFirst()
            .map(Semester::getSchedule)
            .orElseThrow(() -> new ResourceNotFoundException("Active semester not found for student", "studentId", studentId));
    }

    /**
     * Retorna el horario de un semestre específico del estudiante
     */
    public Schedule getScheduleBySemesterNumber(String studentId, int semesterNumber) {
        Student student = findById(studentId);
        return student.getSemesters().stream()
            .filter(s -> s.getId() == semesterNumber)
            .findFirst()
            .map(Semester::getSchedule)
            .orElseThrow(() -> new ResourceNotFoundException("Semester not found for student", "semesterNumber", String.valueOf(semesterNumber)));
    }

    /**
     * Matricula una materia (Enrollment) al semestre activo del estudiante usando DTO
     */
    public Enrollment enrollSubject(String studentId, EnrollmentRequestDTO request) {
        Student student = findById(studentId);
        Semester activeSemester = student.getSemesters().stream()
            .filter(Semester::isActive)
            .findFirst()
            .orElseThrow(() -> new ResourceNotFoundException("Active semester not found for student", "studentId", studentId));
        Schedule schedule = activeSemester.getSchedule();

        Subject subject = subjectRepository.findByCode(request.getSubjectCode())
            .orElseThrow(() -> new ResourceNotFoundException("Subject", "code", request.getSubjectCode()));

        // Buscar el grupo por ID y validar que pertenece a la materia
        Group group = subject.getAvailableGroups().stream()
            .filter(g -> g.getId().equals(request.getGroupId()))
            .findFirst()
            .orElseThrow(() -> new ResourceNotFoundException("Group", "id", request.getGroupId()));

        if (!group.isActive()) {
            throw new BusinessValidationException("El grupo no está activo para inscripciones");
        }
        if (group.getGroupStudents().size() >= group.getCapacity()) {
            throw new BusinessValidationException("El grupo ha alcanzado su capacidad máxima");
        }

        // Validar conflicto de horario
        Enrollment tempEnrollment = new Enrollment(group, subject, 1, "ACTIVE", 0.0);
        for (Enrollment existing : schedule.getEnrollments()) {
            if (tempEnrollment.validateConflict(existing)) {
                throw new BusinessValidationException("Conflicto de horario con otra materia ya inscrita");
            }
        }

        // Crear y agregar la inscripción
        schedule.getEnrollments().add(tempEnrollment);
        save(student);
        return tempEnrollment;
    }




}
