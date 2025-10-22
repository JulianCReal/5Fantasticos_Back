package com.example.fantasticosback.controller;

import com.example.fantasticosback.service.ScheduleService;
import com.example.fantasticosback.enums.UserRole;
// No validations in controller: moved to service
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.logging.Logger;

@Tag(
    name = "Schedule",
    description = "Consulta de horarios académicos de estudiantes con control de acceso basado en roles"
)
@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private static Logger log = Logger.getLogger(ScheduleController.class.getName());
    private final ScheduleService scheduleService;

    @Operation(
        summary = "Obtener horario de un estudiante",
        description = "Permite consultar el horario académico de un estudiante. " +
                     "El acceso está controlado por roles: " +
                     "- STUDENT: Solo puede ver su propio horario. " +
                     "- TEACHER: Puede ver horarios de estudiantes en sus grupos. " +
                     "- DEAN_OFFICE: Puede ver horarios de todos los estudiantes de su facultad."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Horario obtenido exitosamente",
            content = @Content(schema = @Schema(implementation = Map.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Rol de usuario inválido"
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Acceso denegado - No tiene permisos para ver este horario"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Estudiante no encontrado"
        )
    })
    @GetMapping("/student/{studentId}")
    public ResponseEntity<?> getStudentSchedule(
            @Parameter(description = "ID del estudiante", required = true) @PathVariable String studentId,
            @Parameter(description = "ID del usuario que realiza la consulta", required = true) @RequestHeader("X-User-Id") String userId,
            @Parameter(description = "Rol del usuario (STUDENT, TEACHER, DEAN_OFFICE)", required = true) @RequestHeader("X-User-Role") String userRoleStr) {

        // No validation here: delegate to service
        log.info("Schedule query - Student: " + studentId +
                ", User: " + userId + ", RoleHeader: " + userRoleStr);

        Map<String, Object> schedule = scheduleService.getStudentSchedule(studentId, userId, userRoleStr);
        return ResponseEntity.ok(schedule);
    }



    @Operation(
        summary = "Obtener mi horario",
        description = "Permite a un estudiante consultar su propio horario académico de manera simplificada sin necesidad de headers adicionales"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Horario propio obtenido exitosamente",
            content = @Content(schema = @Schema(implementation = Map.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Estudiante no encontrado"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor"
        )
    })
    @GetMapping("/my-schedule/{studentId}")
    public ResponseEntity<?> getMySchedule(
            @Parameter(description = "ID del estudiante", required = true) @PathVariable String studentId) {
        log.info("Own schedule query - Student: " + studentId);

        Map<String, Object> schedule = scheduleService.getStudentSchedule(
            studentId, studentId, UserRole.STUDENT);
        return ResponseEntity.ok(schedule);
    }

}