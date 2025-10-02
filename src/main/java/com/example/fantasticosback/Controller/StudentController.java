package com.example.fantasticosback.Controller;

import com.example.fantasticosback.Dtos.ResponseDTO;
import com.example.fantasticosback.Dtos.StudentDTO;
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
    public ResponseEntity<ResponseDTO<StudentDTO>> crear(@RequestBody StudentDTO dto) {
        Estudiante nuevo = studentService.convertirADominio(dto);
        Estudiante guardado = studentService.guardar(nuevo);
        StudentDTO respuesta = studentService.convertirAEstudianteDTO(guardado);
        return ResponseEntity.ok(ResponseDTO.success(respuesta, "Estudiante creado correctamente"));
    }

    @GetMapping
    public ResponseEntity<ResponseDTO<List<StudentDTO>>> listar() {
        List<StudentDTO> estudiantes = studentService.convertirLista(studentService.obtenerTodos());
        return ResponseEntity.ok(ResponseDTO.success(estudiantes, "Lista de estudiantes"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<StudentDTO>> obtener(@PathVariable String id) {
        Estudiante estudiante = studentService.obtenerPorId(id);
        if (estudiante == null) {
            return ResponseEntity.status(404).body(ResponseDTO.error("Estudiante no encontrado"));
        }
        StudentDTO dto = studentService.convertirAEstudianteDTO(estudiante);
        return ResponseEntity.ok(ResponseDTO.success(dto, "Estudiante encontrado"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO<StudentDTO>> actualizar(@PathVariable String id, @RequestBody StudentDTO dto) {
        Estudiante existente = studentService.obtenerPorId(id);
        if (existente == null) {
            return ResponseEntity.status(404).body(ResponseDTO.error("Estudiante no encontrado"));
        }

        Estudiante actualizado = studentService.convertirADominio(dto);
        actualizado.setId(id);
        Estudiante guardado = studentService.actualizar(actualizado);
        StudentDTO respuesta = studentService.convertirAEstudianteDTO(guardado);

        return ResponseEntity.ok(ResponseDTO.success(respuesta, "Estudiante actualizado"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<Void>> eliminar(@PathVariable String id) {
        Estudiante existente = studentService.obtenerPorId(id);
        if (existente == null) {
            return ResponseEntity.status(404).body(ResponseDTO.error("Estudiante no encontrado"));
        }

        studentService.eliminar(id);
        return ResponseEntity.ok(ResponseDTO.success(null, "Estudiante eliminado correctamente"));
    }

    @GetMapping("/carrera/{carrera}")
    public ResponseEntity<ResponseDTO<List<StudentDTO>>> obtenerPorCarrera(@PathVariable String carrera) {
        List<StudentDTO> estudiantes = studentService.convertirLista(studentService.obtenerPorCarrera(carrera));
        return ResponseEntity.ok(ResponseDTO.success(estudiantes, "Estudiantes por carrera"));
    }

    @GetMapping("/semestre/{semestre}")
    public ResponseEntity<ResponseDTO<List<StudentDTO>>> obtenerPorSemestre(@PathVariable int semestre) {
        List<StudentDTO> estudiantes = studentService.convertirLista(studentService.obtenerPorSemestre(semestre));
        return ResponseEntity.ok(ResponseDTO.success(estudiantes, "Estudiantes por semestre"));
    }
}
