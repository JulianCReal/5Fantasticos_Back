package com.example.fantasticosback.Controller;


import com.example.fantasticosback.Server.MateriaService;
import com.example.fantasticosback.Model.Materia;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/materias")
public class MateriaController {

    @Autowired
    private MateriaService materiaService;

    @PostMapping
    public ResponseEntity<Materia> crear(@RequestBody Materia materia) {
        Materia guardado = materiaService.guardar(materia);
        return ResponseEntity.ok(guardado);
    }

    @GetMapping
    public ResponseEntity<List<Materia>> listar() {
        List<Materia> materias = materiaService.obtenerTodos();
        return ResponseEntity.ok(materias);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Materia> obtener(@PathVariable String id){
        Materia materia = materiaService.obtenerPorId(id);
        if (materia != null) {
            return ResponseEntity.ok(materia);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/creditos/{creditos}")
    public ResponseEntity<List<Materia>> actualizarPorCreditos(@PathVariable int creditos, @RequestBody Materia materia) {
        List<Materia> existentes = materiaService.obtenerPorCreditos(creditos);

        if (existentes.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        for (Materia existente : existentes) {
            existente.setCreditos(materia.getCreditos());
            materiaService.guardar(existente);
        }

        return ResponseEntity.ok(existentes);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id){
        Materia existente = materiaService.obtenerPorId(id);
        if (existente != null){
            materiaService.eliminar(id);
            return  ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/semestre/{semestre}")
    public ResponseEntity<List<Materia>> obtenerPorSemestre(@PathVariable int semestre){
        List<Materia> materias = materiaService.obtenerPorSemestre(semestre);
        return ResponseEntity.ok(materias);
    }

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<List<Materia>> obtenerPorNombre(@PathVariable String nombre){
        List<Materia> materias = materiaService.obtenerPorNombre(nombre);
        return  ResponseEntity.ok(materias);
    }
}
