package com.example.fantasticosback.Controller;

import com.example.fantasticosback.Server.StudentService;
import com.example.fantasticosback.Model.Estudiante;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/estudiantes")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @PostMapping
    public ResponseEntity<Estudiante> crear(@RequestBody Estudiante estudiante) {
        Estudiante guardado = studentService.guardar(estudiante);
        return ResponseEntity.ok(guardado);
    }

    @GetMapping
    public ResponseEntity<List<Estudiante>> listar() {
        List<Estudiante> estudiantes = studentService.obtenerTodos();
        return ResponseEntity.ok(estudiantes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Estudiante> obtener(@PathVariable String id) {
        Estudiante estudiante = studentService.obtenerPorId(id);
        if (estudiante != null) {
            return ResponseEntity.ok(estudiante);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Estudiante> actualizar(@PathVariable String id, @RequestBody Estudiante estudiante) {
        Estudiante existente = studentService.obtenerPorId(id);
        if (existente != null) {
            estudiante.setId(id);
            Estudiante actualizado = studentService.actualizar(estudiante);
            return ResponseEntity.ok(actualizado);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        Estudiante existente = studentService.obtenerPorId(id);
        if (existente != null) {
            studentService.eliminar(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/carrera/{carrera}")
    public ResponseEntity<List<Estudiante>> obtenerPorCarrera(@PathVariable String carrera) {
        List<Estudiante> estudiantes = studentService.obtenerPorCarrera(carrera);
        return ResponseEntity.ok(estudiantes);
    }

    @GetMapping("/semestre/{semestre}")
    public ResponseEntity<List<Estudiante>> obtenerPorSemestre(@PathVariable int semestre) {
        List<Estudiante> estudiantes = studentService.obtenerPorSemestre(semestre);
        return ResponseEntity.ok(estudiantes);
    }
}
