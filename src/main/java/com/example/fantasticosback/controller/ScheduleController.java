package com.example.fantasticosback.controller;

import com.example.fantasticosback.service.ScheduleService;
import com.example.fantasticosback.enums.UserRole;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.logging.Logger;

@Tag(
    name = "Horarios",
    description = "Consulta de horarios académicos de estudiantes con control de acceso basado en roles"
)
@RestController
@RequestMapping("/api/schedules")
public class ScheduleController {

    private static final Logger log = Logger.getLogger(ScheduleController.class.getName());

    @Autowired
    private ScheduleService scheduleService;

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

        try {

            UserRole userRole;
            try {
                userRole = UserRole.valueOf(userRoleStr.toUpperCase());
            } catch (IllegalArgumentException e) {
                log.warning("Invalid user role: " + userRoleStr);
                return ResponseEntity.badRequest()
                    .body("Invalid user role. Valid roles: STUDENT, TEACHER, DEAN_OFFICE");
            }

            log.info("Schedule query - Student: " + studentId +
                    ", User: " + userId + ", Role: " + userRole);

            Map<String, Object> schedule = scheduleService.getStudentSchedule(studentId, userId, userRole);
            return ResponseEntity.ok(schedule);

        } catch (IllegalArgumentException e) {
            log.warning("Invalid argument: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Error: " + e.getMessage());
        } catch (IllegalAccessException e) {
            log.warning("Access denied: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body("Access denied: " + e.getMessage());
        } catch (Exception e) {
            log.severe("Internal error when querying schedule: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Internal server error");
        }
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
        try {
            log.info("Own schedule query - Student: " + studentId);

            Map<String, Object> schedule = scheduleService.getStudentSchedule(
                studentId, studentId, UserRole.STUDENT);
            return ResponseEntity.ok(schedule);

        } catch (IllegalArgumentException e) {
            log.warning("Student not found: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Error: " + e.getMessage());
        } catch (Exception e) {
            log.severe("Internal error when querying schedule: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Internal server error");
        }
    }
}