package com.example.fantasticosback.controller;

import com.example.fantasticosback.dto.response.DeanOfficeDTO;
import com.example.fantasticosback.dto.response.ResponseDTO;
import com.example.fantasticosback.service.DeanOfficeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Tag(
        name = "Dean Office",
        description = "Gestión de Decanaturas: registro, consulta, actualización y eliminación"
)
@RestController
@RequestMapping("/api/dean-offices")
public class DeanOfficeController {

    private final DeanOfficeService deanOfficeService;


    public DeanOfficeController(DeanOfficeService deanOfficeService) {
        this.deanOfficeService = deanOfficeService;
    }

    @Operation(
            summary = "Crear una decanatura",
            description = "Crea una nueva decanatura asociada a una facultad. " +
                    "Solo puede existir una decanatura por facultad."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Decanatura creada correctamente",
            content = @Content(schema = @Schema(implementation = DeanOfficeDTO.class))
    )

    @PostMapping
    public ResponseEntity<ResponseDTO<DeanOfficeDTO>> create(@RequestBody DeanOfficeDTO dto) {
        DeanOfficeDTO saved = deanOfficeService.save(dto);
        return ResponseEntity.ok(ResponseDTO.success(saved, "Dean office created successfully"));
    }

    @Operation(
            summary = "Listar todas las decanaturas",
            description = "Obtiene la lista completa de decanaturas registradas en el sistema"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Lista de decanaturas obtenida correctamente"
    )

    @GetMapping
    public ResponseEntity<ResponseDTO<List<DeanOfficeDTO>>> getAll() {
        List<DeanOfficeDTO> deanOffices = deanOfficeService.findAll();
        return ResponseEntity.ok(ResponseDTO.success(deanOffices, "List of dean offices"));
    }

    @Operation(
            summary = "Buscar una decanatura por ID",
            description = "Obtiene los datos de una decanatura específica según su ID"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Decanatura encontrada",
            content = @Content(schema = @Schema(implementation = DeanOfficeDTO.class))
    )
    @ApiResponse(responseCode = "404", description = "Decanatura no encontrada")

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<DeanOfficeDTO>> getById(@PathVariable String id) {
        DeanOfficeDTO deanOffice = deanOfficeService.findById(id);
        return ResponseEntity.ok(ResponseDTO.success(deanOffice, "Dean office found"));
    }

    @Operation(
            summary = "Actualizar una decanatura",
            description = "Actualiza completamente una decanatura existente (PUT). " +
                    "Si se cambia la facultad, se valida que no exista otra decanatura en esa facultad."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Decanatura actualizada correctamente"
    )

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO<DeanOfficeDTO>> update(@PathVariable String id, @RequestBody DeanOfficeDTO dto) {
        DeanOfficeDTO updated = deanOfficeService.update(id, dto);
        return ResponseEntity.ok(ResponseDTO.success(updated, "Dean office updated successfully"));
    }

    @Operation(
            summary = "Eliminar una decanatura",
            description = "Elimina una decanatura por ID. " +
                    "No se puede eliminar si aún tiene estudiantes asociados."
    )
    @ApiResponse(responseCode = "200", description = "Decanatura eliminada correctamente")
    @ApiResponse(responseCode = "400", description = "No se puede eliminar: tiene estudiantes asignados")

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<Void>> delete(@PathVariable String id) {
        deanOfficeService.delete(id);
        return ResponseEntity.ok(ResponseDTO.success(null, "Dean office deleted successfully"));
    }

    @Operation(
            summary = "Buscar decanatura por facultad",
            description = "Obtiene la decanatura correspondiente a una facultad específica"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Decanatura encontrada por facultad"
    )
    @ApiResponse(responseCode = "404", description = "No existe decanatura para esa facultad")


    @GetMapping("/faculty/{faculty}")
    public ResponseEntity<ResponseDTO<DeanOfficeDTO>> getByFaculty(@PathVariable String faculty) {
        DeanOfficeDTO deanOffice = deanOfficeService.findByFaculty(faculty);
        return ResponseEntity.ok(ResponseDTO.success(deanOffice, "Dean office by faculty"));
    }

    @Operation(
            summary = "Actualizar parcialmente una decanatura",
            description = "Permite actualizar solo algunos campos de una decanatura sin afectar los demás (PATCH)"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Decanatura actualizada parcialmente"
    )

    @PatchMapping("/{id}")
    public ResponseEntity<ResponseDTO<DeanOfficeDTO>> patch(
            @PathVariable String id,
            @RequestBody DeanOfficeDTO dto) {

        DeanOfficeDTO patched = deanOfficeService.patch(id, dto);
        return ResponseEntity.ok(ResponseDTO.success(patched, "Dean office partially updated"));
    }
}
