package com.example.fantasticosback.service;

import com.example.fantasticosback.dto.response.CreateGroupDTO;
import com.example.fantasticosback.dto.response.SubjectDTO;
import com.example.fantasticosback.repository.SubjectRepository;
import com.example.fantasticosback.repository.TeacherRepository;
import com.example.fantasticosback.model.Document.Subject;
import com.example.fantasticosback.model.Document.Group;
import com.example.fantasticosback.model.Document.Teacher;
import com.example.fantasticosback.util.SubjectCatalog;
import com.example.fantasticosback.util.ClassSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SubjectService {

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private StudentService studentService;

    public Subject save(Subject subject) { return subjectRepository.save(subject);}

    public List<Subject> findAll() {return subjectRepository.findAll();}

    public Subject findById(String id) {return subjectRepository.findById(id).orElse(null);}

    public Subject update(Subject subject) {return subjectRepository.save(subject);}

    public void delete(String id) {subjectRepository.deleteById(id);}

    public List<Subject> findByName(String name) {return subjectRepository.findByName(name);}

    public List<Subject> findBySemester(int semester) {return subjectRepository.findBySemester(semester);}

    public List<Subject> findByCredits(int credits) {return subjectRepository.findByCredits(credits);}

    public SubjectDTO toDTO(Subject subject) {
        return new SubjectDTO(
                String.valueOf(subject.getSubjectId()),
                subject.getName(),
                subject.getCredits(),
                subject.getSemester(),
                subject.getAvailableGroups()
        );
    }

    public Subject fromDTO(SubjectDTO dto) {
        Subject subject = new Subject(
                dto.getId(),
                dto.getName(),
                dto.getCredits(),
                dto.getSemester()
        );
        subject.setAvailableGroups(dto.getAvailableGroups());
        return subject;
    }

    public List<SubjectDTO> toDTOList(List<Subject> subjects) {
        return subjects.stream().map(this::toDTO).collect(Collectors.toList());
    }
    // Métodos para manejar grupos de materias

    /**
     * Agrega un grupo a una materia existente
     */
    public boolean addGroupToSubject(String subjectId, CreateGroupDTO groupDto) {
        Subject subject = findById(subjectId);
        if (subject == null) {
            throw new IllegalArgumentException("Subject not found with ID: " + subjectId);
        }

        // Buscar el profesor si se especificó
        Teacher teacher = null;
        if (groupDto.getTeacherId() != null && !groupDto.getTeacherId().isEmpty()) {
            teacher = teacherRepository.findById(groupDto.getTeacherId()).orElse(null);
            if (teacher == null) {
                throw new IllegalArgumentException("Teacher not found with ID: " + groupDto.getTeacherId());
            }
        }

        // Generar ID único para el grupo
        int newGroupId = generateGroupId(subject);

        // Crear el nuevo grupo (sin la referencia a subject)
        Group newGroup = new Group(
            newGroupId,
            groupDto.getNumber(),
            groupDto.getCapacity(),
            groupDto.isActive(),
            teacher
        );

        // Agregar el grupo a la materia
        subject.getAvailableGroups().add(newGroup);

        // Guardar la materia actualizada
        subjectRepository.save(subject);
        return true;
    }

    /**
     * Obtiene los grupos de una materia específica
     */
    public List<Object> getGroupsBySubject(String subjectId) {
        Subject subject = findById(subjectId);
        if (subject == null) {
            throw new IllegalArgumentException("Subject not found with ID: " + subjectId);
        }

        return subject.getAvailableGroups().stream()
                .map(group -> Map.of(
                    "groupId", group.getId(),
                    "groupNumber", group.getNumber(),
                    "capacity", group.getCapacity(),
                    "active", group.isActive(),
                    "enrolledStudents", group.getGroupStudents().size(),
                    "availableSpots", group.getCapacity() - group.getGroupStudents().size(),
                    "teacherName", group.getTeacher() != null ?
                        group.getTeacher().getName() + " " + group.getTeacher().getLastName() : "No asignado",
                    "sessions", group.getSessions().size() + " sesiones"
                ))
                .collect(Collectors.toList());
    }

    /**
     * Genera un ID único para un nuevo grupo dentro de una materia
     */
    private int generateGroupId(Subject subject) {
        if (subject.getAvailableGroups().isEmpty()) {
            return 1;
        }
        return subject.getAvailableGroups().stream()
                .mapToInt(Group::getId)
                .max()
                .orElse(0) + 1;
    }

    // Métodos para manejar grupos de materias usando abreviaturas del catálogo

    /**
     * Agrega un grupo a una materia existente usando la abreviatura del catálogo
     */
    public boolean addGroupToSubjectByCode(String subjectCode, CreateGroupDTO groupDto) {
        // Obtener la materia del catálogo usando la abreviatura
        Subject catalogSubject = SubjectCatalog.getSubject(subjectCode);
        if (catalogSubject == null) {
            throw new IllegalArgumentException("Subject not found in catalog with code: " + subjectCode);
        }

        // Buscar la materia en la BD usando el ID del catálogo
        Subject subject = findById(catalogSubject.getSubjectId());
        if (subject == null) {
            throw new IllegalArgumentException("Subject not found in database with code: " + subjectCode);
        }

        // Buscar el profesor si se especificó
        Teacher teacher = null;
        if (groupDto.getTeacherId() != null && !groupDto.getTeacherId().isEmpty()) {
            teacher = teacherRepository.findById(groupDto.getTeacherId()).orElse(null);
            if (teacher == null) {
                throw new IllegalArgumentException("Teacher not found with ID: " + groupDto.getTeacherId());
            }
        }

        // Generar ID único para el grupo
        int newGroupId = generateGroupId(subject);

        // Crear el nuevo grupo (sin la referencia a subject)
        Group newGroup = new Group(
            newGroupId,
            groupDto.getNumber(),
            groupDto.getCapacity(),
            groupDto.isActive(),
            teacher
        );

        // Agregar el grupo a la materia
        subject.getAvailableGroups().add(newGroup);

        // Guardar la materia actualizada
        subjectRepository.save(subject);
        return true;
    }

    /**
     * Obtiene los grupos de una materia específica usando la abreviatura del catálogo
     */
    public List<Object> getGroupsBySubjectCode(String subjectCode) {
        // Obtener la materia del catálogo usando la abreviatura
        Subject catalogSubject = SubjectCatalog.getSubject(subjectCode);
        if (catalogSubject == null) {
            throw new IllegalArgumentException("Subject not found in catalog with code: " + subjectCode);
        }

        // Buscar la materia en la BD usando el ID del catálogo
        Subject subject = findById(catalogSubject.getSubjectId());
        if (subject == null) {
            throw new IllegalArgumentException("Subject not found in database with code: " + subjectCode);
        }

        return subject.getAvailableGroups().stream()
                .map(group -> Map.of(
                    "groupId", group.getId(),
                    "groupNumber", group.getNumber(),
                    "capacity", group.getCapacity(),
                    "active", group.isActive(),
                    "enrolledStudents", group.getGroupStudents().size(),
                    "availableSpots", group.getCapacity() - group.getGroupStudents().size(),
                    "teacherName", group.getTeacher() != null ?
                        group.getTeacher().getName() + " " + group.getTeacher().getLastName() : "No asignado",
                    "sessions", group.getSessions().size() + " sesiones",
                    "subjectCode", subjectCode,
                    "subjectName", subject.getName()
                ))
                .collect(Collectors.toList());
    }

    /**
     * Añade una sesión de clase a un grupo específico de una materia
     */
    public boolean addSessionToGroup(String subjectCode, int groupId, ClassSession session) {
        // Obtener la materia del catálogo usando la abreviatura
        Subject catalogSubject = SubjectCatalog.getSubject(subjectCode);
        if (catalogSubject == null) {
            throw new IllegalArgumentException("Subject not found in catalog with code: " + subjectCode);
        }

        // Buscar la materia en la BD usando el ID del catálogo
        Subject subject = findById(catalogSubject.getSubjectId());
        if (subject == null) {
            throw new IllegalArgumentException("Subject not found in database with code: " + subjectCode);
        }

        // Buscar el grupo específico
        Group targetGroup = subject.getAvailableGroups().stream()
                .filter(group -> group.getId() == groupId)
                .findFirst()
                .orElse(null);

        if (targetGroup == null) {
            throw new IllegalArgumentException("Group not found with ID: " + groupId + " in subject: " + subjectCode);
        }

        // Verificar conflictos de horario dentro del mismo grupo
        for (ClassSession existingSession : targetGroup.getSessions()) {
            if (existingSession.verifyConflict(session)) {
                throw new IllegalArgumentException("Schedule conflict detected. Session overlaps with existing session on " +
                    existingSession.getDay() + " from " + existingSession.getStartTime() + " to " + existingSession.getEndTime());
            }
        }

        // Añadir la sesión al grupo
        targetGroup.addSession(session);

        // Guardar la materia actualizada
        subjectRepository.save(subject);
        return true;
    }

    /**
     * Obtiene las sesiones de un grupo específico
     */
    public List<ClassSession> getGroupSessions(String subjectCode, int groupId) {
        // Obtener la materia del catálogo usando la abreviatura
        Subject catalogSubject = SubjectCatalog.getSubject(subjectCode);
        if (catalogSubject == null) {
            throw new IllegalArgumentException("Subject not found in catalog with code: " + subjectCode);
        }

        // Buscar la materia en la BD usando el ID del catálogo
        Subject subject = findById(catalogSubject.getSubjectId());
        if (subject == null) {
            throw new IllegalArgumentException("Subject not found in database with code: " + subjectCode);
        }

        // Buscar el grupo específico
        Group targetGroup = subject.getAvailableGroups().stream()
                .filter(group -> group.getId() == groupId)
                .findFirst()
                .orElse(null);

        if (targetGroup == null) {
            throw new IllegalArgumentException("Group not found with ID: " + groupId + " in subject: " + subjectCode);
        }

        return targetGroup.getSessions();
    }

    /**
     * Elimina una sesión específica de un grupo
     */
    public boolean removeSessionFromGroup(String subjectCode, int groupId, int sessionIndex) {
        // Obtener la materia del catálogo usando la abreviatura
        Subject catalogSubject = SubjectCatalog.getSubject(subjectCode);
        if (catalogSubject == null) {
            throw new IllegalArgumentException("Subject not found in catalog with code: " + subjectCode);
        }

        // Buscar la materia en la BD usando el ID del catálogo
        Subject subject = findById(catalogSubject.getSubjectId());
        if (subject == null) {
            throw new IllegalArgumentException("Subject not found in database with code: " + subjectCode);
        }

        // Buscar el grupo específico
        Group targetGroup = subject.getAvailableGroups().stream()
                .filter(group -> group.getId() == groupId)
                .findFirst()
                .orElse(null);

        if (targetGroup == null) {
            throw new IllegalArgumentException("Group not found with ID: " + groupId + " in subject: " + subjectCode);
        }

        if (sessionIndex < 0 || sessionIndex >= targetGroup.getSessions().size()) {
            throw new IllegalArgumentException("Invalid session index: " + sessionIndex);
        }

        // Eliminar la sesión
        targetGroup.getSessions().remove(sessionIndex);

        // Guardar la materia actualizada
        subjectRepository.save(subject);
        return true;
    }

}
