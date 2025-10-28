package com.example.fantasticosback.mapper;

import com.example.fantasticosback.dto.request.CreateGroupRequestDTO;
import com.example.fantasticosback.dto.request.CreateSessionRequestDTO;
import com.example.fantasticosback.dto.response.GroupResponseDTO;
import com.example.fantasticosback.dto.response.SessionResponseDTO;
import com.example.fantasticosback.model.Document.Group;
import com.example.fantasticosback.model.Document.Student;
import com.example.fantasticosback.model.Document.Teacher;
import com.example.fantasticosback.util.ClassSession;

import java.util.ArrayList;
import java.util.List;

public class GroupMapper {

    private GroupMapper() {
        // Constructor privado para evitar instanciación
    }

    public static GroupResponseDTO toDTO(Group group) {
        List<String> studentIds = group.getGroupStudents() != null
            ? group.getGroupStudents().stream()
                .map(Student::getStudentId)
                .toList()
            : new ArrayList<>();

        ArrayList<ClassSession> sessions = group.getSessions();

        String teacherId = group.getTeacher() != null ? group.getTeacher().getId() : null;
        String teacherName = group.getTeacher() != null ? group.getTeacher().getName() : null;

        return new GroupResponseDTO(
            group.getId(),
            group.getSubjectId(),
            group.getNumber(),
            group.getCapacity(),
            group.isActive(),
            teacherId,
            teacherName,
            studentIds,
            sessions
        );
    }

    public static List<GroupResponseDTO> toDTOList(List<Group> groups) {
        return groups.stream()
            .map(GroupMapper::toDTO)
            .toList();
    }

    public static Group toDomain(CreateGroupRequestDTO dto, Teacher teacher) {
        return new Group(
            dto.getId(),
            dto.getSubjectId(),
            dto.getNumber(),
            dto.getCapacity(),
            dto.isActive(),
            teacher,
            new ArrayList<>(),
            new ArrayList<>()
        );
    }

    public static SessionResponseDTO sessionToDTO(ClassSession session) {
        return new SessionResponseDTO(
            session.getDay(),
            session.getStartTime(),
            session.getEndTime(),
            session.getClassroom()
        );
    }

    public static ClassSession sessionToDomain(CreateSessionRequestDTO dto) {
        return new ClassSession(
            dto.getDay(),
            dto.getStartTime(),
            dto.getEndTime(),
            dto.getClassroom()
        );
    }

    public static List<SessionResponseDTO> sessionsToDTOList(List<ClassSession> sessions) {
        return sessions.stream()
            .map(GroupMapper::sessionToDTO)
            .toList();
    }
}
