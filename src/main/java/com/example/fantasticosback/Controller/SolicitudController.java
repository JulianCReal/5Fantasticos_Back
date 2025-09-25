package com.example.fantasticosback.Controller;


import com.example.fantasticosback.Model.Solicitud;
import com.example.fantasticosback.Server.SolicitudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/solicitudes")
public class SolicitudController {
    @Autowired
    private SolicitudService solicitudService;

    @PostMapping
    public ResponseEntity<Solicitud> crear(@RequestBody Solicitud decanatura) {
        Solicitud guardada = solicitudService.guardar(decanatura);
        return ResponseEntity.ok(guardada);
    }

    @GetMapping
    public ResponseEntity<List<Solicitud>> listar() {
        return ResponseEntity.ok(solicitudService.encontrarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Solicitud> obtener(@PathVariable String id) {
        Solicitud decanatura = solicitudService.obtenerPorId(id);
        if (decanatura != null) {
            return ResponseEntity.ok(decanatura);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Solicitud> actualizar(@PathVariable String id, @RequestBody Solicitud solicitud) {
        Solicitud existente = solicitudService.obtenerPorId(id);
        if (existente != null) {
            solicitud.setId(id);
            Solicitud actualizado = solicitudService.actualizar(solicitud);
            return ResponseEntity.ok(actualizado);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        Solicitud existente = solicitudService.obtenerPorId(id);
        if (existente != null) {
            solicitudService.eliminar(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/nombreEstado/{nombreEstado}")
    public ResponseEntity<List<Solicitud>> obtenerPorEstado(@PathVariable String nombreEstado) {
        return ResponseEntity.ok(solicitudService.obtenerPorNombreEstado(nombreEstado));
    }
}
