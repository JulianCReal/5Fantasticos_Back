package com.example.fantasticosback.service;

import com.example.fantasticosback.dto.response.TeacherDTO;
import com.example.fantasticosback.exception.BusinessValidationException;
import com.example.fantasticosback.exception.ResourceNotFoundException;
import com.example.fantasticosback.model.Document.*;
import com.example.fantasticosback.repository.EnrollmentRepository;
import com.example.fantasticosback.repository.StudentRepository;
import com.example.fantasticosback.util.ClassSession;
import com.example.fantasticosback.repository.GroupRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupService {
    
    private final GroupRepository groupRepository;
    private final TeacherService teacherService;

    public GroupService(GroupRepository groupRepository, TeacherService teacherService) {
        this.groupRepository = groupRepository;
        this.teacherService = teacherService;
    }

    public Group createGroup(Group group) {
        validateGroupCreation(group);
        return groupRepository.save(group);
    }

    public Group getGroupById(String id) {
        return groupRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Group", "id", id));
    }

    public Group assignTeacher(String groupId, String teacherId) {
        Group group = getGroupById(groupId);
        Teacher teacher = teacherService.findById(teacherId);
        
        if (group.getTeacher() != null) {
            throw new BusinessValidationException("El grupo ya tiene un profesor asignado");
        }
        
        group.setTeacher(teacher);
        return groupRepository.save(group);
    }

    public Group addSession(String groupId, ClassSession session) {
        Group group = getGroupById(groupId);
        
        if (group.getTeacher() == null) {
            throw new BusinessValidationException("No se puede agregar horario sin profesor asignado");
        }
        
        // Validar que no haya conflictos de horario
        if (hasScheduleConflict(group, session)) {
            throw new BusinessValidationException("La sesión genera conflicto con horarios existentes");
        }
        
        group.getSessions().add(session);
        return groupRepository.save(group);
    }

    public void addStudentToGroup(String groupId, Student student) {
        Group group = getGroupById(groupId);
        
        if (group.getTeacher() == null) {
            throw new BusinessValidationException("No se puede inscribir estudiantes sin profesor asignado");
        }
        
        if (group.getGroupStudents().size() >= group.getCapacity()) {
            throw new BusinessValidationException("El grupo ha alcanzado su capacidad máxima");
        }
        
        if (!group.isActive()) {
            throw new BusinessValidationException("No se pueden inscribir estudiantes en un grupo inactivo");
        }
        
        group.getGroupStudents().add(student);
        groupRepository.save(group);
    }

    // Nuevo método: remover estudiante por studentId (busca por studentId en la lista)
    public void removeStudentFromGroup(String groupId, String studentId) {
        Group group = getGroupById(groupId);
        if (group.getGroupStudents() == null || group.getGroupStudents().isEmpty()) {
            throw new ResourceNotFoundException("Student", "id", studentId);
        }

        boolean removed = group.getGroupStudents().removeIf(s -> studentId != null && studentId.equals(s.getStudentId()));
        if (!removed) {
            throw new ResourceNotFoundException("Student", "id", studentId);
        }

        groupRepository.save(group);
    }

    public List<ClassSession> getGroupSchedule(String groupId) {
        Group group = getGroupById(groupId);
        return group.getSessions();
    }

    /**
     * Obtiene todos los estudiantes inscritos en un grupo específico.
     * Basado en la colección Enrollment.
     */
    public List<Student> getAllStudentsInGroup(String groupId,
                                               EnrollmentRepository enrollmentRepository,
                                               StudentRepository studentRepository) {
        List<Enrollment> enrollments = enrollmentRepository.findByGroupId(groupId);

        List<String> studentIds = enrollments.stream()
                .map(Enrollment::getStudentId)
                .distinct()
                .toList();

        return studentRepository.findAllById(studentIds);
    }


    private void validateGroupCreation(Group group) {
        if (group.getCapacity() <= 0) {
            throw new BusinessValidationException("La capacidad del grupo debe ser mayor a 0");
        }
    }

    private boolean hasScheduleConflict(Group group, ClassSession newSession) {
        return group.getSessions().stream()
            .anyMatch(existingSession -> existingSession.verifyConflict(newSession));
    }
}
