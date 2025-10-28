package com.example.fantasticosback.service;

import com.example.fantasticosback.dto.request.EnrollmentRequestDTO;
import com.example.fantasticosback.dto.response.StudentDTO;
import com.example.fantasticosback.dto.response.GroupResponseDTO;
import com.example.fantasticosback.exception.ResourceNotFoundException;
import com.example.fantasticosback.exception.BusinessValidationException;
import com.example.fantasticosback.mapper.StudentMapper;
import com.example.fantasticosback.model.Document.*;
import com.example.fantasticosback.repository.ScheduleRepository;
import com.example.fantasticosback.repository.StudentRepository;
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
    private final ScheduleRepository scheduleRepository;
    private final ScheduleService scheduleService;
    private final EnrollmentService enrollmentService;
    private final GroupService groupService;


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
        findById(id);
        student.setStudentId(id);

        return studentRepository.save(student);
    }

    public void delete(String id) {
        // Verificar existencia del estudiante
        findById(id);
        // Buscar horario activo por el semester activo dentro de las schedules
        Schedule activeSchedule = scheduleRepository.findByStudentId(id).stream()
                .filter(s -> s.getSemester() != null && s.getSemester().isActive())
                .findFirst()
                .orElse(null);

        if (activeSchedule != null && activeSchedule.getEnrollmentIds() != null && !activeSchedule.getEnrollmentIds().isEmpty()) {
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
        return studentRepository.findByCurrentSemester(semester);
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
    public void cancelSubject(String studentId, String enrollmentId) {
        // Primero cancelar la inscripción en la colección de enrollments
        enrollmentService.cancelEnrollment(studentId, enrollmentId);

        // Luego remover la referencia del horario activo
        Schedule activeSchedule = scheduleRepository.findByStudentId(studentId).stream()
                .filter(s -> s.getSemester() != null && s.getSemester().isActive())
                .findFirst()
                .orElse(null);
        if (activeSchedule == null) {
            log.warning("No active schedule found to remove enrollment reference.");
            return;
        }

        scheduleService.removeEnrollment(activeSchedule, enrollmentId);
        log.info("Enrollment ID " + enrollmentId + " cancelado correctamente del horario del estudiante " + studentId + ".");
    }


    public AcademicTrafficLight getAcademicTrafficLight(Student student) {
        return student.getAcademicTrafficLight();
    }

    /**
     * Retorna el horario current del estudiante (del semestre activo)
     */
    public Schedule getCurrentSchedule(String studentId) {
        List<Schedule> schedules = scheduleRepository.findByStudentId(studentId);
        if (schedules == null || schedules.isEmpty()) {
            throw new ResourceNotFoundException("Active schedule not found for student", "studentId", studentId);
        }

        // Preferir schedule cuyo semester.active == true, si no existe devolver el último schedule disponible
        Schedule activeSchedule = schedules.stream()
                .filter(s -> s.getSemester() != null && s.getSemester().isActive())
                .findFirst()
                .orElse(schedules.get(schedules.size() - 1));

        return activeSchedule;
    }



    // Busca un Schedule por el campo Semester.id (string como "2025-1")
    public Schedule getScheduleBySemester(String studentId, String semester) {
        return scheduleRepository.findByStudentId(studentId).stream()
                .filter(s -> s.getSemester() != null && semester.equals(s.getSemester().getId()))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Schedule", "semesterId", semester));
    }

    /**
     * Actualiza parcialmente los campos de un estudiante existente
     */
    public Student partialUpdate(String id, StudentDTO dto) {
        Student student = findById(id);
        if (dto.getName() != null) student.setName(dto.getName());
        if (dto.getCareer() != null) student.setCareer(dto.getCareer());
        if (dto.getSemester() != 0) student.setCurrentSemester(dto.getSemester());
        return studentRepository.save(student);
    }

    /**
     * Obtiene todos los grupos en los que está inscrito un estudiante
     */
    public List<GroupResponseDTO> getStudentGroups(String studentId) {
        findById(studentId); // Validar que el estudiante existe
        List<Enrollment> enrollments = enrollmentService.getEnrollmentsByStudentId(studentId);
        return enrollments.stream()
            .filter(e -> "ACTIVE".equals(e.getStatus()))
            .map(e -> {
                Group group = groupService.getGroupById(e.getGroupId());
                return convertToGroupResponseDTO(group);
            })
            .toList();
    }

    private GroupResponseDTO convertToGroupResponseDTO(Group group) {
        GroupResponseDTO dto = new GroupResponseDTO();
        dto.setId(group.getId());
        dto.setSubjectId(group.getSubjectId());
        dto.setNumber(group.getNumber());
        dto.setCapacity(group.getCapacity());
        dto.setActive(group.isActive());
        if (group.getTeacher() != null) {
            dto.setTeacherId(group.getTeacher().getId());
            dto.setTeacherName(group.getTeacher().getName());
        }
        return dto;
    }

}
