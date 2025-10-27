package com.example.fantasticosback.service;

import com.example.fantasticosback.dto.response.CreateGroupDTO;
import com.example.fantasticosback.dto.response.SubjectDTO;
import com.example.fantasticosback.exception.BusinessValidationException;
import com.example.fantasticosback.exception.ResourceNotFoundException;
import com.example.fantasticosback.repository.SubjectRepository;
import com.example.fantasticosback.repository.TeacherRepository;
import com.example.fantasticosback.repository.GroupRepository;
import com.example.fantasticosback.model.Document.Subject;
import com.example.fantasticosback.model.Document.Group;
import com.example.fantasticosback.model.Document.Teacher;
import com.example.fantasticosback.util.ClassSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubjectService {
    private final SubjectRepository subjectRepository;
    private final TeacherRepository teacherRepository;
    private final GroupRepository groupRepository;

    public List<Subject> findAll() {
        return subjectRepository.findAll();
    }

    public Subject findById(String code) {
        return subjectRepository.findById(code).orElse(null);
    }

    public List<Subject> findBySemester(int semester) {
        return subjectRepository.findBySemester(semester);
    }

    public List<Subject> findByName(String name) {
        return subjectRepository.findByName(name);
    }

    public SubjectDTO toDTO(Subject subject) {
        if (subject == null) return null;
        return new SubjectDTO(
                subject.getCode(),
                subject.getName(),
                subject.getCredits(),
                subject.getSemester()
        );
    }

    public Subject fromDTO(SubjectDTO dto) {
        Subject subject = new Subject();
        subject.setCode(dto.getCode());
        subject.setName(dto.getName());
        subject.setCredits(dto.getCredits());
        subject.setSemester(dto.getSemester());
        return subject;
    }

    public SubjectDTO createSubject(SubjectDTO dto) {
        if (dto == null) throw new BusinessValidationException("Subject payload is null");
        if (dto.getCode() == null || dto.getCode().isEmpty()) {
            throw new BusinessValidationException("Subject code is required");
        }

        // Verificar duplicado por código
        Optional<Subject> existing = subjectRepository.findByCode(dto.getCode());
        if (existing.isPresent()) {
            throw new BusinessValidationException("Subject with code already exists: " + dto.getCode());
        }

        Subject subject = fromDTO(dto);
        Subject saved = subjectRepository.save(subject);
        return toDTO(saved);
    }

    public List<SubjectDTO> toDTOList(List<Subject> subjects) {
        return subjects.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public boolean addGroupToSubject(String subjectCode, CreateGroupDTO groupDto) {
        Subject subject = subjectRepository.findById(subjectCode).orElse(null);
        if (subject == null) {
            throw new ResourceNotFoundException("Subject", "code", subjectCode);
        }

        Teacher teacher = null;
        if (groupDto.getTeacherId() != null && !groupDto.getTeacherId().isEmpty()) {
            teacher = teacherRepository.findById(groupDto.getTeacherId()).orElse(null);
            if (teacher == null) {
                throw new ResourceNotFoundException("Teacher", "id", groupDto.getTeacherId());
            }
        }

        String newGroupId = UUID.randomUUID().toString();

        Group newGroup = new Group(
            newGroupId,
            subject.getCode(), // Usar code en lugar de subjectId
            groupDto.getNumber(),
            groupDto.getCapacity(),
            groupDto.isActive(),
            teacher,
            new ArrayList<>(),
            new ArrayList<>()
        );

        groupRepository.save(newGroup);
        return true;
    }

    public List<Object> getGroupsBySubject(String subjectCode) {
        Subject subject = subjectRepository.findById(subjectCode).orElse(null);
        if (subject == null) {
            throw new ResourceNotFoundException("Subject", "code", subjectCode);
        }

        List<Group> groups = groupRepository.findBySubjectId(subjectCode);

        return groups.stream()
                .map(group -> Map.of(
                    "groupId", group.getId(),
                    "groupNumber", group.getNumber(),
                    "capacity", group.getCapacity(),
                    "active", group.isActive(),
                    "enrolledStudents", group.getGroupStudents() != null ? group.getGroupStudents().size() : 0,
                    "availableSpots", group.getCapacity() - (group.getGroupStudents() != null ? group.getGroupStudents().size() : 0),
                    "teacherName", group.getTeacher() != null ?
                        group.getTeacher().getName() + " " + group.getTeacher().getLastName() : "No asignado",
                    "sessions", group.getSessions().size() + " sesiones"
                ))
                .collect(Collectors.toList());
    }

    public boolean addGroupToSubjectByCode(String subjectCode, CreateGroupDTO groupDto) {
        Optional<Subject> subjectOpt = subjectRepository.findById(subjectCode);
        if (subjectOpt.isEmpty()) {
            throw new ResourceNotFoundException("Subject", "code", subjectCode);
        }

        Subject subject = subjectOpt.get();

        Teacher teacher = null;
        if (groupDto.getTeacherId() != null && !groupDto.getTeacherId().isEmpty()) {
            teacher = teacherRepository.findById(groupDto.getTeacherId())
                    .orElseThrow(() -> new ResourceNotFoundException("Teacher", "id", groupDto.getTeacherId()));
        }

        Group newGroup = new Group(
                UUID.randomUUID().toString(),
                subject.getCode(), // Usar code
                groupDto.getNumber(),
                groupDto.getCapacity(),
                groupDto.isActive(),
                teacher,
                new ArrayList<>(),
                new ArrayList<>()
        );

        groupRepository.save(newGroup);
        return true;
    }

    public List<Object> getGroupsBySubjectCode(String subjectCode) {
         Optional<Subject> catalogSubjectOpt = subjectRepository.findById(subjectCode);
         if (catalogSubjectOpt.isEmpty()) {
             throw new ResourceNotFoundException("Subject", "code", subjectCode);
         }

         Subject subject = catalogSubjectOpt.get();

         List<Group> groups = groupRepository.findBySubjectId(subject.getCode());

         return groups.stream()
                 .map(group -> Map.of(
                     "groupId", group.getId(),
                     "groupNumber", group.getNumber(),
                     "capacity", group.getCapacity(),
                     "active", group.isActive(),
                     "enrolledStudents", group.getGroupStudents() != null ? group.getGroupStudents().size() : 0,
                     "availableSpots", group.getCapacity() - (group.getGroupStudents() != null ? group.getGroupStudents().size() : 0),
                     "teacherName", group.getTeacher() != null ?
                         group.getTeacher().getName() + " " + group.getTeacher().getLastName() : "No asignado",
                     "sessions", group.getSessions().size() + " sesiones",
                     "subjectCode", subjectCode,
                     "subjectName", subject.getName()
                 ))
                 .collect(Collectors.toList());
     }

     public boolean addSessionToGroup(String subjectCode, String groupId, ClassSession session) {
         // Implementar método faltante
         return true;
     }

     public List<ClassSession> getGroupSessions(String subjectCode, String groupId) {
         // Implementar método faltante
         return new ArrayList<>();
     }

     public boolean removeSessionFromGroup(String subjectCode, String groupId, int sessionIndex) {
         // Implementar método faltante
         return true;
     }
}