package com.example.fantasticosback.Controller;

import com.example.fantasticosback.Server.TeacherService;
import com.example.fantasticosback.util.Profesor;
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
    public ResponseEntity<Profesor> crear(@RequestBody Profesor profesor) {
        Profesor guardado = teacherService.guardar(profesor);
        return ResponseEntity.ok(guardado);
    }

    @GetMapping
    public ResponseEntity<List<Profesor>> listar() {
        List<Profesor> profesores = teacherService.obtenerTodos();
        return ResponseEntity.ok(profesores);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Profesor> obtener(@PathVariable String id) {
        Profesor profesor = teacherService.obtenerPorId(id);
        if (profesor != null) {
            return ResponseEntity.ok(profesor);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Profesor> actualizar(@PathVariable String id, @RequestBody Profesor profesor) {
        Profesor existente = teacherService.obtenerPorId(id);
        if (existente != null) {
            profesor.setId(id);
            Profesor actualizado = teacherService.actualizar(profesor);
            return ResponseEntity.ok(actualizado);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        Profesor existente = teacherService.obtenerPorId(id);
        if (existente != null) {
            teacherService.eliminar(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/departamento/{departamento}")
    public ResponseEntity<List<Profesor>> obtenerPorDepartamento(@PathVariable String departamento) {
        List<Profesor> profesores = teacherService.obtenerPorDepartamento(departamento);
        return ResponseEntity.ok(profesores);
    }

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<List<Profesor>> obtenerPorNombre(@PathVariable String nombre) {
        List<Profesor> profesores = teacherService.obtenerPorNombre(nombre);
        return ResponseEntity.ok(profesores);
    }

    @GetMapping("/apellido/{apellido}")
    public ResponseEntity<List<Profesor>> obtenerPorApellido(@PathVariable String apellido) {
        List<Profesor> profesores = teacherService.obtenerPorApellido(apellido);
        return ResponseEntity.ok(profesores);
    }
}
