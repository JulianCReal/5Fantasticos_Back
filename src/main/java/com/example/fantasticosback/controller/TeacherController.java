package com.example.fantasticosback.controller;

import com.example.fantasticosback.dto.response.ResponseDTO;
import com.example.fantasticosback.dto.response.TeacherDTO;
import com.example.fantasticosback.service.TeacherService;
import com.example.fantasticosback.model.Document.Teacher;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Tag(
    name = "Profesores",
    description = "Gestión completa de profesores: creación, consulta, actualización, eliminación y búsquedas por filtros"
)
@RestController
@RequestMapping("/api/teachers")
public class TeacherController {

    private TeacherService teacherService;

    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @Operation(
        summary = "Crear un Profesor",
        description = "Registra un nuevo profesor en el sistema con su información personal y académica"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Profesor creado correctamente",
        content = @Content(schema = @Schema(implementation = TeacherDTO.class))
    )
    @ApiResponse(
        responseCode = "400",
        description = "Datos inválidos o profesor ya existe"
    )
    @PostMapping
    public ResponseEntity<ResponseDTO<TeacherDTO>> create(@RequestBody TeacherDTO dto) {
        TeacherDTO saved = teacherService.save(dto);
        return ResponseEntity.ok(ResponseDTO.success(saved, "Teacher created successfully"));
    }

    @Operation(
        summary = "Listar todos los Profesores",
        description = "Obtiene la lista completa de profesores registrados en el sistema"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Lista de profesores obtenida correctamente"
    )
    @GetMapping
    public ResponseEntity<ResponseDTO<List<TeacherDTO>>> getAll() {
        List<TeacherDTO> teachers = teacherService.findAll();
        return ResponseEntity.ok(ResponseDTO.success(teachers, "List of teachers"));
    }

    @Operation(
        summary = "Buscar Profesor por ID",
        description = "Obtiene los datos completos de un profesor específico por su identificador único"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Profesor encontrado",
        content = @Content(schema = @Schema(implementation = TeacherDTO.class))
    )
    @ApiResponse(
        responseCode = "404",
        description = "Profesor no encontrado"
    )
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<Teacher>> getById(
        @io.swagger.v3.oas.annotations.Parameter(description = "ID único del profesor", required = true)
        @PathVariable String id) {
        Teacher teacher = teacherService.findById(id);
        return ResponseEntity.ok(ResponseDTO.success(teacher, "Teacher found"));
    }

    @Operation(
        summary = "Actualizar un profesor",
        description = "Actualiza completamente los datos de un profesor existente (PUT)"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Profesor actualizado exitosamente"
    )
    @ApiResponse(
        responseCode = "404",
        description = "Profesor no encontrado"
    )
    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO<TeacherDTO>> update(
        @io.swagger.v3.oas.annotations.Parameter(description = "ID del profesor", required = true)
        @PathVariable String id,
        @RequestBody TeacherDTO dto) {
        TeacherDTO updated = teacherService.update(id, dto);
        return ResponseEntity.ok(ResponseDTO.success(updated, "Teacher updated successfully"));
    }

    @Operation(
        summary = "Actualizar parcialmente un profesor",
        description = "Permite actualizar solo campos específicos de un profesor sin afectar los demás (PATCH)"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Profesor actualizado parcialmente exitosamente"
    )
    @ApiResponse(
        responseCode = "404",
        description = "Profesor no encontrado"
    )
    @PatchMapping("/{id}")
    public ResponseEntity<ResponseDTO<TeacherDTO>> patch(
        @io.swagger.v3.oas.annotations.Parameter(description = "ID del profesor", required = true)
        @PathVariable String id,
        @RequestBody TeacherDTO dto) {
        TeacherDTO patched = teacherService.patch(id, dto);
        return ResponseEntity.ok(ResponseDTO.success(patched, "Teacher partially updated successfully"));
    }

    @Operation(
        summary = "Eliminar un profesor",
        description = "Elimina un profesor del sistema por su ID. Valida que no tenga grupos asignados."
    )
    @ApiResponse(
        responseCode = "200",
        description = "Profesor eliminado exitosamente"
    )
    @ApiResponse(
        responseCode = "404",
        description = "Profesor no encontrado"
    )
    @ApiResponse(
        responseCode = "400",
        description = "No se puede eliminar: tiene grupos asignados"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<Void>> delete(
        @io.swagger.v3.oas.annotations.Parameter(description = "ID del profesor", required = true)
        @PathVariable String id) {
        teacherService.delete(id);
        return ResponseEntity.ok(ResponseDTO.success(null, "Teacher deleted successfully"));
    }

    @Operation(
        summary = "Buscar profesores por departamento",
        description = "Devuelve una lista de profesores pertenecientes al departamento especificado"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Profesores encontrados por departamento"
    )
    @GetMapping("/department/{department}")
    public ResponseEntity<ResponseDTO<List<TeacherDTO>>> getByDepartment(
        @io.swagger.v3.oas.annotations.Parameter(description = "Nombre del departamento", required = true, example = "Ingeniería de Sistemas")
        @PathVariable String department) {
        List<TeacherDTO> teachers = teacherService.findByDepartment(department);
        return ResponseEntity.ok(ResponseDTO.success(teachers, "Teachers by department"));
    }

    @Operation(
        summary = "Buscar profesores por nombre",
        description = "Devuelve la lista de profesores que coincidan con el nombre especificado (búsqueda parcial)"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Profesores encontrados por nombre"
    )
    @GetMapping("/name/{name}")
    public ResponseEntity<ResponseDTO<List<TeacherDTO>>> getByName(
        @io.swagger.v3.oas.annotations.Parameter(description = "Nombre del profesor", required = true)
        @PathVariable String name) {
        List<TeacherDTO> teachers = teacherService.findByName(name);
        return ResponseEntity.ok(ResponseDTO.success(teachers, "Teachers by name"));
    }

    @Operation(
        summary = "Buscar profesores por apellido",
        description = "Devuelve una lista de profesores que coincidan con el apellido especificado (búsqueda parcial)"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Profesores encontrados por apellido"
    )
    @GetMapping("/lastname/{lastName}")
    public ResponseEntity<ResponseDTO<List<TeacherDTO>>> getByLastName(
        @io.swagger.v3.oas.annotations.Parameter(description = "Apellido del profesor", required = true)
        @PathVariable String lastName) {
        List<TeacherDTO> teachers = teacherService.findByLastName(lastName);
        return ResponseEntity.ok(ResponseDTO.success(teachers, "Teachers by last name"));
    }
}