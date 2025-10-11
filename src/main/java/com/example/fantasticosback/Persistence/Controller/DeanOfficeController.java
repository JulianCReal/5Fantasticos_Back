package com.example.fantasticosback.Persistence.Controller;

import com.example.fantasticosback.Dtos.DeanOfficeDTO;
import com.example.fantasticosback.Dtos.ResponseDTO;
import com.example.fantasticosback.Persistence.Service.DeanOfficeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dean-offices")
public class DeanOfficeController {

    private final DeanOfficeService deanOfficeService;


    public DeanOfficeController(DeanOfficeService deanOfficeService) {
        this.deanOfficeService = deanOfficeService;
    }


    @PostMapping
    public ResponseEntity<ResponseDTO<DeanOfficeDTO>> create(@RequestBody DeanOfficeDTO dto) {
        DeanOfficeDTO saved = deanOfficeService.save(dto);
        return ResponseEntity.ok(ResponseDTO.success(saved, "Dean office created successfully"));
    }


    @GetMapping
    public ResponseEntity<ResponseDTO<List<DeanOfficeDTO>>> list() {
        List<DeanOfficeDTO> deanOffices = deanOfficeService.findAll();
        return ResponseEntity.ok(ResponseDTO.success(deanOffices, "List of dean offices"));
    }


    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<DeanOfficeDTO>> get(@PathVariable String id) {
        DeanOfficeDTO deanOffice = deanOfficeService.findById(id);
        return ResponseEntity.ok(ResponseDTO.success(deanOffice, "Dean office found"));
    }


    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO<DeanOfficeDTO>> update(@PathVariable String id, @RequestBody DeanOfficeDTO dto) {
        DeanOfficeDTO updated = deanOfficeService.update(id, dto);
        return ResponseEntity.ok(ResponseDTO.success(updated, "Dean office updated successfully"));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<Void>> delete(@PathVariable String id) {
        deanOfficeService.delete(id);
        return ResponseEntity.ok(ResponseDTO.success(null, "Dean office deleted successfully"));
    }


    @GetMapping("/faculty/{faculty}")
    public ResponseEntity<ResponseDTO<DeanOfficeDTO>> getByFaculty(@PathVariable String faculty) {
        DeanOfficeDTO deanOffice = deanOfficeService.findByFaculty(faculty);
        return ResponseEntity.ok(ResponseDTO.success(deanOffice, "Dean office by faculty"));
    }
    @PatchMapping("/{id}")
    public ResponseEntity<ResponseDTO<DeanOfficeDTO>> patch(
            @PathVariable String id,
            @RequestBody DeanOfficeDTO dto) {

        DeanOfficeDTO patched = deanOfficeService.patch(id, dto);
        return ResponseEntity.ok(ResponseDTO.success(patched, "Dean office partially updated"));
    }
}
