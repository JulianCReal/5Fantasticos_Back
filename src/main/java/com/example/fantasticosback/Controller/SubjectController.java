package com.example.fantasticosback.Controller;


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

    @PostMapping
    public ResponseEntity<ResponseDTO<SubjectDTO>> create(@RequestBody SubjectDTO dto) {
        Subject saved = subjectService.save(subjectService.fromDTO(dto));
        return ResponseEntity.ok(ResponseDTO.success(subjectService.toDTO(saved), "Subject created successfully"));
    }

    @GetMapping
    public ResponseEntity<ResponseDTO<List<SubjectDTO>>> list() {
        List<Subject> subjects = subjectService.findAll();
        return ResponseEntity.ok(ResponseDTO.success(subjectService.toDTOList(subjects), "List of subjects"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<SubjectDTO>> get(@PathVariable String id){
        Subject subject = subjectService.findById(id);
        if (subject != null) {
            return ResponseEntity.ok(ResponseDTO.success(subjectService.toDTO(subject), "Subject found"));
        }
        return ResponseEntity.status(404).body(ResponseDTO.error("Subject not found"));
    }

    @PutMapping("/credits/{credits}")
    public ResponseEntity<ResponseDTO<List<SubjectDTO>>> updateByCredits(@PathVariable int credits, @RequestBody SubjectDTO dto) {
        List<Subject> existing = subjectService.findByCredits(credits);

        if (existing.isEmpty()) {
            return ResponseEntity.status(404).body(ResponseDTO.error("No subjects found with the specified credits"));
        }

        for (Subject existingSubject : existing) {
            existingSubject.setCredits(dto.getCredits());
            subjectService.save(existingSubject);
        }

        return ResponseEntity.ok(ResponseDTO.success(subjectService.toDTOList(existing), "Subjects updated by credits"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<Void>> delete(@PathVariable String id){
        Subject existing = subjectService.findById(id);
        if (existing != null){
            subjectService.delete(id);
            return ResponseEntity.ok(ResponseDTO.success(null, "Subject deleted successfully"));
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
}
