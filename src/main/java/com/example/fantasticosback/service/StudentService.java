// Solo guardar el estudiante - la materia no cambia
package com.example.fantasticosback.service;

import com.example.fantasticosback.dto.response.StudentDTO;
import com.example.fantasticosback.exception.ResourceNotFoundException;
import com.example.fantasticosback.exception.BusinessValidationException;
import com.example.fantasticosback.mapper.StudentMapper;
import com.example.fantasticosback.model.Document.*;
import com.example.fantasticosback.repository.StudentRepository;
import com.example.fantasticosback.util.AcademicTrafficLight;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.logging.Logger;

@RequiredArgsConstructor
@Service
public class StudentService {

    private static final Logger log = Logger.getLogger(StudentService.class.getName());
    private final StudentRepository studentRepository;

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
        // Verificar que el student existe antes de eliminar
        Student existing = findById(id);

        // Validar que no tenga inscripciones activas antes de eliminar
        if (this.getCurrentSchedule(existing) != null && !this.getCurrentSchedule(existing).isEmpty()) {
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

    public List<Object> getCurrentSubjects(String studentId) {
        Student student = findById(studentId); // Ya lanza ResourceNotFoundException si no existe

        return this.getCurrentSchedule(student).stream()
                .map(enrollment -> Map.of(
                        "enrollmentId", enrollment.getId(),
                        "subjectName", enrollment.getSubject().getName(),
                        "subjectId", enrollment.getSubject().getSubjectId(),
                        "groupNumber", enrollment.getGroup().getNumber(),
                        "status", enrollment.getStatus(),
                        "credits", enrollment.getSubject().getCredits(),
                        "teacherName", enrollment.getGroup().getTeacher() != null ?
                                enrollment.getGroup().getTeacher().getName() + " " + enrollment.getGroup().getTeacher().getLastName() : "No asignado",
                        "classSessions", enrollment.getGroup().getSessions().stream()
                                .map(session -> Map.of(
                                        "day", session.getDay(),
                                        "startTime", session.getStartTime(),
                                        "endTime", session.getEndTime(),
                                        "classroom", session.getClassroom()
                                ))
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());
    }

    /**
     * Obtiene el horario de un semestre específico del estudiante
     */
    public List<Object> getStudentScheduleBySemester(String studentId, int semesterIndex) {
        Student student = findById(studentId);

        if (semesterIndex < 0 || semesterIndex >= 10) {
            throw new BusinessValidationException("Semester index must be between 0 and 9");
        }

        return this.getScheduleBySemester(student, semesterIndex).stream()
                .map(enrollment -> Map.of(
                    "enrollmentId", enrollment.getId(),
                    "subjectName", enrollment.getSubject().getName(),
                    "subjectId", enrollment.getSubject().getSubjectId(),
                    "groupNumber", enrollment.getGroup().getNumber(),
                    "status", enrollment.getStatus(),
                    "credits", enrollment.getSubject().getCredits(),
                    "finalGrade", enrollment.getFinalGrade(),
                    "teacherName", enrollment.getGroup().getTeacher() != null ?
                        enrollment.getGroup().getTeacher().getName() + " " + enrollment.getGroup().getTeacher().getLastName() : "No asignado",
                    "classSessions", enrollment.getGroup().getSessions().stream()
                        .map(session -> Map.of(
                            "day", session.getDay(),
                            "startTime", session.getStartTime(),
                            "endTime", session.getEndTime(),
                            "classroom", session.getClassroom()
                        ))
                        .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());
    }


    /**
     * Elimina una materia del semestre actual del estudiante
     */
    public void removeSubject(String studentId, Enrollment enrollment) {
        Student student = findById(studentId);
        if (!student.getSemesters().isEmpty()) {
            Semester currentSemester = student.getSemesters().get(student.getSemesters().size() - 1);
            currentSemester.removeSubject(enrollment);
            studentRepository.save(student);
            log.info("Subject " + enrollment.getSubject().getName() + " withdrawn successfully.");
        } else {
            log.warning("No active semester to remove the subject.");
        }
    }

    /**
     * Cancela una inscripción del semestre actual del estudiante
     */
    public void cancelSubject(String studentId, Enrollment enrollment) {
        Student student = findById(studentId);
        if (!student.getSemesters().isEmpty()) {
            Semester currentSemester = student.getSemesters().get(student.getSemesters().size() - 1);
            currentSemester.cancelSubject(enrollment);
            studentRepository.save(student);
            log.info("Subject " + enrollment.getSubject().getName() + " cancelled successfully.");
        } else {
            log.warning("No active semester to cancel the subject.");
        }
    }

    /**
     * Obtiene el horario del semestre actual de un estudiante
     * @param student Estudiante
     * @return Lista de materias inscritas en el semestre actual
     */
    public ArrayList<Enrollment> getCurrentSchedule(Student student) {
        List<Semester> semesters = student.getSemesters();
        if (semesters.isEmpty()) {
            log.info("No hay semestres registrados para el estudiante.");
            return new ArrayList<>();
        }
        Semester currentSemester = semesters.get(semesters.size() - 1);
        log.info("Consultando horario actual - Semestre: " + (semesters.size()));
        return currentSemester.getSubjects();
    }

    /**
     * Obtiene el horario de un semestre específico de un estudiante
     * @param student Estudiante
     * @param semesterIndex Índice del semestre (0 para el primer semestre)
     * @return Lista de materias del semestre especificado
     */
    public ArrayList<Enrollment> getScheduleBySemester(Student student, int semesterIndex) {
        List<Semester> semesters = student.getSemesters();
        if (semesterIndex < 0 || semesterIndex >= semesters.size()) {
            log.warning("Índice de semestre inválido: " + semesterIndex + ". Semestres disponibles: 0-" + (semesters.size() - 1));
            return new ArrayList<>();
        }
        Semester targetSemester = semesters.get(semesterIndex);
        log.info("Consultando horario del semestre " + (semesterIndex + 1));
        return targetSemester.getSubjects();
    }

    public AcademicTrafficLight getAcademicTrafficLight(Student student) {
        return student.getAcademicTrafficLight();
    }

    /**
     * Verifica si el estudiante tiene materias activas en el semestre actual
     *
     * @return true si tiene materias activas, false en caso contrario
     */
    public boolean hasActiveSchedule(Student student) {
        ArrayList<Enrollment> currentSchedule = getCurrentSchedule(student);
        return !currentSchedule.isEmpty() &&
                currentSchedule.stream().anyMatch(enrollment -> "active".equals(enrollment.getStatus()));
    }

    /**
     * Actualiza parcialmente los campos de un estudiante existente
     */
    public Student partialUpdate(String id, StudentDTO dto) {
        Student student = findById(id);
        if (dto.getName() != null) student.setName(dto.getName());
        if (dto.getCareer() != null) student.setCareer(dto.getCareer());
        if (dto.getSemester() != 0) student.setSemester(dto.getSemester());
        return studentRepository.save(student);
    }




}
