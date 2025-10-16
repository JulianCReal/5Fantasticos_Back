package com.example.fantasticosback.controller;

import com.example.fantasticosback.dto.request.CreateGroupRequestDTO;
import com.example.fantasticosback.dto.request.CreateSessionRequestDTO;
import com.example.fantasticosback.dto.response.GroupResponseDTO;
import com.example.fantasticosback.dto.response.SessionResponseDTO;
import com.example.fantasticosback.dto.response.GroupCapacityDTO;
import com.example.fantasticosback.dto.response.ResponseDTO;
import com.example.fantasticosback.mapper.GroupMapper;
import com.example.fantasticosback.model.Document.Group;
import com.example.fantasticosback.model.Document.Student;
import com.example.fantasticosback.model.Document.Teacher;
import com.example.fantasticosback.service.GroupService;
import com.example.fantasticosback.service.StudentService;
import com.example.fantasticosback.service.TeacherService;
import com.example.fantasticosback.util.ClassSession;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "group-controller", description = "Gestión de Grupos y sus operaciones")
@RestController
@RequestMapping("/api/groups")
@CrossOrigin(origins = "*")
public class GroupController {

    private final GroupService groupService;
    private final StudentService studentService;
    private final TeacherService teacherService;

    public GroupController(GroupService groupService, StudentService studentService, TeacherService teacherService) {
        this.groupService = groupService;
        this.studentService = studentService;
        this.teacherService = teacherService;
    }

    @PostMapping
    public ResponseEntity<GroupResponseDTO> createGroup(@RequestBody CreateGroupRequestDTO dto) {
        Teacher teacher = null;
        if (dto.getTeacherId() != null && !dto.getTeacherId().isEmpty()) {
            teacher = teacherService.findById(dto.getTeacherId());
        }
        Group group = GroupMapper.toDomain(dto, teacher);
        Group createdGroup = groupService.createGroup(group);
        return ResponseEntity.ok(GroupMapper.toDTO(createdGroup));
    }

    @PutMapping("/{groupId}/teacher/{teacherId}")
    public ResponseEntity<GroupResponseDTO> assignTeacher(
            @PathVariable String groupId,
            @PathVariable String teacherId) {
        Group updatedGroup = groupService.assignTeacher(groupId, teacherId);
        return ResponseEntity.ok(GroupMapper.toDTO(updatedGroup));
    }

    @PostMapping("/{groupId}/sessions")
    public ResponseEntity<GroupResponseDTO> addSession(
            @PathVariable String groupId,
            @RequestBody CreateSessionRequestDTO dto) {
        ClassSession session = GroupMapper.sessionToDomain(dto);
        Group updatedGroup = groupService.addSession(groupId, session);
        return ResponseEntity.ok(GroupMapper.toDTO(updatedGroup));
    }

    @GetMapping("/{groupId}/schedule")
    public ResponseEntity<List<SessionResponseDTO>> getGroupSchedule(@PathVariable String groupId) {
        List<ClassSession> sessions = groupService.getGroupSchedule(groupId);
        return ResponseEntity.ok(GroupMapper.sessionsToDTOList(sessions));
    }

    @PutMapping("/{groupId}/students")
    public ResponseEntity<?> addStudent(
            @PathVariable String groupId,
            @RequestBody Student student) {
        groupService.addStudentToGroup(groupId, student);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{groupId}/students/{studentId}")
    public ResponseEntity<?> removeStudent(
            @PathVariable String groupId,
            @PathVariable String studentId) {
        Student student = studentService.findById(studentId);
        groupService.removeStudentFromGroup(groupId, student);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{groupId}")
    public ResponseEntity<GroupResponseDTO> getGroupById(@PathVariable String groupId) {
        Group group = groupService.getGroupById(groupId);
        return ResponseEntity.ok(GroupMapper.toDTO(group));
    }

    @Operation(summary = "Consultar capacidad de grupos por código de materia",
            description = "Obtiene información de capacidad (cupo máximo, estudiantes inscritos y porcentaje de ocupación) de todos los grupos de una materia específica")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Información de capacidad obtenida exitosamente"),
        @ApiResponse(responseCode = "404", description = "Materia no encontrada o sin grupos")
    })
    @GetMapping("/capacity/subject/{subjectCode}")
    public ResponseEntity<ResponseDTO<List<GroupCapacityDTO>>> getGroupCapacityBySubjectCode(
            @Parameter(description = "Código de la materia (ej: CALD)") @PathVariable String subjectCode) {
        List<GroupCapacityDTO> capacityInfo = groupService.getGroupCapacityBySubjectCode(subjectCode);
        return ResponseEntity.ok(ResponseDTO.success(capacityInfo,
            "Información de capacidad para la materia " + subjectCode + " obtenida exitosamente"));
    }
}
