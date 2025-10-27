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
import com.example.fantasticosback.service.DeanService;
import com.example.fantasticosback.service.DeanOfficeService;
import com.example.fantasticosback.service.SubjectService;
import com.example.fantasticosback.service.GroupService;
import com.example.fantasticosback.service.EnrollmentService;
import com.example.fantasticosback.model.Document.Enrollment;
import com.example.fantasticosback.dto.response.DeanDTO;
import com.example.fantasticosback.dto.response.DeanOfficeDTO;
import com.example.fantasticosback.dto.response.SubjectDTO;
import com.example.fantasticosback.dto.response.CreateGroupDTO;
import com.example.fantasticosback.model.Document.Group;
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
    private final DeanService deanService;
    private final DeanOfficeService deanOfficeService;
    private final SubjectService subjectService;
    private final GroupService groupService;
    private final EnrollmentService enrollmentService;
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

    // ========== GESTIÓN DE DECANOS ==========

    @Operation(summary = "Crear decano", description = "Admin crea nuevo decano")
    @PostMapping("/deans")
    public ResponseEntity<ResponseDTO<DeanDTO>> createDean(@RequestBody DeanDTO dto) {
        DeanDTO saved = deanService.create(dto);
        return ResponseEntity.ok(ResponseDTO.success(saved, "Dean created by admin"));
    }

    @Operation(summary = "Listar todos los decanos", description = "Admin obtiene lista completa de decanos")
    @GetMapping("/deans")
    public ResponseEntity<ResponseDTO<List<DeanDTO>>> getAllDeans() {
        List<DeanDTO> deans = deanService.getAll();
        return ResponseEntity.ok(ResponseDTO.success(deans, "All deans retrieved by admin"));
    }

    @Operation(summary = "Actualizar decano", description = "Admin actualiza decano existente")
    @PutMapping("/deans/{id}")
    public ResponseEntity<ResponseDTO<DeanDTO>> updateDean(
            @Parameter(description = "ID del decano") @PathVariable String id,
            @RequestBody DeanDTO dto) {
        DeanDTO updated = deanService.update(id, dto);
        return ResponseEntity.ok(ResponseDTO.success(updated, "Dean updated by admin"));
    }

    @Operation(summary = "Eliminar decano", description = "Admin elimina decano")
    @DeleteMapping("/deans/{id}")
    public ResponseEntity<ResponseDTO<Void>> deleteDean(@PathVariable String id) {
        deanService.delete(id);
        return ResponseEntity.ok(ResponseDTO.success(null, "Dean deleted by admin"));
    }

    // ========== GESTIÓN DE DECANATURAS ==========

    @Operation(summary = "Crear decanatura", description = "Admin crea nueva decanatura")
    @PostMapping("/dean-offices")
    public ResponseEntity<ResponseDTO<DeanOfficeDTO>> createDeanOffice(@RequestBody DeanOfficeDTO dto) {
        DeanOfficeDTO saved = deanOfficeService.save(dto);
        return ResponseEntity.ok(ResponseDTO.success(saved, "Dean office created by admin"));
    }

    @Operation(summary = "Listar todas las decanaturas", description = "Admin obtiene lista completa de decanaturas")
    @GetMapping("/dean-offices")
    public ResponseEntity<ResponseDTO<List<DeanOfficeDTO>>> getAllDeanOffices() {
        List<DeanOfficeDTO> deanOffices = deanOfficeService.findAll();
        return ResponseEntity.ok(ResponseDTO.success(deanOffices, "All dean offices retrieved by admin"));
    }

    @Operation(summary = "Actualizar decanatura", description = "Admin actualiza decanatura existente")
    @PutMapping("/dean-offices/{id}")
    public ResponseEntity<ResponseDTO<DeanOfficeDTO>> updateDeanOffice(
            @Parameter(description = "ID de la decanatura") @PathVariable String id,
            @RequestBody DeanOfficeDTO dto) {
        DeanOfficeDTO updated = deanOfficeService.update(id, dto);
        return ResponseEntity.ok(ResponseDTO.success(updated, "Dean office updated by admin"));
    }

    @Operation(summary = "Eliminar decanatura", description = "Admin elimina decanatura")
    @DeleteMapping("/dean-offices/{id}")
    public ResponseEntity<ResponseDTO<Void>> deleteDeanOffice(@PathVariable String id) {
        deanOfficeService.delete(id);
        return ResponseEntity.ok(ResponseDTO.success(null, "Dean office deleted by admin"));
    }

    // ========== GESTIÓN DE MATERIAS ==========

    @Operation(summary = "Crear materia", description = "Admin crea nueva materia")
    @PostMapping("/subjects")
    public ResponseEntity<ResponseDTO<SubjectDTO>> createSubject(@RequestBody SubjectDTO dto) {
        SubjectDTO saved = subjectService.createSubject(dto);
        return ResponseEntity.ok(ResponseDTO.success(saved, "Subject created by admin"));
    }

    @Operation(summary = "Listar todas las materias", description = "Admin obtiene lista completa de materias")
    @GetMapping("/subjects")
    public ResponseEntity<ResponseDTO<List<SubjectDTO>>> getAllSubjects() {
        List<SubjectDTO> subjects = subjectService.toDTOList(subjectService.findAll());
        return ResponseEntity.ok(ResponseDTO.success(subjects, "All subjects retrieved by admin"));
    }

    @Operation(summary = "Obtener materia por código", description = "Admin obtiene materia específica")
    @GetMapping("/subjects/{code}")
    public ResponseEntity<ResponseDTO<SubjectDTO>> getSubject(@PathVariable String code) {
        SubjectDTO subject = subjectService.toDTO(subjectService.findById(code));
        return ResponseEntity.ok(ResponseDTO.success(subject, "Subject retrieved by admin"));
    }

    // ========== GESTIÓN DE GRUPOS ==========

    @Operation(summary = "Crear grupo para materia", description = "Admin crea nuevo grupo para una materia")
    @PostMapping("/subjects/{subjectCode}/groups")
    public ResponseEntity<ResponseDTO<String>> createGroup(
            @Parameter(description = "Código de la materia") @PathVariable String subjectCode,
            @RequestBody CreateGroupDTO dto) {
        boolean created = subjectService.addGroupToSubject(subjectCode, dto);
        return ResponseEntity.ok(ResponseDTO.success("Group created", "Group created successfully for subject"));
    }

    @Operation(summary = "Listar grupos de materia", description = "Admin obtiene grupos de una materia")
    @GetMapping("/subjects/{subjectCode}/groups")
    public ResponseEntity<ResponseDTO<List<Object>>> getGroupsBySubject(@PathVariable String subjectCode) {
        List<Object> groups = subjectService.getGroupsBySubject(subjectCode);
        return ResponseEntity.ok(ResponseDTO.success(groups, "Groups retrieved by admin"));
    }

    @Operation(summary = "Asignar profesor a grupo", description = "Admin asigna profesor a un grupo")
    @PutMapping("/groups/{groupId}/assign-teacher/{teacherId}")
    public ResponseEntity<ResponseDTO<Group>> assignTeacherToGroup(
            @Parameter(description = "ID del grupo") @PathVariable String groupId,
            @Parameter(description = "ID del profesor") @PathVariable String teacherId) {
        Group updated = groupService.assignTeacher(groupId, teacherId);
        return ResponseEntity.ok(ResponseDTO.success(updated, "Teacher assigned to group by admin"));
    }

    @Operation(summary = "Obtener grupo por ID", description = "Admin obtiene información de un grupo")
    @GetMapping("/groups/{groupId}")
    public ResponseEntity<ResponseDTO<Group>> getGroup(@PathVariable String groupId) {
        Group group = groupService.getGroupById(groupId);
        return ResponseEntity.ok(ResponseDTO.success(group, "Group retrieved by admin"));
    }

    // ========== GESTIÓN DE MATRÍCULAS ==========

    @Operation(summary = "Matricular estudiante en grupo", description = "Admin matricula estudiante en un grupo")
    @PostMapping("/enrollments/students/{studentId}/groups/{groupId}")
    public ResponseEntity<ResponseDTO<Enrollment>> enrollStudent(
            @Parameter(description = "ID del estudiante") @PathVariable String studentId,
            @Parameter(description = "ID del grupo") @PathVariable String groupId,
            @Parameter(description = "Semestre académico") @RequestParam String semester) {
        Enrollment enrollment = enrollmentService.enrollStudentInGroup(studentId, groupId, semester);
        return ResponseEntity.ok(ResponseDTO.success(enrollment, "Student enrolled by admin"));
    }

    @Operation(summary = "Cancelar matrícula", description = "Admin cancela matrícula de estudiante")
    @DeleteMapping("/enrollments/students/{studentId}/enrollments/{enrollmentId}")
    public ResponseEntity<ResponseDTO<Void>> cancelEnrollment(
            @Parameter(description = "ID del estudiante") @PathVariable String studentId,
            @Parameter(description = "ID de la matrícula") @PathVariable String enrollmentId) {
        enrollmentService.cancelEnrollment(studentId, enrollmentId);
        return ResponseEntity.ok(ResponseDTO.success(null, "Enrollment cancelled by admin"));
    }

    @Operation(summary = "Obtener matrículas de estudiante", description = "Admin consulta matrículas de un estudiante")
    @GetMapping("/enrollments/students/{studentId}")
    public ResponseEntity<ResponseDTO<List<Enrollment>>> getStudentEnrollments(@PathVariable String studentId) {
        List<Enrollment> enrollments = enrollmentService.getEnrollmentsByStudentId(studentId);
        return ResponseEntity.ok(ResponseDTO.success(enrollments, "Student enrollments retrieved by admin"));
    }

    @Operation(summary = "Obtener matrículas de grupo", description = "Admin consulta matrículas de un grupo")
    @GetMapping("/enrollments/groups/{groupId}")
    public ResponseEntity<ResponseDTO<List<Enrollment>>> getGroupEnrollments(@PathVariable String groupId) {
        List<Enrollment> enrollments = enrollmentService.getEnrollmentsByGroupId(groupId);
        return ResponseEntity.ok(ResponseDTO.success(enrollments, "Group enrollments retrieved by admin"));
    }

    @Operation(summary = "Actualizar calificación", description = "Admin actualiza calificación de matrícula")
    @PutMapping("/enrollments/{enrollmentId}/grade")
    public ResponseEntity<ResponseDTO<Enrollment>> updateGrade(
            @Parameter(description = "ID de la matrícula") @PathVariable String enrollmentId,
            @Parameter(description = "Calificación (0.0 - 5.0)") @RequestParam double grade) {
        Enrollment enrollment = enrollmentService.updateGrade(enrollmentId, grade);
        return ResponseEntity.ok(ResponseDTO.success(enrollment, "Grade updated by admin"));
    }

    @Operation(summary = "Verificar conflicto de horario", description = "Admin verifica conflictos antes de matricular")
    @GetMapping("/enrollments/students/{studentId}/groups/{groupId}/conflict-check")
    public ResponseEntity<ResponseDTO<Boolean>> checkScheduleConflict(
            @Parameter(description = "ID del estudiante") @PathVariable String studentId,
            @Parameter(description = "ID del grupo") @PathVariable String groupId) {
        boolean hasConflict = enrollmentService.verifyScheduleConflict(studentId, groupId);
        String message = hasConflict ? "Schedule conflict detected" : "No schedule conflict";
        return ResponseEntity.ok(ResponseDTO.success(hasConflict, message));
    }

}