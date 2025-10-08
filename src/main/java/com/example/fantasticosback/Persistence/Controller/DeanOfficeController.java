package com.example.fantasticosback.Persistence.Controller;

import com.example.fantasticosback.Dtos.DeanOfficeDTO;
import com.example.fantasticosback.Dtos.ResponseDTO;
import com.example.fantasticosback.Persistence.Server.DeanOfficeService;
import com.example.fantasticosback.Model.Entities.DeanOffice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dean-offices")
public class DeanOfficeController {

    @Autowired
    private DeanOfficeService deanOfficeService;

    @PostMapping
    public ResponseEntity<ResponseDTO<DeanOfficeDTO>> create(@RequestBody DeanOfficeDTO dto) {
        DeanOffice saved = deanOfficeService.save(deanOfficeService.fromDTO(dto));
        return ResponseEntity.ok(ResponseDTO.success(deanOfficeService.toDTO(saved), "Dean office created successfully"));
    }

    @GetMapping
    public ResponseEntity<ResponseDTO<List<DeanOfficeDTO>>> list() {
        List<DeanOffice> deanOffices = deanOfficeService.findAll();
        return ResponseEntity.ok(ResponseDTO.success(deanOfficeService.toDTOList(deanOffices), "List of dean offices"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<DeanOfficeDTO>> get(@PathVariable String id) {
        DeanOffice deanOffice = deanOfficeService.findById(id);
        if (deanOffice != null) {
            return ResponseEntity.ok(ResponseDTO.success(deanOfficeService.toDTO(deanOffice), "Dean office found"));
        }
        return ResponseEntity.status(404).body(ResponseDTO.error("Dean office not found"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO<DeanOfficeDTO>> update(@PathVariable String id, @RequestBody DeanOfficeDTO dto) {
        DeanOffice existing = deanOfficeService.findById(id);
        if (existing != null) {
            DeanOffice updated = deanOfficeService.update(deanOfficeService.fromDTO(dto));
            updated.setId(id);
            return ResponseEntity.ok(ResponseDTO.success(deanOfficeService.toDTO(updated), "Dean office updated successfully"));
        }
        return ResponseEntity.status(404).body(ResponseDTO.error("Dean office not found"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<Void>> delete(@PathVariable String id) {
        DeanOffice existing = deanOfficeService.findById(id);
        if (existing != null) {
            deanOfficeService.delete(id);
            return ResponseEntity.ok(ResponseDTO.success(null, "Dean office deleted successfully"));
        }
        return ResponseEntity.status(404).body(ResponseDTO.error("Dean office not found"));
    }

    @GetMapping("/faculty/{faculty}")
    public ResponseEntity<ResponseDTO<List<DeanOfficeDTO>>> getByFaculty(@PathVariable String faculty) {
        List<DeanOffice> deanOffices = deanOfficeService.findByFaculty(faculty);
        return ResponseEntity.ok(ResponseDTO.success(deanOfficeService.toDTOList(deanOffices), "Dean offices by faculty"));
    }
}
