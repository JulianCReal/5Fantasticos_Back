package com.example.fantasticosback.controller;

import com.example.fantasticosback.dto.response.CreateGroupDTO;
import com.example.fantasticosback.dto.response.ResponseDTO;
import com.example.fantasticosback.dto.response.SubjectDTO;
import com.example.fantasticosback.service.SubjectService;
import com.example.fantasticosback.model.Document.Subject;
import com.example.fantasticosback.util.ClassSession;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
    name = "Materias",
    description = "Gestión de materias académicas: consulta, grupos, sesiones y horarios"
)
@RestController
@RequestMapping("/api/subjects")
public class SubjectController {
    @Autowired
    private SubjectService subjectService;

    @Operation(
        summary = "Listar todas las materias",
        description = "Obtiene la lista completa de materias predefinidas en el sistema"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Lista de materias obtenida exitosamente"
    )
    @GetMapping
    public ResponseEntity<ResponseDTO<List<SubjectDTO>>> list() {
        List<Subject> subjects = subjectService.findAll();
        return ResponseEntity.ok(ResponseDTO.success(subjectService.toDTOList(subjects), "List of predefined subjects"));
    }

    @Operation(
        summary = "Obtener materia por ID",
        description = "Obtiene los detalles de una materia específica por su identificador único"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Materia encontrada",
            content = @Content(schema = @Schema(implementation = SubjectDTO.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Materia no encontrada"
        )
    })
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<SubjectDTO>> get(
            @Parameter(description = "ID de la materia", required = true) @PathVariable String id) {
        Subject subject = subjectService.findById(id);
        if (subject != null) {
            return ResponseEntity.ok(ResponseDTO.success(subjectService.toDTO(subject), "Subject found"));
        }
        return ResponseEntity.status(404).body(ResponseDTO.error("Subject not found"));
    }

    @Operation(
        summary = "Buscar materias por semestre",
        description = "Obtiene todas las materias correspondientes a un semestre académico específico (1-10)"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Materias del semestre obtenidas exitosamente"
    )
    @GetMapping("/semester/{semester}")
    public ResponseEntity<ResponseDTO<List<SubjectDTO>>> getBySemester(
            @Parameter(description = "Número de semestre (1-10)", required = true, example = "5") @PathVariable int semester) {
        List<Subject> subjects = subjectService.findBySemester(semester);
        return ResponseEntity.ok(ResponseDTO.success(subjectService.toDTOList(subjects), "Subjects by semester"));
    }

    @Operation(
        summary = "Buscar materias por nombre",
        description = "Busca materias que coincidan con el nombre especificado (búsqueda parcial)"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Materias encontradas"
    )
    @GetMapping("/name/{name}")
    public ResponseEntity<ResponseDTO<List<SubjectDTO>>> getByName(
            @Parameter(description = "Nombre de la materia", required = true) @PathVariable String name) {
        List<Subject> subjects = subjectService.findByName(name);
        return ResponseEntity.ok(ResponseDTO.success(subjectService.toDTOList(subjects), "Subjects by name"));
    }

    @Operation(
        summary = "Agregar grupo a una materia",
        description = "Crea y asocia un nuevo grupo a una materia existente usando su código/abreviatura"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Grupo agregado exitosamente a la materia"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Error al agregar el grupo o código de materia inválido"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Materia no encontrada"
        )
    })
    @PostMapping("/{subjectCode}/groups")
    public ResponseEntity<ResponseDTO<String>> addGroupToSubject(
            @Parameter(description = "Código de la materia (ej: DOSW, ARQO)", required = true) @PathVariable String subjectCode,
            @RequestBody CreateGroupDTO groupDto) {
        boolean success = subjectService.addGroupToSubjectByCode(subjectCode, groupDto);
        if (success) {
            return ResponseEntity.ok(ResponseDTO.success("Group added successfully", "Group added to subject " + subjectCode));
        } else {
            return ResponseEntity.badRequest().body(ResponseDTO.error("Failed to add group to subject"));
        }
    }

    @Operation(
        summary = "Obtener grupos de una materia",
        description = "Retorna todos los grupos asociados a una materia específica mediante su código"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Grupos obtenidos exitosamente"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Materia no encontrada"
        )
    })
    @GetMapping("/{subjectCode}/groups")
    public ResponseEntity<ResponseDTO<List<Object>>> getSubjectGroups(
            @Parameter(description = "Código de la materia", required = true) @PathVariable String subjectCode) {
        List<Object> groups = subjectService.getGroupsBySubjectCode(subjectCode);
        return ResponseEntity.ok(ResponseDTO.success(groups, "Groups retrieved for " + subjectCode));
    }

    @Operation(
        summary = "Agregar sesión de clase a un grupo",
        description = "Añade una nueva sesión de clase (horario) a un grupo específico de una materia"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Sesión agregada exitosamente"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Error al agregar la sesión o conflicto de horarios"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Materia o grupo no encontrado"
        )
    })
    @PostMapping("/{subjectCode}/groups/{groupId}/sessions")
    public ResponseEntity<ResponseDTO<String>> addSessionToGroup(
            @Parameter(description = "Código de la materia", required = true) @PathVariable String subjectCode,
            @Parameter(description = "ID del grupo", required = true) @PathVariable int groupId,
            @RequestBody ClassSession session) {
        boolean success = subjectService.addSessionToGroup(subjectCode, groupId, session);
        if (success) {
            return ResponseEntity.ok(ResponseDTO.success("Session added successfully",
                    "Session added to group " + groupId + " in subject " + subjectCode));
        } else {
            return ResponseEntity.badRequest().body(ResponseDTO.error("Failed to add session to group"));
        }
    }

    @Operation(
        summary = "Obtener sesiones de un grupo",
        description = "Retorna todas las sesiones de clase (horarios) configuradas para un grupo específico"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Sesiones obtenidas exitosamente"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Materia o grupo no encontrado"
        )
    })
    @GetMapping("/{subjectCode}/groups/{groupId}/sessions")
    public ResponseEntity<ResponseDTO<List<ClassSession>>> getGroupSessions(
            @Parameter(description = "Código de la materia", required = true) @PathVariable String subjectCode,
            @Parameter(description = "ID del grupo", required = true) @PathVariable String groupId) {
        List<ClassSession> sessions = subjectService.getGroupSessions(subjectCode, groupId);
        return ResponseEntity.ok(ResponseDTO.success(sessions,
                "Sessions retrieved for group " + groupId + " in subject " + subjectCode));
    }

    @Operation(
        summary = "Eliminar sesión de un grupo",
        description = "Elimina una sesión específica de clase de un grupo por su índice"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Sesión eliminada exitosamente"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Error al eliminar la sesión"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Materia, grupo o sesión no encontrada"
        )
    })
    @DeleteMapping("/{subjectCode}/groups/{groupId}/sessions/{sessionIndex}")
    public ResponseEntity<ResponseDTO<String>> removeSessionFromGroup(
            @Parameter(description = "Código de la materia", required = true) @PathVariable String subjectCode,
            @Parameter(description = "ID del grupo", required = true) @PathVariable int groupId,
            @Parameter(description = "Índice de la sesión a eliminar", required = true) @PathVariable int sessionIndex) {
        boolean success = subjectService.removeSessionFromGroup(subjectCode, groupId, sessionIndex);
        if (success) {
            return ResponseEntity.ok(ResponseDTO.success("Session removed successfully",
                    "Session " + sessionIndex + " removed from group " + groupId + " in subject " + subjectCode));
        } else {
            return ResponseEntity.badRequest().body(ResponseDTO.error("Failed to remove session from group"));
        }
    }
}
