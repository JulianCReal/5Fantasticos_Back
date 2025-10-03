package com.example.fantasticosback.Controller;

import com.example.fantasticosback.Dtos.DeanOfficeDTO;
import com.example.fantasticosback.Dtos.ResponseDTO;
import com.example.fantasticosback.Server.DeanOfficeService;
import com.example.fantasticosback.Model.DeanOffice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/decanaturas")
public class DeanOfficeController {

    @Autowired
    private DeanOfficeService deanOfficeService;

    @PostMapping
    public ResponseEntity<ResponseDTO<DeanOfficeDTO>> crear(@RequestBody DeanOfficeDTO dto) {
        DeanOffice guardada = deanOfficeService.guardar(deanOfficeService.fromDTO(dto));
        return ResponseEntity.ok(ResponseDTO.success(deanOfficeService.toDTO(guardada), "Dean office created successfully"));
    }

    @GetMapping
    public ResponseEntity<ResponseDTO<List<DeanOfficeDTO>>> listar() {
        List<DeanOffice> deanOffices = deanOfficeService.obtenerTodos();
        return ResponseEntity.ok(ResponseDTO.success(deanOfficeService.toDTOList(deanOffices), "List of dean offices"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<DeanOfficeDTO>> obtener(@PathVariable String id) {
        DeanOffice deanOffice = deanOfficeService.obtenerPorId(id);
        if (deanOffice != null) {
            return ResponseEntity.ok(ResponseDTO.success(deanOfficeService.toDTO(deanOffice), "Dean office found"));
        }
        return ResponseEntity.status(404).body(ResponseDTO.error("Dean office not found"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO<DeanOfficeDTO>> actualizar(@PathVariable String id, @RequestBody DeanOfficeDTO dto) {
        DeanOffice existente = deanOfficeService.obtenerPorId(id);
        if (existente != null) {
            DeanOffice actualizado = deanOfficeService.actualizar(deanOfficeService.fromDTO(dto));
            actualizado.setId(id);
            return ResponseEntity.ok(ResponseDTO.success(deanOfficeService.toDTO(actualizado), "Dean office updated successfully"));
        }
        return ResponseEntity.status(404).body(ResponseDTO.error("Dean office not found"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<Void>> eliminar(@PathVariable String id) {
        DeanOffice existente = deanOfficeService.obtenerPorId(id);
        if (existente != null) {
            deanOfficeService.eliminar(id);
            return ResponseEntity.ok(ResponseDTO.success(null, "Dean office deleted successfully"));
        }
        return ResponseEntity.status(404).body(ResponseDTO.error("Dean office not found"));
    }

    @GetMapping("/facultad/{facultad}")
    public ResponseEntity<ResponseDTO<List<DeanOfficeDTO>>> obtenerPorFacultad(@PathVariable String facultad) {
        List<DeanOffice> deanOffices = deanOfficeService.obtenerPorFacultad(facultad);
        return ResponseEntity.ok(ResponseDTO.success(deanOfficeService.toDTOList(deanOffices), "Dean offices by faculty"));
    }
}
