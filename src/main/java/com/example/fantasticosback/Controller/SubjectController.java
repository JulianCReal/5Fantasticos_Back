package com.example.fantasticosback.Controller;


import com.example.fantasticosback.Dtos.ResponseDTO;
import com.example.fantasticosback.Dtos.SubjectDTO;
import com.example.fantasticosback.Model.Subject;
import com.example.fantasticosback.Server.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/materias")
public class SubjectController {
    @Autowired
    private SubjectService subjectService;

    @PostMapping
    public ResponseEntity<ResponseDTO<SubjectDTO>> crear(@RequestBody SubjectDTO dto) {
        Subject guardado = subjectService.guardar(subjectService.fromDTO(dto));
        return ResponseEntity.ok(ResponseDTO.success(subjectService.toDTO(guardado), "Subject created successfully"));
    }

    @GetMapping
    public ResponseEntity<ResponseDTO<List<SubjectDTO>>> listar() {
        List<Subject> subjects = subjectService.obtenerTodos();
        return ResponseEntity.ok(ResponseDTO.success(subjectService.toDTOList(subjects), "List of subjects"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<SubjectDTO>> obtener(@PathVariable String id){
        Subject subject = subjectService.obtenerPorId(id);
        if (subject != null) {
            return ResponseEntity.ok(ResponseDTO.success(subjectService.toDTO(subject), "Subject found"));
        }
        return ResponseEntity.status(404).body(ResponseDTO.error("Subject not found"));
    }

    @PutMapping("/creditos/{creditos}")
    public ResponseEntity<ResponseDTO<List<SubjectDTO>>> actualizarPorCreditos(@PathVariable int creditos, @RequestBody SubjectDTO dto) {
        List<Subject> existentes = subjectService.obtenerPorCreditos(creditos);

        if (existentes.isEmpty()) {
            return ResponseEntity.status(404).body(ResponseDTO.error("No subjects found with the specified credits"));
        }

        for (Subject existente : existentes) {
            existente.setCredits(dto.getCredits());
            subjectService.guardar(existente);
        }

        return ResponseEntity.ok(ResponseDTO.success(subjectService.toDTOList(existentes), "Subjects updated by credits"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<Void>> eliminar(@PathVariable String id){
        Subject existente = subjectService.obtenerPorId(id);
        if (existente != null){
            subjectService.eliminar(id);
            return ResponseEntity.ok(ResponseDTO.success(null, "Subject deleted successfully"));
        }
        return ResponseEntity.status(404).body(ResponseDTO.error("Subject not found"));
    }

    @GetMapping("/semestre/{semestre}")
    public ResponseEntity<ResponseDTO<List<SubjectDTO>>> obtenerPorSemestre(@PathVariable int semestre){
        List<Subject> subjects = subjectService.obtenerPorSemestre(semestre);
        return ResponseEntity.ok(ResponseDTO.success(subjectService.toDTOList(subjects), "Subjects by semester"));
    }

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<ResponseDTO<List<SubjectDTO>>> obtenerPorNombre(@PathVariable String nombre){
        List<Subject> subjects = subjectService.obtenerPorNombre(nombre);
        return ResponseEntity.ok(ResponseDTO.success(subjectService.toDTOList(subjects), "Subjects by name"));
    }
}
