package com.example.fantasticosback.Controller;
import com.example.fantasticosback.Dtos.CreateGroupDTO;
import com.example.fantasticosback.Dtos.ResponseDTO;
import com.example.fantasticosback.Dtos.SubjectDTO;
import com.example.fantasticosback.Server.SubjectService;
import com.example.fantasticosback.Model.Subject;
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
}
