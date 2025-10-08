package com.example.fantasticosback.Persistence.Controller;
import com.example.fantasticosback.Dtos.CreateGroupDTO;
import com.example.fantasticosback.Dtos.ResponseDTO;
import com.example.fantasticosback.Dtos.SubjectDTO;
import com.example.fantasticosback.Persistence.Server.SubjectService;
import com.example.fantasticosback.Model.Entities.Subject;
import com.example.fantasticosback.util.ClassSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subjects")
public class SubjectController {
    @Autowired
    private SubjectService subjectService;

    @GetMapping
    public ResponseEntity<ResponseDTO<List<SubjectDTO>>> list() {
        List<Subject> subjects = subjectService.findAll();
        return ResponseEntity.ok(ResponseDTO.success(subjectService.toDTOList(subjects), "List of predefined subjects"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<SubjectDTO>> get(@PathVariable String id){
        Subject subject = subjectService.findById(id);
        if (subject != null) {
            return ResponseEntity.ok(ResponseDTO.success(subjectService.toDTO(subject), "Subject found"));
        }
        return ResponseEntity.status(404).body(ResponseDTO.error("Subject not found"));
    }

    @GetMapping("/semester/{semester}")
    public ResponseEntity<ResponseDTO<List<SubjectDTO>>> getBySemester(@PathVariable int semester){
        List<Subject> subjects = subjectService.findBySemester(semester);
        return ResponseEntity.ok(ResponseDTO.success(subjectService.toDTOList(subjects), "Subjects by semester"));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<ResponseDTO<List<SubjectDTO>>> getByName(@PathVariable String name){
        List<Subject> subjects = subjectService.findByName(name);
        return ResponseEntity.ok(ResponseDTO.success(subjectService.toDTOList(subjects), "Subjects by name"));
    }

    // Endpoint para agregar grupos a una materia existente usando abreviatura
    @PostMapping("/{subjectCode}/groups")
    public ResponseEntity<ResponseDTO<String>> addGroupToSubject(
            @PathVariable String subjectCode,
            @RequestBody CreateGroupDTO groupDto) {
        try {
            boolean success = subjectService.addGroupToSubjectByCode(subjectCode, groupDto);
            if (success) {
                return ResponseEntity.ok(ResponseDTO.success("Group added successfully", "Group added to subject " + subjectCode));
            } else {
                return ResponseEntity.badRequest().body(ResponseDTO.error("Failed to add group to subject"));
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(ResponseDTO.error("Subject not found: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ResponseDTO.error("Internal error: " + e.getMessage()));
        }
    }

    // Endpoint para ver grupos de una materia específica usando abreviatura
    @GetMapping("/{subjectCode}/groups")
    public ResponseEntity<ResponseDTO<List<Object>>> getSubjectGroups(@PathVariable String subjectCode) {
        try {
            List<Object> groups = subjectService.getGroupsBySubjectCode(subjectCode);
            return ResponseEntity.ok(ResponseDTO.success(groups, "Groups retrieved for " + subjectCode));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(ResponseDTO.error("Subject not found: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ResponseDTO.error("Internal error: " + e.getMessage()));
        }
    }

    // Endpoint para añadir una sesión de clase a un grupo específico
    @PostMapping("/{subjectCode}/groups/{groupId}/sessions")
    public ResponseEntity<ResponseDTO<String>> addSessionToGroup(
            @PathVariable String subjectCode,
            @PathVariable int groupId,
            @RequestBody ClassSession session) {
        try {
            boolean success = subjectService.addSessionToGroup(subjectCode, groupId, session);
            if (success) {
                return ResponseEntity.ok(ResponseDTO.success("Session added successfully",
                        "Session added to group " + groupId + " in subject " + subjectCode));
            } else {
                return ResponseEntity.badRequest().body(ResponseDTO.error("Failed to add session to group"));
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(ResponseDTO.error("Error: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ResponseDTO.error("Internal error: " + e.getMessage()));
        }
    }

    // Endpoint para obtener las sesiones de un grupo específico
    @GetMapping("/{subjectCode}/groups/{groupId}/sessions")
    public ResponseEntity<ResponseDTO<List<ClassSession>>> getGroupSessions(
            @PathVariable String subjectCode,
            @PathVariable int groupId) {
        try {
            List<ClassSession> sessions = subjectService.getGroupSessions(subjectCode, groupId);
            return ResponseEntity.ok(ResponseDTO.success(sessions,
                    "Sessions retrieved for group " + groupId + " in subject " + subjectCode));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(ResponseDTO.error("Error: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ResponseDTO.error("Internal error: " + e.getMessage()));
        }
    }

    // Endpoint para eliminar una sesión específica de un grupo
    @DeleteMapping("/{subjectCode}/groups/{groupId}/sessions/{sessionIndex}")
    public ResponseEntity<ResponseDTO<String>> removeSessionFromGroup(
            @PathVariable String subjectCode,
            @PathVariable int groupId,
            @PathVariable int sessionIndex) {
        try {
            boolean success = subjectService.removeSessionFromGroup(subjectCode, groupId, sessionIndex);
            if (success) {
                return ResponseEntity.ok(ResponseDTO.success("Session removed successfully",
                        "Session " + sessionIndex + " removed from group " + groupId + " in subject " + subjectCode));
            } else {
                return ResponseEntity.badRequest().body(ResponseDTO.error("Failed to remove session from group"));
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(ResponseDTO.error("Error: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(ResponseDTO.error("Internal error: " + e.getMessage()));
        }
    }
}
