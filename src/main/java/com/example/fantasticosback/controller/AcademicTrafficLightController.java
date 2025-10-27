package com.example.fantasticosback.controller;

import com.example.fantasticosback.dto.request.AcademicTrafficLightDTO;
import com.example.fantasticosback.service.AcademicTrafficLightService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/trafficlight")
@CrossOrigin(origins = "*")
public class AcademicTrafficLightController {

    private final AcademicTrafficLightService service;

    public AcademicTrafficLightController(AcademicTrafficLightService service) {
        this.service = service;
    }

    @GetMapping("/{studentId}")
    public ResponseEntity<AcademicTrafficLightDTO> getTrafficLight(@PathVariable String studentId) {
        AcademicTrafficLightDTO dto = service.getAcademicTrafficLight(studentId);
        return ResponseEntity.ok(dto);
    }
}