package com.example.fantasticosback.controller;


import com.example.fantasticosback.dto.request.RequestDTO;
import com.example.fantasticosback.dto.request.RequestResponseDTO;
import com.example.fantasticosback.dto.response.ResponseDTO;
import com.example.fantasticosback.model.Document.Request;
import com.example.fantasticosback.model.RequestCreatrors.RequestCreator;
import com.example.fantasticosback.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/requests")
public class RequestController {
    private final RequestService requestService;
    private final ArrayList<RequestCreator> requestCreators;

    @Autowired
    public RequestController(RequestService requestService, ArrayList<RequestCreator> requestCreators) {
        this.requestService = requestService;
        this.requestCreators = requestCreators;
    }

    @PostMapping
    public ResponseEntity<ResponseDTO<RequestResponseDTO>> create(@RequestBody RequestDTO dto) {
        RequestCreator creator = selectCreatorForUse(dto);
        Request request = creator.createRequest(dto);
        Request saved = requestService.save(request);

        RequestResponseDTO responseDTO = convertToResponseDTO(saved);
        return ResponseEntity.ok(ResponseDTO.success(responseDTO, "Request created successfully"));
    }

    @GetMapping
    public ResponseEntity<List<RequestResponseDTO>> listAllRequests() {
        List<Request> list = requestService.findAll();
        List<RequestResponseDTO> listResponse = list.stream().map(this::convertToResponseDTO).collect(Collectors.toList());

        return ResponseEntity.ok(listResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<RequestResponseDTO>> getById(@PathVariable String id) {
        Request request = requestService.findById(id);
        RequestResponseDTO responseDTO = convertToResponseDTO(request);
        return ResponseEntity.ok(ResponseDTO.success(responseDTO, "Request found"));
    }
    /*
    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO<RequestDTO>> update(@PathVariable String id, @RequestBody RequestDTO dto) {
        Request updatedRequest = requestService.fromDTO(dto);
        updatedRequest.setRequestId(id);
        Request updated = requestService.update(updatedRequest);
        return ResponseEntity.ok(ResponseDTO.success(requestService.toDTO(updated), "Request updated successfully"));
    }*/

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<Void>> delete(@PathVariable String id) {
        requestService.delete(id);
        return ResponseEntity.ok(ResponseDTO.success(null, "Request deleted successfully"));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<RequestResponseDTO>> getByUserId(@PathVariable String userId) {
        List<Request> list = requestService.findByUserId(userId);
        List<RequestResponseDTO> listResponse = list.stream().map(this::convertToResponseDTO).collect(Collectors.toList());
        return ResponseEntity.ok(listResponse);
    }

    @GetMapping("/status/{statusName}")
    public ResponseEntity<List<RequestResponseDTO>> getByStatus(@PathVariable String statusName) {
        List<Request> list = requestService.findByStateName(statusName);
        List<RequestResponseDTO> listResponse = list.stream().map(this::convertToResponseDTO).collect(Collectors.toList());
        return ResponseEntity.ok(listResponse);
    }

    @GetMapping("/priority/{requestPriority}")
    public ResponseEntity<List<RequestResponseDTO>> getByRequestPriority(@PathVariable String requestPriority) {
        List<Request> list = requestService.findByRequestPriority(requestPriority);
        List<RequestResponseDTO> listResponse = list.stream().map(this::convertToResponseDTO).collect(Collectors.toList());
        return ResponseEntity.ok(listResponse);
    }

    @GetMapping("/creatorRole/{creatorRole}")
    public ResponseEntity<List<RequestResponseDTO>> getByCreatorRole(@PathVariable String creatorRole) {
        List<Request> list = requestService.findByCreatorRole(creatorRole);
        List<RequestResponseDTO> listResponse = list.stream().map(this::convertToResponseDTO).collect(Collectors.toList());
        return ResponseEntity.ok(listResponse);
    }

    @GetMapping("/deanOffice/{deanOffice}")
    public ResponseEntity<List<RequestResponseDTO>> getByDeanOffice(@PathVariable String deanOffice) {
        List<Request> list = requestService.findByDeanOffice(deanOffice);
        List<RequestResponseDTO> listResponse = list.stream().map(this::convertToResponseDTO).collect(Collectors.toList());
        return ResponseEntity.ok(listResponse);
    }

    @GetMapping("/RequestByOnestudent/{studentId}")
    public ResponseEntity<List<RequestResponseDTO>> getRequestsByOneStudent(@PathVariable String studentId) {
        List<Request> list = requestService.findRequestsByOneStudent(studentId);
        List<RequestResponseDTO> listResponse = list.stream().map(this::convertToResponseDTO).collect(Collectors.toList());
        return ResponseEntity.ok(listResponse);
    }

    @GetMapping("/studentsByOffice")
    public ResponseEntity<List<RequestResponseDTO>> getStudentsRequestByDeanOffice(@RequestParam String deanOffice, @RequestParam String userId) {
        List<Request> list = requestService.findStudentsRequestByDeanOffice(deanOffice, userId);
        List<RequestResponseDTO> listResponse = list.stream().map(this::convertToResponseDTO).collect(Collectors.toList());
        return ResponseEntity.ok(listResponse);
    }

    @GetMapping("/date/{requestDate}")
    public ResponseEntity<List<RequestResponseDTO>> getByRequestDate(@RequestParam("requestDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate requestDate) {
        List<Request> list = requestService.findByRequestDate(requestDate);
        List<RequestResponseDTO> listResponse = list.stream().map(this::convertToResponseDTO).collect(Collectors.toList());
        return ResponseEntity.ok(listResponse);
    }

    private RequestCreator selectCreatorForUse(RequestDTO dto) {
        return requestCreators.stream().filter(creator -> creator.getRequestType(dto.getRequestType()) == dto.getRequestType())
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No RequestCreator found for type: " + dto.getRequestType()));
    }

    private RequestResponseDTO convertToResponseDTO(Request request) {
        RequestResponseDTO responseDTO = new RequestResponseDTO();
        responseDTO.setRequestId(request.getId());
        responseDTO.setUserId(request.getUserId());
        responseDTO.setRequestType(request.getType());
        responseDTO.setHistoryResponses(request.getHistoryResponses());
        responseDTO.setRequestStateName(request.getStateName());
        responseDTO.setDeanOffice(request.getDeanOffice());
        responseDTO.setObservations(request.getObservations());
        return responseDTO;
    }
}
