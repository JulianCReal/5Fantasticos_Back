package com.example.fantasticosback.Controller;

import com.example.fantasticosback.Dtos.ResponseDTO;
import com.example.fantasticosback.Dtos.StudentDTO;
import com.example.fantasticosback.Server.StudentService;
import com.example.fantasticosback.Model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @PostMapping
    public ResponseEntity<ResponseDTO<StudentDTO>> create(@RequestBody StudentDTO dto) {
        Student newStudent = studentService.convertToDomain(dto);
        Student saved = studentService.save(newStudent);
        StudentDTO response = studentService.convertToStudentDTO(saved);
        return ResponseEntity.ok(ResponseDTO.success(response, "Student created successfully"));
    }

    @GetMapping
    public ResponseEntity<ResponseDTO<List<StudentDTO>>> list() {
        List<StudentDTO> students = studentService.convertList(studentService.findAll());
        return ResponseEntity.ok(ResponseDTO.success(students, "List of students"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<StudentDTO>> get(@PathVariable String id) {
        Student student = studentService.findById(id);
        if (student == null) {
            return ResponseEntity.status(404).body(ResponseDTO.error("Student not found"));
        }
        StudentDTO dto = studentService.convertToStudentDTO(student);
        return ResponseEntity.ok(ResponseDTO.success(dto, "Student found"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO<StudentDTO>> update(@PathVariable String id, @RequestBody StudentDTO dto) {
        Student existing = studentService.findById(id);
        if (existing == null) {
            return ResponseEntity.status(404).body(ResponseDTO.error("Student not found"));
        }

        Student updated = studentService.convertToDomain(dto);
        updated.setId(id);
        Student saved = studentService.update(updated);
        StudentDTO response = studentService.convertToStudentDTO(saved);

        return ResponseEntity.ok(ResponseDTO.success(response, "Student updated"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<Void>> delete(@PathVariable String id) {
        Student existing = studentService.findById(id);
        if (existing == null) {
            return ResponseEntity.status(404).body(ResponseDTO.error("Student not found"));
        }

        studentService.delete(id);
        return ResponseEntity.ok(ResponseDTO.success(null, "Student deleted successfully"));
    }

    @GetMapping("/career/{career}")
    public ResponseEntity<ResponseDTO<List<StudentDTO>>> getByCareer(@PathVariable String career) {
        List<StudentDTO> students = studentService.convertList(studentService.findByCareer(career));
        return ResponseEntity.ok(ResponseDTO.success(students, "Students by career"));
    }

    @GetMapping("/semester/{semester}")
    public ResponseEntity<ResponseDTO<List<StudentDTO>>> getBySemester(@PathVariable int semester) {
        List<StudentDTO> students = studentService.convertList(studentService.findBySemester(semester));
        return ResponseEntity.ok(ResponseDTO.success(students, "Students by semester"));
    }
}
