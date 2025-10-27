package com.example.fantasticosback.controller;

import com.example.fantasticosback.service.AcademicTrafficLightService;
import com.example.fantasticosback.util.AcademicTrafficLight;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/academic-traffic-light")
public class AcademicTrafficLightController {

    @Autowired
    private AcademicTrafficLightService service;

    /**
     * 🔹 Visualizar el semáforo académico por estudiante
     */
    @GetMapping("/student/{studentId}")
    public ResponseEntity<AcademicTrafficLight> getTrafficLight(@PathVariable String studentId) {
        AcademicTrafficLight result = service.getAcademicTrafficLightByStudent(studentId);
        return ResponseEntity.ok(result);
    }
}
