package com.example.fantasticosback.controller;

import com.example.fantasticosback.dto.response.ResponseDTO;
import com.example.fantasticosback.dto.response.TeacherDTO;
import com.example.fantasticosback.service.TeacherService;
import com.example.fantasticosback.model.Document.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teachers")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @PostMapping
    public ResponseEntity<ResponseDTO<TeacherDTO>> create(@RequestBody TeacherDTO dto) {
        Teacher teacher = teacherService.fromDTO(dto);
        Teacher saved = teacherService.save(teacher);
        return ResponseEntity.ok(ResponseDTO.success(teacherService.toDTO(saved), "Teacher created"));
    }

    @GetMapping
    public ResponseEntity<ResponseDTO<List<TeacherDTO>>> list() {
        return ResponseEntity.ok(ResponseDTO.success(
                teacherService.toDTOList(teacherService.findAll()), "List of teachers"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<TeacherDTO>> get(@PathVariable String id) {
        Teacher teacher = teacherService.findById(id);
        if (teacher == null) {
            return ResponseEntity.status(404).body(ResponseDTO.error("Teacher not found"));
        }
        return ResponseEntity.ok(ResponseDTO.success(teacherService.toDTO(teacher), "Teacher found"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO<TeacherDTO>> update(@PathVariable String id, @RequestBody TeacherDTO dto) {
        Teacher existing = teacherService.findById(id);
        if (existing == null) {
            return ResponseEntity.status(404).body(ResponseDTO.error("Teacher not found"));
        }
        Teacher teacher = teacherService.fromDTO(dto);
        teacher.setId(id);
        Teacher updated = teacherService.update(teacher);
        return ResponseEntity.ok(ResponseDTO.success(teacherService.toDTO(updated), "Teacher updated"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<Void>> delete(@PathVariable String id) {
        Teacher existing = teacherService.findById(id);
        if (existing == null) {
            return ResponseEntity.status(404).body(ResponseDTO.error("Teacher not found"));
        }
        teacherService.delete(id);
        return ResponseEntity.ok(ResponseDTO.success(null, "Teacher deleted"));
    }

    @GetMapping("/department/{department}")
    public ResponseEntity<ResponseDTO<List<TeacherDTO>>> getByDepartment(@PathVariable String department) {
        return ResponseEntity.ok(ResponseDTO.success(
                teacherService.toDTOList(teacherService.findByDepartment(department)), "Teachers by department"));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<ResponseDTO<List<TeacherDTO>>> getByName(@PathVariable String name) {
        return ResponseEntity.ok(ResponseDTO.success(
                teacherService.toDTOList(teacherService.findByName(name)), "Teachers by name"));
    }

    @GetMapping("/lastname/{lastName}")
    public ResponseEntity<ResponseDTO<List<TeacherDTO>>> getByLastName(@PathVariable String lastName) {
        return ResponseEntity.ok(ResponseDTO.success(
                teacherService.toDTOList(teacherService.findByLastName(lastName)), "Teachers by last name"));
    }
}
