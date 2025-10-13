package com.example.fantasticosback.Persistence.Controller;



import com.example.fantasticosback.Dtos.DeanDTO;
import com.example.fantasticosback.Persistence.Service.DeanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Tag(name = "Dean", description = "Gestión de Decanos y respuestas de solicitudes")

@RestController
@RequestMapping("/api/deans")
public class DeanController {

    private final DeanService deanService;

    public DeanController(DeanService deanService) {
        this.deanService = deanService;
    }
    @Operation(
            summary = "Obtener todos los Decanos",
            description = "Retorna la lista completa de Decanos registrados en el sistema"
    )
    @ApiResponse(responseCode = "200", description = "Lista de decanos obtenida exitosamente")

    @GetMapping
    public ResponseEntity<List<DeanDTO>> getAll() {
        return ResponseEntity.ok(deanService.getAll());
    }

    @Operation(summary = "Obtener un Decano por ID")
    @ApiResponse(responseCode = "200", description = "Decano encontrado")
    @ApiResponse(responseCode = "404", description = "Decano no encontrado")

    @GetMapping("/{id}")
    public ResponseEntity<DeanDTO> getById(@PathVariable String id) {
        return ResponseEntity.ok(deanService.getById(id));
    }

    @Operation(summary = "Crear un nuevo Decano")
    @ApiResponse(responseCode = "200", description = "Decano creado correctamente")

    @PostMapping
    public ResponseEntity<DeanDTO> create(@RequestBody DeanDTO dto) {
        return ResponseEntity.ok(deanService.create(dto));
    }

    @Operation(summary = "Actualizar un Decano existente")
    @ApiResponse(responseCode = "200", description = "Decano actualizado correctamente")

    @PutMapping("/{id}")
    public ResponseEntity<DeanDTO> update(@PathVariable String id, @RequestBody DeanDTO dto) {
        return ResponseEntity.ok(deanService.update(id, dto));
    }

    @Operation(summary = "Eliminar un Decano")
    @ApiResponse(responseCode = "204", description = "Decano eliminado exitosamente")

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        deanService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /*
    @PostMapping("/{faculty}/requests/{requestId}/response")
    public ResponseEntity<Void> respondRequest(@PathVariable String faculty,
                                               @PathVariable String requestId,
                                               @RequestBody String response) {
        deanService.respondRequest(faculty, requestId, response);
        return ResponseEntity.ok().build();
    }*/
}
