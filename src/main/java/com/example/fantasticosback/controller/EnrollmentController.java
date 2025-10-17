package com.example.fantasticosback.controller;

import com.example.fantasticosback.dto.response.ResponseDTO;
import com.example.fantasticosback.model.Document.Enrollment;
import com.example.fantasticosback.service.EnrollmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "enrollment-controller", description = "Gestión de Matrículas y Inscripciones de Estudiantes")
@RestController
@RequestMapping("/api/enrollments")
@CrossOrigin(origins = "*")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @Operation(summary = "Matricular estudiante en un grupo",
            description = "Inscribe un estudiante específico en un grupo de una materia para un semestre determinado")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Estudiante matriculado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Estudiante o grupo no encontrado"),
        @ApiResponse(responseCode = "400", description = "El grupo está lleno o el estudiante ya está inscrito")
    })
    @PostMapping("/students/{studentId}/groups/{groupId}")
    public ResponseEntity<ResponseDTO<Enrollment>> enrollStudent(
            @Parameter(description = "ID del estudiante a matricular") @PathVariable String studentId,
            @Parameter(description = "ID del grupo al cual inscribir") @PathVariable String groupId,
            @Parameter(description = "Código del semestre (ej: 2024-1)") @RequestParam String semester) {

        Enrollment enrollment = enrollmentService.enrollStudentInGroup(studentId, groupId, semester);
        return ResponseEntity.ok(ResponseDTO.success(enrollment,
            "Estudiante matriculado exitosamente en el grupo"));
    }

    @Operation(summary = "Cancelar matrícula de estudiante",
            description = "Cancela la inscripción de un estudiante en una materia específica")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Matrícula cancelada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Estudiante o matrícula no encontrada")
    })
    @DeleteMapping("/students/{studentId}/enrollments/{enrollmentId}")
    public ResponseEntity<ResponseDTO<String>> cancelEnrollment(
            @Parameter(description = "ID del estudiante") @PathVariable String studentId,
            @Parameter(description = "ID de la matrícula a cancelar") @PathVariable String enrollmentId) {

        enrollmentService.cancelEnrollment(studentId, enrollmentId);
        return ResponseEntity.ok(ResponseDTO.success("Matrícula cancelada exitosamente",
            "La inscripción ha sido retirada del sistema"));
    }

    @Operation(summary = "Obtener matrículas de un estudiante",
            description = "Consulta todas las materias en las que está inscrito un estudiante específico")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Matrículas obtenidas exitosamente"),
        @ApiResponse(responseCode = "404", description = "Estudiante no encontrado")
    })
    @GetMapping("/students/{studentId}")
    public ResponseEntity<ResponseDTO<List<Enrollment>>> getStudentEnrollments(
            @Parameter(description = "ID del estudiante") @PathVariable String studentId) {
        List<Enrollment> enrollments = enrollmentService.getEnrollmentsByStudentId(studentId);
        return ResponseEntity.ok(ResponseDTO.success(enrollments,
            "Matrículas del estudiante obtenidas exitosamente"));
    }

    @Operation(summary = "Obtener estudiantes inscritos en un grupo",
            description = "Consulta todos los estudiantes matriculados en un grupo específico")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de estudiantes obtenida exitosamente"),
        @ApiResponse(responseCode = "404", description = "Grupo no encontrado")
    })
    @GetMapping("/groups/{groupId}")
    public ResponseEntity<ResponseDTO<List<Enrollment>>> getGroupEnrollments(
            @Parameter(description = "ID del grupo") @PathVariable String groupId) {
        List<Enrollment> enrollments = enrollmentService.getEnrollmentsByGroupId(groupId);
        return ResponseEntity.ok(ResponseDTO.success(enrollments,
            "Estudiantes del grupo obtenidos exitosamente"));
    }

    @Operation(summary = "Actualizar calificación de matrícula",
            description = "Asigna o actualiza la calificación final de un estudiante en una materia")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Calificación actualizada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Matrícula no encontrada"),
        @ApiResponse(responseCode = "400", description = "Calificación inválida (debe estar entre 0.0 y 5.0)")
    })
    @PutMapping("/{enrollmentId}/grade")
    public ResponseEntity<ResponseDTO<Enrollment>> updateGrade(
            @Parameter(description = "ID de la matrícula") @PathVariable String enrollmentId,
            @Parameter(description = "Calificación (0.0 - 5.0)") @RequestParam double grade) {

        Enrollment enrollment = enrollmentService.updateGrade(enrollmentId, grade);
        return ResponseEntity.ok(ResponseDTO.success(enrollment,
            "Calificación actualizada exitosamente"));
    }
}
