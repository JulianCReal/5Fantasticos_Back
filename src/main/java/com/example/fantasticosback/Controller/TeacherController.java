package com.example.fantasticosback.Controller;

import com.example.fantasticosback.Dtos.ResponseDTO;
import com.example.fantasticosback.Dtos.TeacherDTO;
import com.example.fantasticosback.Server.TeacherService;
import com.example.fantasticosback.Model.Profesor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profesores")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @PostMapping
    public ResponseEntity<ResponseDTO<TeacherDTO>> create(@RequestBody TeacherDTO dto) {
        Profesor profesor = teacherService.fromDTO(dto);
        Profesor saved = teacherService.guardar(profesor);
        return ResponseEntity.ok(ResponseDTO.success(teacherService.toDTO(saved), "Teacher created"));
    }

    @GetMapping
    public ResponseEntity<ResponseDTO<List<TeacherDTO>>> list() {
        return ResponseEntity.ok(ResponseDTO.success(
                teacherService.toDTOList(teacherService.obtenerTodos()), "List of teachers"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<TeacherDTO>> get(@PathVariable String id) {
        Profesor profesor = teacherService.obtenerPorId(id);
        if (profesor == null) {
            return ResponseEntity.status(404).body(ResponseDTO.error("Teacher not found"));
        }
        return ResponseEntity.ok(ResponseDTO.success(teacherService.toDTO(profesor), "Teacher found"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO<TeacherDTO>> update(@PathVariable String id, @RequestBody TeacherDTO dto) {
        Profesor existing = teacherService.obtenerPorId(id);
        if (existing == null) {
            return ResponseEntity.status(404).body(ResponseDTO.error("Teacher not found"));
        }
        Profesor profesor = teacherService.fromDTO(dto);
        profesor.setId(id);
        Profesor updated = teacherService.actualizar(profesor);
        return ResponseEntity.ok(ResponseDTO.success(teacherService.toDTO(updated), "Teacher updated"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<Void>> delete(@PathVariable String id) {
        Profesor existing = teacherService.obtenerPorId(id);
        if (existing == null) {
            return ResponseEntity.status(404).body(ResponseDTO.error("Teacher not found"));
        }
        teacherService.eliminar(id);
        return ResponseEntity.ok(ResponseDTO.success(null, "Teacher deleted"));
    }

    @GetMapping("/department/{department}")
    public ResponseEntity<ResponseDTO<List<TeacherDTO>>> byDepartment(@PathVariable String department) {
        return ResponseEntity.ok(ResponseDTO.success(
                teacherService.toDTOList(teacherService.obtenerPorDepartamento(department)), "Teachers by department"));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<ResponseDTO<List<TeacherDTO>>> byName(@PathVariable String name) {
        return ResponseEntity.ok(ResponseDTO.success(
                teacherService.toDTOList(teacherService.obtenerPorNombre(name)), "Teachers by name"));
    }

    @GetMapping("/lastname/{lastName}")
    public ResponseEntity<ResponseDTO<List<TeacherDTO>>> byLastName(@PathVariable String lastName) {
        return ResponseEntity.ok(ResponseDTO.success(
                teacherService.toDTOList(teacherService.obtenerPorApellido(lastName)), "Teachers by last name"));
    }
}
