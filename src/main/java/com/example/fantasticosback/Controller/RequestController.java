package com.example.fantasticosback.Controller;


import com.example.fantasticosback.Dtos.RequestDTO;
import com.example.fantasticosback.Dtos.ResponseDTO;
import com.example.fantasticosback.Model.Request;
import com.example.fantasticosback.Server.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/solicitudes")
public class RequestController {
    @Autowired
    private RequestService requestService;

    @PostMapping
    public ResponseEntity<ResponseDTO<RequestDTO>> create(@RequestBody RequestDTO dto) {
        Request request = requestService.fromDTO(dto);
        Request saved = requestService.guardar(request);
        return ResponseEntity.ok(ResponseDTO.success(requestService.toDTO(saved), "Request created successfully"));
    }

    @GetMapping
    public ResponseEntity<ResponseDTO<List<RequestDTO>>> list() {
        List<RequestDTO> list = requestService.toDTOList(requestService.encontrarTodos());
        return ResponseEntity.ok(ResponseDTO.success(list, "List of requests"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<RequestDTO>> getById(@PathVariable String id) {
        Request request = requestService.obtenerPorId(id);
        if (request == null) {
            return ResponseEntity.status(404).body(ResponseDTO.error("Request not found"));
        }
        return ResponseEntity.ok(ResponseDTO.success(requestService.toDTO(request), "Request found"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO<RequestDTO>> update(@PathVariable String id, @RequestBody RequestDTO dto) {
        Request existing = requestService.obtenerPorId(id);
        if (existing == null) {
            return ResponseEntity.status(404).body(ResponseDTO.error("Request not found"));
        }

        Request updatedRequest = requestService.fromDTO(dto);
        updatedRequest.setSolicitudId(id);  // Important: preserve original ID
        Request updated = requestService.actualizar(updatedRequest);
        return ResponseEntity.ok(ResponseDTO.success(requestService.toDTO(updated), "Request updated successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<Void>> delete(@PathVariable String id) {
        Request existing = requestService.obtenerPorId(id);
        if (existing == null) {
            return ResponseEntity.status(404).body(ResponseDTO.error("Request not found"));
        }
        requestService.eliminar(id);
        return ResponseEntity.ok(ResponseDTO.success(null, "Request deleted successfully"));
    }

    @GetMapping("/status/{nombreEstado}")
    public ResponseEntity<ResponseDTO<List<RequestDTO>>> getByStatus(@PathVariable String nombreEstado) {
        List<RequestDTO> list = requestService.toDTOList(requestService.obtenerPorNombreEstado(nombreEstado));
        return ResponseEntity.ok(ResponseDTO.success(list, "Requests by status"));
    }
}
