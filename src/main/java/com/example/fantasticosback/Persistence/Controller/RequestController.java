package com.example.fantasticosback.Persistence.Controller;


import com.example.fantasticosback.Dtos.RequestDTO;
import com.example.fantasticosback.Dtos.ResponseDTO;
import com.example.fantasticosback.Model.Document.Request;
import com.example.fantasticosback.Persistence.Service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/requests")
public class RequestController {
    @Autowired
    private RequestService requestService;

    @PostMapping
    public ResponseEntity<ResponseDTO<RequestDTO>> create(@RequestBody RequestDTO dto) {
        Request request = requestService.fromDTO(dto);
        Request saved = requestService.save(request);
        return ResponseEntity.ok(ResponseDTO.success(requestService.toDTO(saved), "Request created successfully"));
    }

    @GetMapping
    public ResponseEntity<ResponseDTO<List<RequestDTO>>> list() {
        List<RequestDTO> list = requestService.toDTOList(requestService.findAll());
        return ResponseEntity.ok(ResponseDTO.success(list, "List of requests"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<RequestDTO>> getById(@PathVariable String id) {
        Request request = requestService.findById(id);
        return ResponseEntity.ok(ResponseDTO.success(requestService.toDTO(request), "Request found"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO<RequestDTO>> update(@PathVariable String id, @RequestBody RequestDTO dto) {
        Request updatedRequest = requestService.fromDTO(dto);
        updatedRequest.setRequestId(id);  // Important: preserve original ID
        Request updated = requestService.update(updatedRequest);
        return ResponseEntity.ok(ResponseDTO.success(requestService.toDTO(updated), "Request updated successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<Void>> delete(@PathVariable String id) {
        requestService.delete(id);
        return ResponseEntity.ok(ResponseDTO.success(null, "Request deleted successfully"));
    }

    @GetMapping("/status/{statusName}")
    public ResponseEntity<ResponseDTO<List<RequestDTO>>> getByStatus(@PathVariable String statusName) {
        List<RequestDTO> list = requestService.toDTOList(requestService.findByStateName(statusName));
        return ResponseEntity.ok(ResponseDTO.success(list, "Requests by status"));
    }
}
