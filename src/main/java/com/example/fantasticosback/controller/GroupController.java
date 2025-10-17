package com.example.fantasticosback.controller;

import com.example.fantasticosback.dto.request.CreateGroupRequestDTO;
import com.example.fantasticosback.dto.request.CreateSessionRequestDTO;
import com.example.fantasticosback.dto.response.GroupResponseDTO;
import com.example.fantasticosback.dto.response.SessionResponseDTO;
import com.example.fantasticosback.dto.response.GroupCapacityDTO;
import com.example.fantasticosback.dto.response.ResponseDTO;
import com.example.fantasticosback.dto.response.StudentDTO;
import com.example.fantasticosback.mapper.GroupMapper;
import com.example.fantasticosback.model.Document.Group;
import com.example.fantasticosback.model.Document.Student;
import com.example.fantasticosback.model.Document.Teacher;
import com.example.fantasticosback.service.GroupService;
import com.example.fantasticosback.service.StudentService;
import com.example.fantasticosback.service.TeacherService;
import com.example.fantasticosback.util.ClassSession;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "group-controller", description = "Gestión de Grupos y sus operaciones")
@RestController
@RequestMapping("/api/groups")
@CrossOrigin(origins = "*")
public class GroupController {

    private final GroupService groupService;
    private final StudentService studentService;
    private final TeacherService teacherService;

    public GroupController(GroupService groupService, StudentService studentService, TeacherService teacherService) {
        this.groupService = groupService;
        this.studentService = studentService;
        this.teacherService = teacherService;
    }

    @Operation(summary = "Crear un nuevo grupo",
            description = "Crea un nuevo grupo para una materia específica con capacidad definida")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Grupo creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos para la creación del grupo")
    })
    @PostMapping
    public ResponseEntity<ResponseDTO<GroupResponseDTO>> createGroup(@RequestBody CreateGroupRequestDTO dto) {
        Teacher teacher = null;
        if (dto.getTeacherId() != null && !dto.getTeacherId().isEmpty()) {
            teacher = teacherService.findById(dto.getTeacherId());
        }
        Group group = GroupMapper.toDomain(dto, teacher);
        Group createdGroup = groupService.createGroup(group);
        return ResponseEntity.ok(ResponseDTO.success(GroupMapper.toDTO(createdGroup), "Grupo creado exitosamente"));
    }

    @Operation(summary = "Asignar profesor a un grupo",
            description = "Asigna un profesor específico a un grupo existente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Profesor asignado exitosamente al grupo"),
        @ApiResponse(responseCode = "404", description = "Grupo o profesor no encontrado"),
        @ApiResponse(responseCode = "400", description = "El grupo ya tiene un profesor asignado")
    })
    @PutMapping("/{groupId}/teacher/{teacherId}")
    public ResponseEntity<ResponseDTO<GroupResponseDTO>> assignTeacher(
            @Parameter(description = "ID del grupo") @PathVariable String groupId,
            @Parameter(description = "ID del profesor") @PathVariable String teacherId) {
        Group updatedGroup = groupService.assignTeacher(groupId, teacherId);
        return ResponseEntity.ok(ResponseDTO.success(GroupMapper.toDTO(updatedGroup),
            "Profesor asignado exitosamente al grupo"));
    }

    @Operation(summary = "Agregar sesión de clase a un grupo",
            description = "Agrega una nueva sesión de clase al horario de un grupo específico")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Sesión agregada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Grupo no encontrado"),
        @ApiResponse(responseCode = "400", description = "Conflicto de horario o grupo sin profesor")
    })
    @PostMapping("/{groupId}/sessions")
    public ResponseEntity<ResponseDTO<GroupResponseDTO>> addSession(
            @Parameter(description = "ID del grupo") @PathVariable String groupId,
            @RequestBody CreateSessionRequestDTO dto) {
        ClassSession session = GroupMapper.sessionToDomain(dto);
        Group updatedGroup = groupService.addSession(groupId, session);
        return ResponseEntity.ok(ResponseDTO.success(GroupMapper.toDTO(updatedGroup),
            "Sesión agregada exitosamente al grupo"));
    }

    @Operation(summary = "Obtener horario de un grupo",
            description = "Obtiene todas las sesiones de clase programadas para un grupo específico")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Horario obtenido exitosamente"),
        @ApiResponse(responseCode = "404", description = "Grupo no encontrado")
    })
    @GetMapping("/{groupId}/schedule")
    public ResponseEntity<ResponseDTO<List<SessionResponseDTO>>> getGroupSchedule(
            @Parameter(description = "ID del grupo") @PathVariable String groupId) {
        List<ClassSession> sessions = groupService.getGroupSchedule(groupId);
        return ResponseEntity.ok(ResponseDTO.success(GroupMapper.sessionsToDTOList(sessions),
            "Horario del grupo obtenido exitosamente"));
    }

    @Operation(summary = "Inscribir estudiante a un grupo",
            description = "Inscribe un estudiante existente a un grupo específico")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Estudiante inscrito exitosamente"),
        @ApiResponse(responseCode = "404", description = "Grupo o estudiante no encontrado"),
        @ApiResponse(responseCode = "400", description = "Grupo sin capacidad o inactivo")
    })
    @PutMapping("/{groupId}/students")
    public ResponseEntity<ResponseDTO<String>> addStudent(
            @Parameter(description = "ID del grupo") @PathVariable String groupId,
            @RequestBody StudentDTO studentDTO) {
        Student student = studentService.convertToDomain(studentDTO);
        groupService.addStudentToGroup(groupId, student);
        return ResponseEntity.ok(ResponseDTO.success("Estudiante inscrito exitosamente",
            "El estudiante ha sido inscrito al grupo"));
    }

    @Operation(summary = "Retirar estudiante de un grupo",
            description = "Retira un estudiante específico de un grupo")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Estudiante retirado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Grupo o estudiante no encontrado")
    })
    @DeleteMapping("/{groupId}/students/{studentId}")
    public ResponseEntity<ResponseDTO<String>> removeStudent(
            @Parameter(description = "ID del grupo") @PathVariable String groupId,
            @Parameter(description = "ID del estudiante") @PathVariable String studentId) {
        Student student = studentService.findById(studentId);
        groupService.removeStudentFromGroup(groupId, student);
        return ResponseEntity.ok(ResponseDTO.success("Estudiante retirado exitosamente",
            "El estudiante ha sido retirado del grupo"));
    }

    @Operation(summary = "Obtener información de un grupo",
            description = "Obtiene la información completa de un grupo específico por su ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Información del grupo obtenida exitosamente"),
        @ApiResponse(responseCode = "404", description = "Grupo no encontrado")
    })
    @GetMapping("/{groupId}")
    public ResponseEntity<ResponseDTO<GroupResponseDTO>> getGroupById(
            @Parameter(description = "ID del grupo") @PathVariable String groupId) {
        Group group = groupService.getGroupById(groupId);
        return ResponseEntity.ok(ResponseDTO.success(GroupMapper.toDTO(group),
            "Información del grupo obtenida exitosamente"));
    }

    @Operation(summary = "Consultar capacidad de grupos por código de materia",
            description = "Obtiene información de capacidad (cupo máximo, estudiantes inscritos y porcentaje de ocupación) de todos los grupos de una materia específica")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Información de capacidad obtenida exitosamente"),
        @ApiResponse(responseCode = "404", description = "Materia no encontrada o sin grupos")
    })
    @GetMapping("/capacity/subject/{subjectCode}")
    public ResponseEntity<ResponseDTO<List<GroupCapacityDTO>>> getGroupCapacityBySubjectCode(
            @Parameter(description = "Código de la materia (ej: CALD)") @PathVariable String subjectCode) {
        List<GroupCapacityDTO> capacityInfo = groupService.getGroupCapacityBySubjectCode(subjectCode);
        return ResponseEntity.ok(ResponseDTO.success(capacityInfo,
            "Información de capacidad para la materia " + subjectCode + " obtenida exitosamente"));
    }
}
