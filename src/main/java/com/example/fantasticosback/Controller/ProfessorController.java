package com.example.fantasticosback.Controller;

import com.example.fantasticosback.Dtos.ResponseDTO;
import com.example.fantasticosback.Dtos.TeacherDTO;
import com.example.fantasticosback.Server.ProfessorService;
import com.example.fantasticosback.Model.Professor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profesores")
public class ProfessorController {

    @Autowired
    private ProfessorService professorService;

    @PostMapping
    public ResponseEntity<ResponseDTO<TeacherDTO>> create(@RequestBody TeacherDTO dto) {
        Professor professor = professorService.fromDTO(dto);
        Professor saved = professorService.guardar(professor);
        return ResponseEntity.ok(ResponseDTO.success(professorService.toDTO(saved), "Teacher created"));
    }

    @GetMapping
    public ResponseEntity<ResponseDTO<List<TeacherDTO>>> list() {
        return ResponseEntity.ok(ResponseDTO.success(
                professorService.toDTOList(professorService.obtenerTodos()), "List of teachers"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<TeacherDTO>> get(@PathVariable String id) {
        Professor professor = professorService.obtenerPorId(id);
        if (professor == null) {
            return ResponseEntity.status(404).body(ResponseDTO.error("Teacher not found"));
        }
        return ResponseEntity.ok(ResponseDTO.success(professorService.toDTO(professor), "Teacher found"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO<TeacherDTO>> update(@PathVariable String id, @RequestBody TeacherDTO dto) {
        Professor existing = professorService.obtenerPorId(id);
        if (existing == null) {
            return ResponseEntity.status(404).body(ResponseDTO.error("Teacher not found"));
        }
        Professor professor = professorService.fromDTO(dto);
        professor.setId(id);
        Professor updated = professorService.actualizar(professor);
        return ResponseEntity.ok(ResponseDTO.success(professorService.toDTO(updated), "Teacher updated"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<Void>> delete(@PathVariable String id) {
        Professor existing = professorService.obtenerPorId(id);
        if (existing == null) {
            return ResponseEntity.status(404).body(ResponseDTO.error("Teacher not found"));
        }
        professorService.eliminar(id);
        return ResponseEntity.ok(ResponseDTO.success(null, "Teacher deleted"));
    }

    @GetMapping("/department/{department}")
    public ResponseEntity<ResponseDTO<List<TeacherDTO>>> byDepartment(@PathVariable String department) {
        return ResponseEntity.ok(ResponseDTO.success(
                professorService.toDTOList(professorService.obtenerPorDepartamento(department)), "Teachers by department"));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<ResponseDTO<List<TeacherDTO>>> byName(@PathVariable String name) {
        return ResponseEntity.ok(ResponseDTO.success(
                professorService.toDTOList(professorService.obtenerPorNombre(name)), "Teachers by name"));
    }

    @GetMapping("/lastname/{lastName}")
    public ResponseEntity<ResponseDTO<List<TeacherDTO>>> byLastName(@PathVariable String lastName) {
        return ResponseEntity.ok(ResponseDTO.success(
                professorService.toDTOList(professorService.obtenerPorApellido(lastName)), "Teachers by last name"));
    }
}
