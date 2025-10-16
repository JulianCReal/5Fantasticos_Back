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
        name = "Teacher",
        description = "Teacher management: creation, consultation, update and deletion"
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
            description = "Registra un nuevo profesor en el sistema"
    )
    @ApiResponse(
            responseCode = ("200") ,
            description = "Profesor creado correctamente",
            content = @Content(schema = @Schema(implementation = TeacherDTO.class))
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
            responseCode = ("200") ,
            description = "Lista de profesores obtenida correctamente"
    )
    @GetMapping
    public ResponseEntity<ResponseDTO<List<TeacherDTO>>> getAll() {
        List<TeacherDTO> teachers = teacherService.findAll();
        return ResponseEntity.ok(ResponseDTO.success(teachers, "List of teachers"));
    }

    @Operation(
            summary = "Buscar Profesor por ID",
            description = "Obtiene los datos de un profesor especifico por su ID"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Profesor encontrado",
            content = @Content(schema = @Schema(implementation = TeacherDTO.class))
    )
    @ApiResponse(responseCode = "404", description = "Profesor no encontrado")
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<TeacherDTO>> getById(@PathVariable String id) {
        TeacherDTO teacher = teacherService.findById(id);
        return ResponseEntity.ok(ResponseDTO.success(teacher, "Teacher found"));
    }

    @Operation(
            summary = "Actualizar un profesor",
            description = "Actualiza un profesor completamente (PUT)"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Profesor actualizado exitosamente"
    )
    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO<TeacherDTO>> update(@PathVariable String id, @RequestBody TeacherDTO dto) {
        TeacherDTO updated = teacherService.update(id, dto);
        return ResponseEntity.ok(ResponseDTO.success(updated, "Teacher updated successfully"));
    }

    @Operation(
            summary = "Se actualiza un profesor",
            description = "Permite actualizar solo un campo de profesor (PATCH)"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Profesor actualizado parcialmente exitosamente"
    )
    @PatchMapping("/{id}")
    public ResponseEntity<ResponseDTO<TeacherDTO>> patch(@PathVariable String id, @RequestBody TeacherDTO dto) {
        TeacherDTO patched = teacherService.patch(id, dto);
        return ResponseEntity.ok(ResponseDTO.success(patched, "Teacher partially updated successfully"));
    }

    @Operation(
            summary = "Elimina un profesor",
            description = "Elimina un profesor por su ID"
    )
    @ApiResponse(responseCode = "200", description = "Teacher deleted successfully")
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<Void>> delete(@PathVariable String id) {
        teacherService.delete(id);
        return ResponseEntity.ok(ResponseDTO.success(null, "Teacher deleted successfully"));
    }

    @Operation(
            summary = "Busca profesores por departamento",
            description = "Devuelce una lista de profesores pertenecientes al departamento especificado"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Profesores encontrados por departamento"
    )
    @GetMapping("/department/{department}")
    public ResponseEntity<ResponseDTO<List<TeacherDTO>>> getByDepartment(@PathVariable String department) {
        List<TeacherDTO> teachers = teacherService.findByDepartment(department);
        return ResponseEntity.ok(ResponseDTO.success(teachers, "Teachers by department"));
    }

    @Operation(
            summary = "Busca profesor por nombre",
            description = "Devuelve la lista de profesores que coincidan con el nombre especificado"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Profesor encontrado por nombre"
    )
    @GetMapping("/name/{name}")
    public ResponseEntity<ResponseDTO<List<TeacherDTO>>> getByName(@PathVariable String name) {
        List<TeacherDTO> teachers = teacherService.findByName(name);
        return ResponseEntity.ok(ResponseDTO.success(teachers, "Teachers by name"));
    }

    @Operation(
            summary = "Busca profesores por apellido",
            description = "Devuelve una lista de profesores que coincidan con el apellido"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Profesor encontrado por apellido"
    )
    @GetMapping("/lastname/{lastName}")
    public ResponseEntity<ResponseDTO<List<TeacherDTO>>> getByLastName(@PathVariable String lastName) {
        List<TeacherDTO> teachers = teacherService.findByLastName(lastName);
        return ResponseEntity.ok(ResponseDTO.success(teachers, "Teachers by last name"));
    }
}
