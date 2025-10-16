package com.example.fantasticosback.service;

import com.example.fantasticosback.exception.BusinessValidationException;
import com.example.fantasticosback.exception.ResourceNotFoundException;
import com.example.fantasticosback.model.Document.Group;
import com.example.fantasticosback.model.Document.Teacher;
import com.example.fantasticosback.model.Document.Student;
import com.example.fantasticosback.model.Document.Semester;
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

    public void removeStudentFromGroup(String groupId, Student student) {
        Group group = getGroupById(groupId);
        group.getGroupStudents().remove(student);
        groupRepository.save(group);
    }

    public List<ClassSession> getGroupSchedule(String groupId) {
        Group group = getGroupById(groupId);
        return group.getSessions();
    }

    /**
     * Obtiene todos los estudiantes inscritos en un grupo específico
     */
    public List<Student> getAllStudentsInGroup(String groupId, List<Student> allStudents) {
        return allStudents.stream()
                .filter(student -> {
                    List<Semester> semesters = student.getSemesters();
                    if (semesters.isEmpty()) {
                        return false;
                    }
                    Semester currentSemester = semesters.get(semesters.size() - 1);
                    return currentSemester.getSubjects().stream()
                            .anyMatch(enrollment -> enrollment.getGroup().getId() == groupId);
                })
                .toList();
    }

    private void validateGroupCreation(Group group) {
        if (group.getCapacity() <= 0) {
            throw new BusinessValidationException("La capacidad del grupo debe ser mayor a 0");
        }
        if (groupRepository.existsByNumber(group.getNumber())) {
            throw new BusinessValidationException("Ya existe un grupo con este número");
        }
    }

    private boolean hasScheduleConflict(Group group, ClassSession newSession) {
        return group.getSessions().stream()
            .anyMatch(existingSession -> existingSession.verifyConflict(newSession));
    }
}
