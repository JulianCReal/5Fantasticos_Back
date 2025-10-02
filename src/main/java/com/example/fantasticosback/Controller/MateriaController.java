package com.example.fantasticosback.Controller;


import com.example.fantasticosback.Dtos.ResponseDTO;
import com.example.fantasticosback.Dtos.SubjectDTO;
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
    public ResponseEntity<ResponseDTO<SubjectDTO>> crear(@RequestBody SubjectDTO dto) {
        Materia guardado = materiaService.guardar(materiaService.fromDTO(dto));
        return ResponseEntity.ok(ResponseDTO.success(materiaService.toDTO(guardado), "Subject created successfully"));
    }

    @GetMapping
    public ResponseEntity<ResponseDTO<List<SubjectDTO>>> listar() {
        List<Materia> materias = materiaService.obtenerTodos();
        return ResponseEntity.ok(ResponseDTO.success(materiaService.toDTOList(materias), "List of subjects"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<SubjectDTO>> obtener(@PathVariable String id){
        Materia materia = materiaService.obtenerPorId(id);
        if (materia != null) {
            return ResponseEntity.ok(ResponseDTO.success(materiaService.toDTO(materia), "Subject found"));
        }
        return ResponseEntity.status(404).body(ResponseDTO.error("Subject not found"));
    }

    @PutMapping("/creditos/{creditos}")
    public ResponseEntity<ResponseDTO<List<SubjectDTO>>> actualizarPorCreditos(@PathVariable int creditos, @RequestBody SubjectDTO dto) {
        List<Materia> existentes = materiaService.obtenerPorCreditos(creditos);

        if (existentes.isEmpty()) {
            return ResponseEntity.status(404).body(ResponseDTO.error("No subjects found with the specified credits"));
        }

        for (Materia existente : existentes) {
            existente.setCreditos(dto.getCredits());
            materiaService.guardar(existente);
        }

        return ResponseEntity.ok(ResponseDTO.success(materiaService.toDTOList(existentes), "Subjects updated by credits"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<Void>> eliminar(@PathVariable String id){
        Materia existente = materiaService.obtenerPorId(id);
        if (existente != null){
            materiaService.eliminar(id);
            return ResponseEntity.ok(ResponseDTO.success(null, "Subject deleted successfully"));
        }
        return ResponseEntity.status(404).body(ResponseDTO.error("Subject not found"));
    }

    @GetMapping("/semestre/{semestre}")
    public ResponseEntity<ResponseDTO<List<SubjectDTO>>> obtenerPorSemestre(@PathVariable int semestre){
        List<Materia> materias = materiaService.obtenerPorSemestre(semestre);
        return ResponseEntity.ok(ResponseDTO.success(materiaService.toDTOList(materias), "Subjects by semester"));
    }

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<ResponseDTO<List<SubjectDTO>>> obtenerPorNombre(@PathVariable String nombre){
        List<Materia> materias = materiaService.obtenerPorNombre(nombre);
        return ResponseEntity.ok(ResponseDTO.success(materiaService.toDTOList(materias), "Subjects by name"));
    }
}
