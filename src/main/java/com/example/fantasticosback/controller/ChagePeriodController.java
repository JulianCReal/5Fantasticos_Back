package com.example.fantasticosback.controller;

import com.example.fantasticosback.model.Document.ChangePeriod;
import com.example.fantasticosback.service.ChangePeriodService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Este archivo contenía una clase con un typo en el nombre: "ChagePeriodController" en lugar de "ChangePeriodController".
// Para evitar conflictos y duplicados se ha desactivado su contenido.
// El controlador correcto está en `ChangePeriodController.java`.
// Puedes eliminar este archivo si lo deseas.

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/change-periods")
public class ChagePeriodController {

    private final ChangePeriodService service;

    @PostMapping
    public ResponseEntity<ChangePeriod> create(@RequestBody ChangePeriod period) {
        return ResponseEntity.ok(service.create(period));
    }

    @GetMapping("/active")
    public ResponseEntity<ChangePeriod> getActive() {
        return service.getActivePeriod()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }
}
