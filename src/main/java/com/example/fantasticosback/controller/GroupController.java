package com.example.fantasticosback.controller;

import com.example.fantasticosback.dto.request.CreateGroupRequestDTO;
import com.example.fantasticosback.dto.request.CreateSessionRequestDTO;
import com.example.fantasticosback.dto.response.GroupResponseDTO;
import com.example.fantasticosback.dto.response.SessionResponseDTO;
import com.example.fantasticosback.mapper.GroupMapper;
import com.example.fantasticosback.model.Document.Group;
import com.example.fantasticosback.model.Document.Student;
import com.example.fantasticosback.model.Document.Teacher;
import com.example.fantasticosback.service.GroupService;
import com.example.fantasticosback.service.StudentService;
import com.example.fantasticosback.service.TeacherService;
import com.example.fantasticosback.util.ClassSession;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
    name = "Grupos",
    description = "Gestión de grupos de clases: creación, asignación de profesores, gestión de sesiones y estudiantes"
)
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

    @Operation(
        summary = "Crear un nuevo grupo",
        description = "Crea un nuevo grupo de clase con capacidad, número de grupo y opcionalmente un profesor asignado"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Grupo creado exitosamente",
            content = @Content(schema = @Schema(implementation = GroupResponseDTO.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos inválidos o grupo ya existe"
        )
    })
    @PostMapping
    public ResponseEntity<GroupResponseDTO> createGroup(@RequestBody CreateGroupRequestDTO dto) {
        Teacher teacher = null;
        if (dto.getTeacherId() != null && !dto.getTeacherId().isEmpty()) {
            teacher = teacherService.findById(dto.getTeacherId());
        }
        Group group = GroupMapper.toDomain(dto, teacher);
        Group createdGroup = groupService.createGroup(group);
        return ResponseEntity.ok(GroupMapper.toDTO(createdGroup));
    }

    @Operation(
        summary = "Asignar profesor a un grupo",
        description = "Asigna un profesor específico a un grupo existente"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Profesor asignado exitosamente",
            content = @Content(schema = @Schema(implementation = GroupResponseDTO.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Grupo o profesor no encontrado"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "El grupo ya tiene un profesor asignado"
        )
    })
    @PutMapping("/{groupId}/teacher/{teacherId}")
    public ResponseEntity<GroupResponseDTO> assignTeacher(
            @Parameter(description = "ID del grupo", required = true) @PathVariable String groupId,
            @Parameter(description = "ID del profesor", required = true) @PathVariable String teacherId) {
        Group updatedGroup = groupService.assignTeacher(groupId, teacherId);
        return ResponseEntity.ok(GroupMapper.toDTO(updatedGroup));
    }

    @Operation(
        summary = "Agregar sesión de clase a un grupo",
        description = "Agrega una nueva sesión de clase (horario) al grupo especificando día, hora de inicio y fin, y salón"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Sesión agregada exitosamente",
            content = @Content(schema = @Schema(implementation = GroupResponseDTO.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Grupo no encontrado"
        ),
        @ApiResponse(
            responseCode = "409",
            description = "Conflicto de horario con otra sesión"
        )
    })
    @PostMapping("/{groupId}/sessions")
    public ResponseEntity<GroupResponseDTO> addSession(
            @Parameter(description = "ID del grupo", required = true) @PathVariable String groupId,
            @RequestBody CreateSessionRequestDTO dto) {
        ClassSession session = GroupMapper.sessionToDomain(dto);
        Group updatedGroup = groupService.addSession(groupId, session);
        return ResponseEntity.ok(GroupMapper.toDTO(updatedGroup));
    }

    @Operation(
        summary = "Obtener horario de un grupo",
        description = "Retorna todas las sesiones (horarios) configuradas para un grupo específico"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Horario obtenido exitosamente"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Grupo no encontrado"
        )
    })
    @GetMapping("/{groupId}/schedule")
    public ResponseEntity<List<SessionResponseDTO>> getGroupSchedule(
            @Parameter(description = "ID del grupo", required = true) @PathVariable String groupId) {
        List<ClassSession> sessions = groupService.getGroupSchedule(groupId);
        return ResponseEntity.ok(GroupMapper.sessionsToDTOList(sessions));
    }

    @Operation(
        summary = "Agregar estudiante a un grupo",
        description = "Inscribe un estudiante en un grupo específico si hay capacidad disponible"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Estudiante agregado exitosamente"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Grupo no encontrado"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Grupo sin capacidad disponible o estudiante ya inscrito"
        )
    })
    @PutMapping("/{groupId}/students")
    public ResponseEntity<?> addStudent(
            @Parameter(description = "ID del grupo", required = true) @PathVariable String groupId,
            @RequestBody Student student) {
        groupService.addStudentToGroup(groupId, student);
        return ResponseEntity.ok().build();
    }

    @Operation(
        summary = "Remover estudiante de un grupo",
        description = "Elimina un estudiante de un grupo específico"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Estudiante removido exitosamente"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Grupo o estudiante no encontrado"
        )
    })
    @DeleteMapping("/{groupId}/students/{studentId}")
    public ResponseEntity<?> removeStudent(
            @Parameter(description = "ID del grupo", required = true) @PathVariable String groupId,
            @Parameter(description = "ID del estudiante", required = true) @PathVariable String studentId) {
        Student student = studentService.findById(studentId);
        groupService.removeStudentFromGroup(groupId, student);
        return ResponseEntity.ok().build();
    }

    @Operation(
        summary = "Obtener información de un grupo",
        description = "Retorna la información completa de un grupo incluyendo profesor, estudiantes y sesiones"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Información del grupo obtenida exitosamente",
            content = @Content(schema = @Schema(implementation = GroupResponseDTO.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Grupo no encontrado"
        )
    })
    @GetMapping("/{groupId}")
    public ResponseEntity<GroupResponseDTO> getGroupById(
            @Parameter(description = "ID del grupo", required = true) @PathVariable String groupId) {
        Group group = groupService.getGroupById(groupId);
        return ResponseEntity.ok(GroupMapper.toDTO(group));
    }
}
