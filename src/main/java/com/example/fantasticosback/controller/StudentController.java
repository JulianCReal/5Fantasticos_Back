package com.example.fantasticosback.controller;

import com.example.fantasticosback.dto.response.ResponseDTO;
import com.example.fantasticosback.dto.response.StudentDTO;
import com.example.fantasticosback.service.StudentService;
import com.example.fantasticosback.model.Document.Student;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Tag(
    name = "Student",
    description = "Gestión completa de estudiantes: CRUD, consultas por filtros, horarios y matrículas"
)
@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {


    private final StudentService studentService;

    @Operation(summary = "Crear un nuevo estudiante",
            description = "Registra un nuevo estudiante en el sistema")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Estudiante creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping
    public ResponseEntity<ResponseDTO<StudentDTO>> create(@RequestBody StudentDTO dto) {
        Student newStudent = studentService.convertToDomain(dto);
        Student saved = studentService.save(newStudent);
        StudentDTO response = studentService.convertToStudentDTO(saved);
        return ResponseEntity.ok(ResponseDTO.success(response, "Student created successfully"));
    }

    @Operation(summary = "Listar todos los estudiantes",
            description = "Obtiene la lista completa de estudiantes registrados")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de estudiantes obtenida exitosamente")
    })
    @GetMapping
    public ResponseEntity<ResponseDTO<List<StudentDTO>>> list() {
        List<StudentDTO> students = studentService.convertList(studentService.findAll());
        return ResponseEntity.ok(ResponseDTO.success(students, "List of students"));
    }

    @Operation(summary = "Obtener estudiante por ID",
            description = "Devuelve los datos de un estudiante específico")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Estudiante encontrado"),
        @ApiResponse(responseCode = "404", description = "Estudiante no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<StudentDTO>> get(@Parameter(description = "ID del estudiante") @PathVariable String id) {
        Student student = studentService.findById(id);
        StudentDTO dto = studentService.convertToStudentDTO(student);
        return ResponseEntity.ok(ResponseDTO.success(dto, "Student found"));
    }

    @Operation(summary = "Actualizar estudiante",
            description = "Actualiza los datos de un estudiante existente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Estudiante actualizado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Estudiante no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO<StudentDTO>> update(@Parameter(description = "ID del estudiante") @PathVariable String id, @RequestBody StudentDTO dto) {
        Student updated = studentService.convertToDomain(dto);
        Student saved = studentService.update(id, updated);
        StudentDTO response = studentService.convertToStudentDTO(saved);
        return ResponseEntity.ok(ResponseDTO.success(response, "Student updated"));
    }

    @Operation(summary = "Eliminar estudiante",
            description = "Elimina un estudiante del sistema")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Estudiante eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Estudiante no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<Void>> delete(@Parameter(description = "ID del estudiante") @PathVariable String id) {
        studentService.delete(id);
        return ResponseEntity.ok(ResponseDTO.success(null, "Student deleted successfully"));
    }

    @Operation(summary = "Listar estudiantes por carrera",
            description = "Obtiene estudiantes filtrados por carrera")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de estudiantes por carrera obtenida exitosamente")
    })
    @GetMapping("/career/{career}")
    public ResponseEntity<ResponseDTO<List<StudentDTO>>> getByCareer(@Parameter(description = "Nombre de la carrera") @PathVariable String career) {
        List<StudentDTO> students = studentService.convertList(studentService.findByCareer(career));
        return ResponseEntity.ok(ResponseDTO.success(students, "Students by career"));
    }

    @Operation(summary = "Listar estudiantes por semestre",
            description = "Obtiene estudiantes filtrados por semestre")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de estudiantes por semestre obtenida exitosamente")
    })
    @GetMapping("/semester/{semester}")
    public ResponseEntity<ResponseDTO<List<StudentDTO>>> getBySemester(@Parameter(description = "Número de semestre") @PathVariable int semester) {
        List<StudentDTO> students = studentService.convertList(studentService.findBySemester(semester));
        return ResponseEntity.ok(ResponseDTO.success(students, "Students by semester"));
    }

    @Operation(summary = "Actualizar parcialmente un estudiante",
            description = "Actualiza solo los campos proporcionados de un estudiante existente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Estudiante actualizado parcialmente exitosamente"),
        @ApiResponse(responseCode = "404", description = "Estudiante no encontrado"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })

    @PatchMapping("/{id}")
    public ResponseEntity<ResponseDTO<StudentDTO>> partialUpdate(
            @Parameter(description = "ID del estudiante") @PathVariable String id,
            @RequestBody StudentDTO dto) {
        Student updated = studentService.partialUpdate(id, dto);
        StudentDTO response = studentService.convertToStudentDTO(updated);
        return ResponseEntity.ok(ResponseDTO.success(response, "Student partially updated"));
    }
}
