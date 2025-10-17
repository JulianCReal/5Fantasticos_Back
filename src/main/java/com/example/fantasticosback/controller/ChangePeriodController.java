package com.example.fantasticosback.controller;

import com.example.fantasticosback.model.Document.ChangePeriod;
import com.example.fantasticosback.service.ChangePeriodService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/change-periods")
public class ChangePeriodController {

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

