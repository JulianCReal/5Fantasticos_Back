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

    public Subject findById(String id) {
        return subjectRepository.findById(id).orElse(null);
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
                subject.getSubjectId(),
                subject.getCode(),
                subject.getName(),
                subject.getCredits(),
                subject.getSemester()
        );
    }

    public Subject fromDTO(SubjectDTO dto) {
        Subject subject = new Subject();
        subject.setSubjectId(dto.getId());
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
        if (existing != null && existing.isPresent()) {
            throw new BusinessValidationException("Subject with code already exists: " + dto.getCode());
        }

        Subject subject = fromDTO(dto);
        // Generar ID si no viene en el DTO
        if (subject.getSubjectId() == null || subject.getSubjectId().isEmpty()) {
            subject.setSubjectId(UUID.randomUUID().toString());
        }

        // Guardar directamente usando el repositorio (no usar métodos de servicio auxiliares)
        Subject saved = subjectRepository.save(subject);
        return toDTO(saved);
    }

    public List<SubjectDTO> toDTOList(List<Subject> subjects) {
        return subjects.stream().map(this::toDTO).collect(Collectors.toList());
    }
    // Métodos para manejar grupos de materias

    /**
     * Agrega un grupo a una materia existente
     */
    public boolean addGroupToSubject(String subjectId, CreateGroupDTO groupDto) {
        // Buscar la materia directamente en el repositorio
        Subject subject = subjectRepository.findById(subjectId).orElse(null);
        if (subject == null) {
            throw new ResourceNotFoundException("Subject", "id", subjectId);
        }

        // Buscar el profesor si se especificó
        Teacher teacher = null;
        if (groupDto.getTeacherId() != null && !groupDto.getTeacherId().isEmpty()) {
            teacher = teacherRepository.findById(groupDto.getTeacherId()).orElse(null);
            if (teacher == null) {
                throw new ResourceNotFoundException("Teacher", "id", groupDto.getTeacherId());
            }
        }

        // Generar ID único para el grupo usando UUID (IDs deben ser Strings)
        String newGroupId = UUID.randomUUID().toString();

        // Crear el nuevo grupo (sin la referencia a subject)
        Group newGroup = new Group(
            newGroupId,
            subject.getSubjectId(),
            groupDto.getNumber(),
            groupDto.getCapacity(),
            groupDto.isActive(),
            teacher,
            new java.util.ArrayList<>(),
            new java.util.ArrayList<>()
        );

        // Guardar el grupo directamente en su colección
        groupRepository.save(newGroup);
        return true;
    }

    /**
     * Obtiene los grupos de una materia específica
     */
    public List<Object> getGroupsBySubject(String subjectId) {
        Subject subject = subjectRepository.findById(subjectId).orElse(null);
        if (subject == null) {
            throw new ResourceNotFoundException("Subject", "id", subjectId);
        }

        List<Group> groups = groupRepository.findBySubjectId(subjectId);

        return groups.stream()
                .map(group -> Map.of(
                    "groupId", group.getId(),
                    "groupNumber", group.getNumber(),
                    "capacity", group.getCapacity(),
                    "active", group.isActive(),
                    // proteger contra listas null
                    "enrolledStudents", group.getGroupStudents() != null ? group.getGroupStudents().size() : 0,
                    "availableSpots", group.getCapacity() - (group.getGroupStudents() != null ? group.getGroupStudents().size() : 0),
                    "teacherName", group.getTeacher() != null ?
                        group.getTeacher().getName() + " " + group.getTeacher().getLastName() : "No asignado",
                    "sessions", group.getSessions().size() + " sesiones"
                ))
                .collect(Collectors.toList());
    }

    /**
     * Agrega un grupo a una materia existente usando el código (BD)
     */
    public boolean addGroupToSubjectByCode(String subjectCode, CreateGroupDTO groupDto) {
        Optional<Subject> subjectOpt = subjectRepository.findByCode(subjectCode);
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
                subject.getSubjectId(),
                groupDto.getNumber(),
                groupDto.getCapacity(),
                groupDto.isActive(),
                teacher,
                new ArrayList<>(),
                new ArrayList<>()
        );

        // Guardar el grupo directamente en su colección
        groupRepository.save(newGroup);
        return true;
    }

    /**
     * Obtiene los grupos de una materia específica usando el código de materia
     */
    public List<Object> getGroupsBySubjectCode(String subjectCode) {
         Optional<Subject> catalogSubjectOpt = subjectRepository.findByCode(subjectCode);
         if (catalogSubjectOpt.isEmpty()) {
             throw new ResourceNotFoundException("Subject", "code", subjectCode);
         }

         Subject subject = catalogSubjectOpt.get();

         List<Group> groups = groupRepository.findBySubjectId(subject.getSubjectId());

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

     /**
      * Añade una sesión de clase a un grupo específico de una materia (por código)
      */
     public boolean addSessionToGroup(String subjectCode, String groupId, ClassSession session) {
         Optional<Subject> catalogSubjectOpt = subjectRepository.findByCode(subjectCode);
         if (catalogSubjectOpt.isEmpty()) {
             throw new ResourceNotFoundException("Subject", "code", subjectCode);
         }

         Subject subject = catalogSubjectOpt.get();

         Optional<Group> groupOpt = groupRepository.findById(groupId);
         if (groupOpt.isEmpty()) {
             throw new ResourceNotFoundException("Group", "id", groupId);
         }

         Group targetGroup = groupOpt.get();

         // Verificar que el grupo pertenece a la materia
         if (!targetGroup.getSubjectId().equals(subject.getSubjectId())) {
             throw new BusinessValidationException("Group does not belong to the specified subject");
         }

         // Verificar conflictos de horario dentro del mismo grupo
         for (ClassSession existingSession : targetGroup.getSessions()) {
             if (existingSession.verifyConflict(session)) {
                 throw new BusinessValidationException("Schedule conflict detected. Session overlaps with existing session on " +
                     existingSession.getDay() + " from " + existingSession.getStartTime() + " to " + existingSession.getEndTime());
             }
         }

         // Añadir la sesión al grupo (Group no expone addSession, accedemos a la lista)
         targetGroup.getSessions().add(session);

         // Guardar el grupo actualizado
         groupRepository.save(targetGroup);
         return true;
     }

     /**
      * Obtiene las sesiones de un grupo específico (por código)
      */
     public List<ClassSession> getGroupSessions(String subjectCode, String groupId) {
         Optional<Subject> catalogSubjectOpt = subjectRepository.findByCode(subjectCode);
         if (catalogSubjectOpt.isEmpty()) {
             throw new ResourceNotFoundException("Subject", "code", subjectCode);
         }

         Subject subject = catalogSubjectOpt.get();

         Optional<Group> groupOpt = groupRepository.findById(groupId);
         if (groupOpt.isEmpty()) {
             throw new ResourceNotFoundException("Group", "id", groupId);
         }

         Group targetGroup = groupOpt.get();

         // Verificar que el grupo pertenece a la materia
         if (!targetGroup.getSubjectId().equals(subject.getSubjectId())) {
             throw new BusinessValidationException("Group does not belong to the specified subject");
         }

         return targetGroup.getSessions();
     }

     /**
      * Elimina una sesión específica de un grupo (por código)
      */
     public boolean removeSessionFromGroup(String subjectCode, String groupId, int sessionIndex) {
         Optional<Subject> catalogSubjectOpt = subjectRepository.findByCode(subjectCode);
         if (catalogSubjectOpt.isEmpty()) {
             throw new ResourceNotFoundException("Subject", "code", subjectCode);
         }

         Subject subject = catalogSubjectOpt.get();

         Optional<Group> groupOpt = groupRepository.findById(groupId);
         if (groupOpt.isEmpty()) {
             throw new ResourceNotFoundException("Group", "id", groupId);
         }

         Group targetGroup = groupOpt.get();

         // Verificar que el grupo pertenece a la materia
         if (!targetGroup.getSubjectId().equals(subject.getSubjectId())) {
             throw new BusinessValidationException("Group does not belong to the specified subject");
         }

         if (sessionIndex < 0 || sessionIndex >= targetGroup.getSessions().size()) {
             throw new BusinessValidationException("Invalid session index: " + sessionIndex);
         }

         // Eliminar la sesión
         targetGroup.getSessions().remove(sessionIndex);

         // Guardar el grupo actualizado
         groupRepository.save(targetGroup);
         return true;
     }

 }
