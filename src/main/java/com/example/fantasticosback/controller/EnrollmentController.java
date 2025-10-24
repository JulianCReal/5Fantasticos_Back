package com.example.fantasticosback.controller;

import com.example.fantasticosback.model.Document.Enrollment;
import com.example.fantasticosback.service.EnrollmentService;
import com.example.fantasticosback.service.StudentService;
import com.example.fantasticosback.dto.request.EnrollmentRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Enrollment",
        description = "Gestión de matrículas: inscripción de estudiantes en grupos, cancelación, consulta y actualización de calificaciones"
)
@RestController
@RequestMapping("/api/enrollments")
@CrossOrigin(origins = "*")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;
    private final StudentService studentService;

    public EnrollmentController(EnrollmentService enrollmentService, StudentService studentService) {
        this.enrollmentService = enrollmentService;
        this.studentService = studentService;
    }

    @Operation(
            summary = "Matricular estudiante en un grupo",
            description = "Permite inscribir un estudiante en un grupo específico de una materia para un semestre académico determinado"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Estudiante matriculado exitosamente",
                    content = @Content(schema = @Schema(implementation = Enrollment.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos o estudiante ya matriculado"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Estudiante o grupo no encontrado"
            )
    })
    @PostMapping("/students/{studentId}/groups/{groupId}")
    public ResponseEntity<Enrollment> enrollStudent(
            @Parameter(description = "ID del estudiante", required = true) @PathVariable String studentId,
            @Parameter(description = "ID del grupo", required = true) @PathVariable String groupId,
            @Parameter(description = "Semestre académico (ej: 2024-1)", required = true) @RequestParam String semester) {

        Enrollment enrollment = enrollmentService.enrollStudentInGroup(studentId, groupId, semester);
        return ResponseEntity.ok(enrollment);
    }

    @Operation(
            summary = "Cancelar matrícula",
            description = "Permite a un estudiante cancelar su matrícula de un grupo específico"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Matrícula cancelada exitosamente"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Estudiante o matrícula no encontrada"
            )
    })
    @DeleteMapping("/students/{studentId}/enrollments/{enrollmentId}")
    public ResponseEntity<Void> cancelEnrollment(
            @Parameter(description = "ID del estudiante", required = true) @PathVariable String studentId,
            @Parameter(description = "ID de la matrícula", required = true) @PathVariable String enrollmentId) {

        enrollmentService.cancelEnrollment(studentId, enrollmentId);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Obtener matrículas de un estudiante",
            description = "Retorna todas las matrículas activas de un estudiante específico"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de matrículas obtenida exitosamente"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Estudiante no encontrado"
            )
    })
    @GetMapping("/students/{studentId}")
    public ResponseEntity<List<Enrollment>> getStudentEnrollments(
            @Parameter(description = "ID del estudiante", required = true) @PathVariable String studentId) {
        return ResponseEntity.ok(enrollmentService.getEnrollmentsByStudentId(studentId));
    }

    @Operation(
            summary = "Obtener matrículas de un grupo",
            description = "Retorna todas las matrículas (estudiantes inscritos) de un grupo específico"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de matrículas del grupo obtenida exitosamente"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Grupo no encontrado"
            )
    })
    @GetMapping("/groups/{groupId}")
    public ResponseEntity<List<Enrollment>> getGroupEnrollments(
            @Parameter(description = "ID del grupo", required = true) @PathVariable String groupId) {
        return ResponseEntity.ok(enrollmentService.getEnrollmentsByGroupId(groupId));
    }

    @Operation(
            summary = "Actualizar calificación de una matrícula",
            description = "Permite al profesor actualizar la calificación final de un estudiante en una matrícula específica"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Calificación actualizada exitosamente",
                    content = @Content(schema = @Schema(implementation = Enrollment.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Calificación inválida (debe estar entre 0.0 y 5.0)"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Matrícula no encontrada"
            )
    })
    @PutMapping("/{enrollmentId}/grade")
    public ResponseEntity<Enrollment> updateGrade(
            @Parameter(description = "ID de la matrícula", required = true) @PathVariable String enrollmentId,
            @Parameter(description = "Calificación (0.0 - 5.0)", required = true) @RequestParam double grade) {

        Enrollment enrollment = enrollmentService.updateGrade(enrollmentId, grade);
        return ResponseEntity.ok(enrollment);
    }
}