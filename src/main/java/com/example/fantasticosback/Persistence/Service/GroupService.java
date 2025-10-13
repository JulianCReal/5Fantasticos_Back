package com.example.fantasticosback.Persistence.Service;

import com.example.fantasticosback.Exception.BusinessValidationException;
import com.example.fantasticosback.Exception.ResourceNotFoundException;
import com.example.fantasticosback.Model.Document.Group;
import com.example.fantasticosback.Model.Document.Teacher;
import com.example.fantasticosback.Model.Document.Student;
import com.example.fantasticosback.util.ClassSession;
import com.example.fantasticosback.Persistence.Repository.GroupRepository;
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

    public Group getGroupById(int groupId) {
        return groupRepository.findById(groupId)
            .orElseThrow(() -> new ResourceNotFoundException("Group", "id", groupId));
    }

    public Group assignTeacher(int groupId, String teacherId) {
        Group group = getGroupById(groupId);
        Teacher teacher = teacherService.findById(teacherId);
        
        if (group.getTeacher() != null) {
            throw new BusinessValidationException("El grupo ya tiene un profesor asignado");
        }
        
        group.setTeacher(teacher);
        return groupRepository.save(group);
    }

    public Group addSession(int groupId, ClassSession session) {
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

    public void addStudentToGroup(int groupId, Student student) {
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

    public void removeStudentFromGroup(int groupId, Student student) {
        Group group = getGroupById(groupId);
        group.getGroupStudents().remove(student);
        groupRepository.save(group);
    }

    public List<ClassSession> getGroupSchedule(int groupId) {
        Group group = getGroupById(groupId);
        return group.getSessions();
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
