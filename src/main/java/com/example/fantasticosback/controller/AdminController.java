package com.example.fantasticosback.controller;

import com.example.fantasticosback.dto.request.RegisterRequestDTO;
import com.example.fantasticosback.dto.response.ResponseDTO;
import com.example.fantasticosback.dto.response.StudentDTO;
import com.example.fantasticosback.dto.response.TeacherDTO;
import com.example.fantasticosback.dto.response.UserResponseDTO;
import com.example.fantasticosback.model.Document.User;
import com.example.fantasticosback.repository.UserRepository;
import com.example.fantasticosback.service.StudentService;
import com.example.fantasticosback.service.TeacherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Admin", description = "Administración completa del sistema - CRUD de usuarios, estudiantes y profesores")
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final StudentService studentService;
    private final TeacherService teacherService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // ========== GESTIÓN DE ESTUDIANTES ==========

    @Operation(summary = "Crear estudiante", description = "Admin crea nuevo estudiante")
    @PostMapping("/students")
    public ResponseEntity<ResponseDTO<StudentDTO>> createStudent(@RequestBody StudentDTO dto) {

        // Convertimos DTO a dominio
        var student = studentService.convertToDomain(dto);

        // Guardamos
        var saved = studentService.save(student);

        // Convertimos a respuesta DTO
        StudentDTO response = studentService.convertToStudentDTO(saved);

        return ResponseEntity.ok(ResponseDTO.success(response, "Student created by admin"));
    }

    @Operation(summary = "Listar todos los estudiantes", description = "Admin obtiene lista completa de estudiantes")
    @GetMapping("/students")
    public ResponseEntity<ResponseDTO<List<StudentDTO>>> getAllStudents() {
        List<StudentDTO> students = studentService.convertList(studentService.findAll());
        return ResponseEntity.ok(ResponseDTO.success(students, "All students retrieved by admin"));
    }

    @Operation(summary = "Actualizar estudiante", description = "Admin actualiza estudiante existente")
    @PutMapping("/students/{id}")
    public ResponseEntity<ResponseDTO<StudentDTO>> updateStudent(
            @Parameter(description = "ID del estudiante") @PathVariable String id,
            @RequestBody StudentDTO dto) {

        var updated = studentService.update(id, studentService.convertToDomain(dto));
        var response = studentService.convertToStudentDTO(updated);

        return ResponseEntity.ok(ResponseDTO.success(response, "Student updated by admin"));
    }

    @Operation(summary = "Eliminar estudiante", description = "Admin elimina estudiante")
    @DeleteMapping("/students/{id}")
    public ResponseEntity<ResponseDTO<Void>> deleteStudent(@PathVariable String id) {
        studentService.delete(id);
        return ResponseEntity.ok(ResponseDTO.success(null, "Student deleted by admin"));
    }


    // ========== GESTIÓN DE PROFESORES ==========

    @Operation(summary = "Crear profesor", description = "Admin crea nuevo profesor")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Profesor creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PostMapping("/teachers")
    public ResponseEntity<ResponseDTO<TeacherDTO>> createTeacher(@RequestBody TeacherDTO dto) {
        TeacherDTO saved = teacherService.save(dto);
        return ResponseEntity.ok(ResponseDTO.success(saved, "Teacher created by admin"));
    }

    @Operation(summary = "Listar todos los profesores", description = "Admin obtiene lista completa de profesores")
    @GetMapping("/teachers")
    public ResponseEntity<ResponseDTO<List<TeacherDTO>>> getAllTeachers() {
        List<TeacherDTO> teachers = teacherService.findAll();
        return ResponseEntity.ok(ResponseDTO.success(teachers, "All teachers retrieved by admin"));
    }

    @Operation(summary = "Actualizar profesor", description = "Admin actualiza profesor existente")
    @PutMapping("/teachers/{id}")
    public ResponseEntity<ResponseDTO<TeacherDTO>> updateTeacher(
            @Parameter(description = "ID del profesor") @PathVariable String id,
            @RequestBody TeacherDTO dto) {
        TeacherDTO updated = teacherService.update(id, dto);
        return ResponseEntity.ok(ResponseDTO.success(updated, "Teacher updated by admin"));
    }

    @Operation(summary = "Eliminar profesor", description = "Admin elimina profesor")
    @DeleteMapping("/teachers/{id}")
    public ResponseEntity<ResponseDTO<Void>> deleteTeacher(@PathVariable String id) {
        teacherService.delete(id);
        return ResponseEntity.ok(ResponseDTO.success(null, "Teacher deleted by admin"));
    }

    // ========== GESTIÓN DE USUARIOS ==========

    // ========== GESTIÓN DE USUARIOS ==========

    @Operation(summary = "Crear usuario", description = "Admin crea nuevo usuario del sistema")
    @PostMapping("/users")
    public ResponseEntity<ResponseDTO<UserResponseDTO>> createUser(@RequestBody RegisterRequestDTO registerRequest) {

        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body(ResponseDTO.error("Email is already in use"));
        }

        User newUser = new User(
                registerRequest.getEmail(),
                passwordEncoder.encode(registerRequest.getPassword()),
                registerRequest.getRole(),
                registerRequest.getProfileId()
        );

        User saved = userRepository.save(newUser);

        UserResponseDTO response = new UserResponseDTO(
                saved.getEmail(),
                saved.getRole(),
                saved.getProfileId()
        );

        return ResponseEntity.ok(ResponseDTO.success(response, "User created by admin"));
    }

    @Operation(summary = "Listar todos los usuarios", description = "Admin obtiene lista completa de usuarios")
    @GetMapping("/users")
    public ResponseEntity<ResponseDTO<List<UserResponseDTO>>> getAllUsers() {
        List<UserResponseDTO> users = userRepository.findAll().stream()
                .map(user -> new UserResponseDTO(
                        user.getEmail(),
                        user.getRole(),
                        user.getProfileId()
                ))
                .toList();

        return ResponseEntity.ok(ResponseDTO.success(users, "All users retrieved by admin"));
    }

    @Operation(summary = "Eliminar usuario", description = "Admin elimina usuario del sistema")
    @DeleteMapping("/users/{email}")
    public ResponseEntity<ResponseDTO<Void>> deleteUser(@PathVariable String email) {
        if (userRepository.findByEmail(email).isEmpty()) {
            return ResponseEntity.badRequest().body(ResponseDTO.error("User not found with email: " + email));
        }

        userRepository.deleteByEmail(email);
        return ResponseEntity.ok(ResponseDTO.success(null, "User deleted by admin"));
    }

}