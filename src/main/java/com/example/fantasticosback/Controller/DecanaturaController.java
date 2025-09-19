package com.example.fantasticosback.Controller;

import com.example.fantasticosback.Server.DecanaturaService;
import com.example.fantasticosback.util.Decanatura;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/decanaturas")
public class DecanaturaController {

    @Autowired
    private DecanaturaService decanaturaService;

    @PostMapping
    public ResponseEntity<Decanatura> crear(@RequestBody Decanatura decanatura) {
        Decanatura guardada = decanaturaService.guardar(decanatura);
        return ResponseEntity.ok(guardada);
    }

    @GetMapping
    public ResponseEntity<List<Decanatura>> listar() {
        return ResponseEntity.ok(decanaturaService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Decanatura> obtener(@PathVariable String id) {
        Decanatura decanatura = decanaturaService.obtenerPorId(id);
        if (decanatura != null) {
            return ResponseEntity.ok(decanatura);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Decanatura> actualizar(@PathVariable String id, @RequestBody Decanatura decanatura) {
        Decanatura existente = decanaturaService.obtenerPorId(id);
        if (existente != null) {
            decanatura.setId(id);
            Decanatura actualizado = decanaturaService.actualizar(decanatura);
            return ResponseEntity.ok(actualizado);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        Decanatura existente = decanaturaService.obtenerPorId(id);
        if (existente != null) {
            decanaturaService.eliminar(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/facultad/{facultad}")
    public ResponseEntity<List<Decanatura>> obtenerPorFacultad(@PathVariable String facultad) {
        return ResponseEntity.ok(decanaturaService.obtenerPorFacultad(facultad));
    }
}
