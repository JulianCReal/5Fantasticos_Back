package com.example.fantasticosback.controller;

import com.example.fantasticosback.util.AcademicTrafficLight;
import com.example.fantasticosback.service.AcademicTrafficLightService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/academic-traffic-lights")
@RequiredArgsConstructor
@Tag(name = "Semáforo Académico", description = "API para gestión del semáforo académico de estudiantes")
public class AcademicTrafficLightController {

    private final AcademicTrafficLightService academicTrafficLightService;

    @Operation(summary = "Obtener semáforo académico por ID de estudiante")
    @GetMapping("/student/{studentId}")
    public ResponseEntity<AcademicTrafficLight> getByStudentId(@PathVariable String studentId) {
        AcademicTrafficLight trafficLight = academicTrafficLightService.findByStudentId(studentId);
        return ResponseEntity.ok(trafficLight);
    }

    @Operation(summary = "Obtener todos los semáforos académicos")
    @GetMapping
    public ResponseEntity<List<AcademicTrafficLight>> getAll() {
        List<AcademicTrafficLight> trafficLights = academicTrafficLightService.findAll();
        return ResponseEntity.ok(trafficLights);
    }

    @Operation(summary = "Crear nuevo semáforo académico")
    @PostMapping
    public ResponseEntity<AcademicTrafficLight> create(@RequestBody AcademicTrafficLight trafficLight) {
        AcademicTrafficLight created = academicTrafficLightService.save(trafficLight);
        return ResponseEntity.ok(created);
    }

    @Operation(summary = "Actualizar semáforo académico")
    @PutMapping("/{id}")
    public ResponseEntity<AcademicTrafficLight> update(@PathVariable String id, @RequestBody AcademicTrafficLight trafficLight) {
        AcademicTrafficLight updated = academicTrafficLightService.update(id, trafficLight);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Recalcular estado académico")
    @PostMapping("/student/{studentId}/recalculate")
    public ResponseEntity<AcademicTrafficLight> recalculate(@PathVariable String studentId) {
        AcademicTrafficLight recalculated = academicTrafficLightService.recalculateAcademicStatus(studentId);
        return ResponseEntity.ok(recalculated);
    }

    @Operation(summary = "Eliminar semáforo académico")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        academicTrafficLightService.delete(id);
        return ResponseEntity.noContent().build();
    }
}