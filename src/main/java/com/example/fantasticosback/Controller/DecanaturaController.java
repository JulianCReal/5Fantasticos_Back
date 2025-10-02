package com.example.fantasticosback.Controller;

import com.example.fantasticosback.Dtos.DeanOfficeDTO;
import com.example.fantasticosback.Dtos.ResponseDTO;
import com.example.fantasticosback.Server.DecanaturaService;
import com.example.fantasticosback.Model.Decanatura;
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
    public ResponseEntity<ResponseDTO<DeanOfficeDTO>> crear(@RequestBody DeanOfficeDTO dto) {
        Decanatura guardada = decanaturaService.guardar(decanaturaService.fromDTO(dto));
        return ResponseEntity.ok(ResponseDTO.success(decanaturaService.toDTO(guardada), "Dean office created successfully"));
    }

    @GetMapping
    public ResponseEntity<ResponseDTO<List<DeanOfficeDTO>>> listar() {
        List<Decanatura> decanaturas = decanaturaService.obtenerTodos();
        return ResponseEntity.ok(ResponseDTO.success(decanaturaService.toDTOList(decanaturas), "List of dean offices"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<DeanOfficeDTO>> obtener(@PathVariable String id) {
        Decanatura decanatura = decanaturaService.obtenerPorId(id);
        if (decanatura != null) {
            return ResponseEntity.ok(ResponseDTO.success(decanaturaService.toDTO(decanatura), "Dean office found"));
        }
        return ResponseEntity.status(404).body(ResponseDTO.error("Dean office not found"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO<DeanOfficeDTO>> actualizar(@PathVariable String id, @RequestBody DeanOfficeDTO dto) {
        Decanatura existente = decanaturaService.obtenerPorId(id);
        if (existente != null) {
            Decanatura actualizado = decanaturaService.actualizar(decanaturaService.fromDTO(dto));
            actualizado.setId(id);
            return ResponseEntity.ok(ResponseDTO.success(decanaturaService.toDTO(actualizado), "Dean office updated successfully"));
        }
        return ResponseEntity.status(404).body(ResponseDTO.error("Dean office not found"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<Void>> eliminar(@PathVariable String id) {
        Decanatura existente = decanaturaService.obtenerPorId(id);
        if (existente != null) {
            decanaturaService.eliminar(id);
            return ResponseEntity.ok(ResponseDTO.success(null, "Dean office deleted successfully"));
        }
        return ResponseEntity.status(404).body(ResponseDTO.error("Dean office not found"));
    }

    @GetMapping("/facultad/{facultad}")
    public ResponseEntity<ResponseDTO<List<DeanOfficeDTO>>> obtenerPorFacultad(@PathVariable String facultad) {
        List<Decanatura> decanaturas = decanaturaService.obtenerPorFacultad(facultad);
        return ResponseEntity.ok(ResponseDTO.success(decanaturaService.toDTOList(decanaturas), "Dean offices by faculty"));
    }
}
