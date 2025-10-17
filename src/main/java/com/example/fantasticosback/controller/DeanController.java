package com.example.fantasticosback.controller;



import com.example.fantasticosback.dto.response.DeanDTO;
import com.example.fantasticosback.dto.response.ResponseDTO;
import com.example.fantasticosback.model.Document.Request;
import com.example.fantasticosback.model.Document.Student;
import com.example.fantasticosback.service.DeanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Tag(name = "Dean", description = "Gestión de Decanos y respuestas de solicitudes")

@RestController
@RequestMapping("/api/deans")
public class DeanController {

    private final DeanService deanService;

    public DeanController(DeanService deanService) {
        this.deanService = deanService;
    }
    @Operation(
            summary = "Obtener todos los Decanos",
            description = "Retorna la lista completa de Decanos registrados en el sistema"
    )
    @ApiResponse(responseCode = "200", description = "Lista de decanos obtenida exitosamente")

    @GetMapping
    public ResponseEntity<List<DeanDTO>> getAll() {
        return ResponseEntity.ok(deanService.getAll());
    }

    @Operation(summary = "Obtener un Decano por ID")
    @ApiResponse(responseCode = "200", description = "Decano encontrado")
    @ApiResponse(responseCode = "404", description = "Decano no encontrado")

    @GetMapping("/{id}")
    public ResponseEntity<DeanDTO> getById(@PathVariable String id) {
        return ResponseEntity.ok(deanService.getById(id));
    }

    @Operation(summary = "Crear un nuevo Decano")
    @ApiResponse(responseCode = "200", description = "Decano creado correctamente")

    @PostMapping
    public ResponseEntity<DeanDTO> create(@RequestBody DeanDTO dto) {
        return ResponseEntity.ok(deanService.create(dto));
    }

    @Operation(summary = "Actualizar un Decano existente")
    @ApiResponse(responseCode = "200", description = "Decano actualizado correctamente")

    @PutMapping("/{id}")
    public ResponseEntity<DeanDTO> update(@PathVariable String id, @RequestBody DeanDTO dto) {
        return ResponseEntity.ok(deanService.update(id, dto));
    }

    @Operation(summary = "Eliminar un Decano")
    @ApiResponse(responseCode = "204", description = "Decano eliminado exitosamente")

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        deanService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Resolve a student request by Dean",
            description = """
        Allows a Dean to resolve a student's request associated with their Dean Office.
        The Dean can approve or reject the request, and must provide a response message.
        
        Rules:
        - The Dean must belong to the same DeanOffice as the request.
        - Only the assigned Dean can resolve requests for their DeanOffice.
        - The request status will be updated accordingly.
        """,
            tags = {"Dean"}
    )
    @Parameters({
            @Parameter(
                    name = "deanId",
                    description = "Unique ID of the Dean resolving the request.",
                    required = true,
                    example = "64f3b12d9f2e1a4b5c9e7890"
            ),
            @Parameter(
                    name = "requestId",
                    description = "Unique ID of the request being resolved.",
                    required = true,
                    example = "6521d9e74f9f2a00b8d34211"
            ),
            @Parameter(
                    name = "responseMessage",
                    description = "Response message from the Dean (e.g., Approved, Rejected, etc.)",
                    required = true,
                    example = "Your request has been approved."
            ),
            @Parameter(
                    name = "status",
                    description = "Decision of the Dean: true = approved, false = rejected.",
                    required = true,
                    example = "true"
            )
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Request resolved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Request.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input or business rule violation",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Dean or Request not found",
                    content = @Content(mediaType = "application/json"))
    })
    @PatchMapping("/{deanId}/requests/{requestId}/resolve")
    public ResponseEntity<ResponseDTO<Request>> resolveRequest(
            @PathVariable String deanId,
            @PathVariable String requestId,
            @RequestParam String responseMessage,
            @RequestParam Boolean status) {

        Request resolved = deanService.resolveRequest(deanId, requestId, responseMessage, status);
        return ResponseEntity.ok(ResponseDTO.success(resolved, "Request resolved successfully by Dean"));
    }

    @Operation(
            summary = "Get all requests from Dean's DeanOffice",
            description = "Returns all requests belonging to the Dean's DeanOffice",
            parameters = {
                    @Parameter(name = "deanId", description = "ID of the Dean", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of requests retrieved successfully"),
                    @ApiResponse(responseCode = "404", description = "Dean not found or not assigned to any DeanOffice")
            }
    )
    @GetMapping("/{deanId}/requests")
    public ResponseEntity<ResponseDTO<List<String>>> getRequestsByDean(@PathVariable String deanId) {
        List<String> requests = deanService.getRequestsByDean(deanId);
        return ResponseEntity.ok(ResponseDTO.success(requests, "Requests retrieved successfully"));
    }

    @Operation(
            summary = "Get all students from Dean's DeanOffice",
            description = "Allows a dean to view all students belonging to their assigned DeanOffice.",
            parameters = {
                    @Parameter(name = "deanId", description = "ID of the Dean", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of students retrieved successfully"),
                    @ApiResponse(responseCode = "404", description = "Dean not assigned to any DeanOffice")
            }
    )
    @GetMapping("/{deanId}/students")
    public ResponseEntity<ResponseDTO<List<Student>>> getStudentsByDean(@PathVariable String deanId) {
        List<Student> students = deanService.getStudentsByDean(deanId);
        return ResponseEntity.ok(ResponseDTO.success(students, "Students retrieved successfully"));
    }

    @Operation(
            summary = "Get DeanOffice requests by priority",
            description = "Allows a Dean to view all requests belonging to their DeanOffice filtered by priority.",
            parameters = {
                    @Parameter(name = "deanId", description = "ID of the Dean", required = true),
                    @Parameter(name = "priority", description = "Priority level (LOW, MEDIUM, HIGH)", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Requests retrieved successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid priority value"),
                    @ApiResponse(responseCode = "404", description = "Dean not assigned to any DeanOffice")
            }
    )
    @GetMapping("/{deanId}/requests/by-priority")
    public ResponseEntity<ResponseDTO<List<Request>>> getRequestsByPriority(
            @PathVariable String deanId,
            @RequestParam String priority) {

        List<Request> requests = deanService.getRequestsByDeanAndPriority(deanId, priority);
        return ResponseEntity.ok(ResponseDTO.success(requests, "Requests retrieved successfully"));
    }



}
