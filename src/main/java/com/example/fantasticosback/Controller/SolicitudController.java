package com.example.fantasticosback.Controller;


import com.example.fantasticosback.Dtos.RequestDTO;
import com.example.fantasticosback.Dtos.ResponseDTO;
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
    public ResponseEntity<ResponseDTO<RequestDTO>> create(@RequestBody RequestDTO dto) {
        Solicitud solicitud = solicitudService.fromDTO(dto);
        Solicitud saved = solicitudService.guardar(solicitud);
        return ResponseEntity.ok(ResponseDTO.success(solicitudService.toDTO(saved), "Request created successfully"));
    }

    @GetMapping
    public ResponseEntity<ResponseDTO<List<RequestDTO>>> list() {
        List<RequestDTO> list = solicitudService.toDTOList(solicitudService.encontrarTodos());
        return ResponseEntity.ok(ResponseDTO.success(list, "List of requests"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<RequestDTO>> getById(@PathVariable String id) {
        Solicitud solicitud = solicitudService.obtenerPorId(id);
        if (solicitud == null) {
            return ResponseEntity.status(404).body(ResponseDTO.error("Request not found"));
        }
        return ResponseEntity.ok(ResponseDTO.success(solicitudService.toDTO(solicitud), "Request found"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO<RequestDTO>> update(@PathVariable String id, @RequestBody RequestDTO dto) {
        Solicitud existing = solicitudService.obtenerPorId(id);
        if (existing == null) {
            return ResponseEntity.status(404).body(ResponseDTO.error("Request not found"));
        }

        Solicitud updatedSolicitud = solicitudService.fromDTO(dto);
        updatedSolicitud.setSolicitudId(id);  // Important: preserve original ID
        Solicitud updated = solicitudService.actualizar(updatedSolicitud);
        return ResponseEntity.ok(ResponseDTO.success(solicitudService.toDTO(updated), "Request updated successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<Void>> delete(@PathVariable String id) {
        Solicitud existing = solicitudService.obtenerPorId(id);
        if (existing == null) {
            return ResponseEntity.status(404).body(ResponseDTO.error("Request not found"));
        }
        solicitudService.eliminar(id);
        return ResponseEntity.ok(ResponseDTO.success(null, "Request deleted successfully"));
    }

    @GetMapping("/status/{nombreEstado}")
    public ResponseEntity<ResponseDTO<List<RequestDTO>>> getByStatus(@PathVariable String nombreEstado) {
        List<RequestDTO> list = solicitudService.toDTOList(solicitudService.obtenerPorNombreEstado(nombreEstado));
        return ResponseEntity.ok(ResponseDTO.success(list, "Requests by status"));
    }
}
