package com.example.fantasticosback.Controller;

import com.example.fantasticosback.Dtos.ResponseDTO;
import com.example.fantasticosback.Dtos.StudentDTO;
import com.example.fantasticosback.Model.Student;
import com.example.fantasticosback.Server.StudentService;
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
        Student nuevo = studentService.convertirADominio(dto);
        Student guardado = studentService.guardar(nuevo);
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
        Student student = studentService.obtenerPorId(id);
        if (student == null) {
            return ResponseEntity.status(404).body(ResponseDTO.error("Estudiante no encontrado"));
        }
        StudentDTO dto = studentService.convertirAEstudianteDTO(student);
        return ResponseEntity.ok(ResponseDTO.success(dto, "Estudiante encontrado"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO<StudentDTO>> actualizar(@PathVariable String id, @RequestBody StudentDTO dto) {
        Student existente = studentService.obtenerPorId(id);
        if (existente == null) {
            return ResponseEntity.status(404).body(ResponseDTO.error("Estudiante no encontrado"));
        }

        Student actualizado = studentService.convertirADominio(dto);
        actualizado.setId(id);
        Student guardado = studentService.actualizar(actualizado);
        StudentDTO respuesta = studentService.convertirAEstudianteDTO(guardado);

        return ResponseEntity.ok(ResponseDTO.success(respuesta, "Estudiante actualizado"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<Void>> eliminar(@PathVariable String id) {
        Student existente = studentService.obtenerPorId(id);
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
