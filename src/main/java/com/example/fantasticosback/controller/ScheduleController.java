package com.example.fantasticosback.controller;

import com.example.fantasticosback.service.ScheduleService;
import com.example.fantasticosback.service.StudentService;
import com.example.fantasticosback.dto.response.ResponseDTO;
import com.example.fantasticosback.model.Document.Schedule;
import com.example.fantasticosback.enums.UserRole;
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

    private static final Logger log = Logger.getLogger(ScheduleController.class.getName());
    private final ScheduleService scheduleService;
    private final StudentService studentService;


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

    @Operation(summary = "Obtener el horario de un semestre específico",
            description = "Devuelve el horario (Schedule) de un semestre específico del estudiante")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Horario del semestre obtenido exitosamente"),
        @ApiResponse(responseCode = "404", description = "Estudiante o semestre no encontrado")
    })
    @GetMapping("/student/{studentId}/semester/{semester}")
    public ResponseEntity<ResponseDTO<Schedule>> getScheduleBySemester(
            @Parameter(description = "ID del estudiante") @PathVariable String studentId,
            @Parameter(description = "Semestre académico (ej: 2025-1)") @PathVariable String semester) {
        Schedule schedule = studentService.getScheduleBySemester(studentId, semester);
        return ResponseEntity.ok(ResponseDTO.success(schedule, "Schedule for semester " + semester + " retrieved successfully"));
    }

}