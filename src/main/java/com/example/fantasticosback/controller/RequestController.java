package com.example.fantasticosback.controller;


import com.example.fantasticosback.dto.request.RequestAddDataDTO;
import com.example.fantasticosback.dto.request.RequestDTO;
import com.example.fantasticosback.dto.request.RequestResponseDTO;
import com.example.fantasticosback.dto.response.ResponseDTO;
import com.example.fantasticosback.mapper.RequestResponseMapper;
import com.example.fantasticosback.model.Document.Request;
import com.example.fantasticosback.model.RequestCreatrors.RequestCreator;
import com.example.fantasticosback.service.RequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Tag(
    name = "Request",
    description = "Gestión completa de solicitudes académicas y administrativas: creación, consulta, filtrado por estado, prioridad, usuario y decanatura"
)
@RestController
@RequestMapping("/api/requests")
public class RequestController {
    private final RequestService requestService;
    private final List<RequestCreator> requestCreators;
    private final RequestResponseMapper requestMapper;

    @Autowired
    public RequestController(RequestService requestService, List<RequestCreator> requestCreators, RequestResponseMapper requestMapper) {
        this.requestService = requestService;
        this.requestCreators = requestCreators;
        this.requestMapper = requestMapper;
    }

    @Operation(
            summary = "Create a new request",
            description = "Allows a user (student, teacher, or administrative staff) to create a new request in the system."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Request created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RequestResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid data or creation error")
    })
    @PostMapping
    public ResponseEntity<ResponseDTO<RequestResponseDTO>> create(@RequestBody RequestDTO dto) {
        RequestCreator creator = selectCreatorForUse(dto);
        Request request = creator.createRequest(dto);
        Request saved = requestService.save(request);

        RequestResponseDTO responseDTO = requestMapper.convertToResponseDTO(saved);
        return ResponseEntity.ok(ResponseDTO.success(responseDTO, "Request created successfully"));
    }

    @Operation(summary = "List all requests", description = "Retrieves all requests stored in the system.")
    @ApiResponse(responseCode = "200", description = "List retrieved successfully")
    @GetMapping
    public ResponseEntity<List<RequestResponseDTO>> listAllRequests() {
        List<Request> list = requestService.findAll();
        List<RequestResponseDTO> listResponse = list.stream().map(requestMapper::convertToResponseDTO).collect(Collectors.toList());

        return ResponseEntity.ok(listResponse);
    }

    @Operation(summary = "Get a request by ID", description = "Retrieves a specific request by its unique identifier.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Request found"),
            @ApiResponse(responseCode = "404", description = "Request not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<RequestResponseDTO>> getById(@PathVariable String id) {
        Request request = requestService.findById(id);
        RequestResponseDTO responseDTO = requestMapper.convertToResponseDTO(request);
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

    @Operation(summary = "Delete a request by ID", description = "Deletes a request from the system by its identifier.")
    @ApiResponse(responseCode = "200", description = "Request deleted successfully")
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<Void>> delete(@PathVariable String id) {
        requestService.delete(id);
        return ResponseEntity.ok(ResponseDTO.success(null, "Request deleted successfully"));
    }

    @Operation(summary = "Get requests by user ID", description = "Retrieves all requests created by a specific user.")
    @ApiResponse(responseCode = "200", description = "Requests retrieved successfully")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<RequestResponseDTO>> getByUserId(@PathVariable String userId) {
        List<Request> list = requestService.findByUserId(userId);
        List<RequestResponseDTO> listResponse = list.stream().map(requestMapper::convertToResponseDTO).collect(Collectors.toList());
        return ResponseEntity.ok(listResponse);
    }

    @Operation(summary = "Get requests by status", description = "Retrieves all requests with the specified status (e.g., PENDING, APPROVED, REJECTED).")
    @ApiResponse(responseCode = "200", description = "Requests filtered by status retrieved successfully")
    @GetMapping("/status/{statusName}")
    public ResponseEntity<List<RequestResponseDTO>> getByStatus(@PathVariable String statusName) {
        List<Request> list = requestService.findByStateName(statusName);
        List<RequestResponseDTO> listResponse = list.stream().map(requestMapper::convertToResponseDTO).collect(Collectors.toList());
        return ResponseEntity.ok(listResponse);
    }

    @Operation(summary = "Get requests by priority", description = "Retrieves requests filtered by their priority level (HIGH, MEDIUM, LOW).")
    @ApiResponse(responseCode = "200", description = "Requests filtered by priority retrieved successfully")
    @GetMapping("/priority/{requestPriority}")
    public ResponseEntity<List<RequestResponseDTO>> getByRequestPriority(@PathVariable String requestPriority) {
        List<Request> list = requestService.findByRequestPriority(requestPriority);
        List<RequestResponseDTO> listResponse = list.stream().map(requestMapper::convertToResponseDTO).collect(Collectors.toList());
        return ResponseEntity.ok(listResponse);
    }

    @Operation(summary = "Get requests by creator role", description = "Retrieves requests created by users with a specific role.")
    @ApiResponse(responseCode = "200", description = "Requests filtered by creator role retrieved successfully")
    @GetMapping("/creatorRole/{creatorRole}")
    public ResponseEntity<List<RequestResponseDTO>> getByCreatorRole(@PathVariable String creatorRole) {
        List<Request> list = requestService.findByCreatorRole(creatorRole);
        List<RequestResponseDTO> listResponse = list.stream().map(requestMapper::convertToResponseDTO).collect(Collectors.toList());
        return ResponseEntity.ok(listResponse);
    }

    @Operation(summary = "Get requests by Dean Office", description = "Lists all requests associated with a specific Dean Office.")
    @ApiResponse(responseCode = "200", description = "Requests filtered by Dean Office retrieved successfully")
    @GetMapping("/deanOffice/{deanOffice}")
    public ResponseEntity<List<RequestResponseDTO>> getByDeanOffice(@PathVariable String deanOffice) {
        List<Request> list = requestService.findByDeanOffice(deanOffice);
        List<RequestResponseDTO> listResponse = list.stream().map(requestMapper::convertToResponseDTO).collect(Collectors.toList());
        return ResponseEntity.ok(listResponse);
    }

    @Operation(summary = "Get all requests from a specific student", description = "Retrieves all requests made by a specific student.")
    @ApiResponse(responseCode = "200", description = "Requests from the student retrieved successfully")
    @GetMapping("/RequestByOnestudent/{studentId}")
    public ResponseEntity<List<RequestResponseDTO>> getRequestsByOneStudent(@PathVariable String studentId) {
        List<Request> list = requestService.findRequestsByOneStudent(studentId);
        List<RequestResponseDTO> listResponse = list.stream().map(requestMapper::convertToResponseDTO).collect(Collectors.toList());
        return ResponseEntity.ok(listResponse);
    }

    @Operation(summary = "Get student requests by Dean Office", description = "Retrieves all student requests associated with a specific Dean Office and user.")
    @Parameters({
            @Parameter(name = "deanOffice", description = "Name of the Dean Office", example = "Engineering"),
            @Parameter(name = "userId", description = "Associated user ID", example = "64f3b12d9f2e1a4b5c9e7890")
    })
    @ApiResponse(responseCode = "200", description = "Requests retrieved successfully")
    @GetMapping("/studentsByOffice")
    public ResponseEntity<List<RequestResponseDTO>> getStudentsRequestByDeanOffice(@RequestParam String deanOffice, @RequestParam String userId) {
        List<Request> list = requestService.findStudentsRequestByDeanOffice(deanOffice, userId);
        List<RequestResponseDTO> listResponse = list.stream().map(requestMapper::convertToResponseDTO).collect(Collectors.toList());
        return ResponseEntity.ok(listResponse);
    }

    @Operation(summary = "Get requests by date", description = "Filters requests created on a specific date.")
    @Parameter(name = "requestDate", description = "Request creation date (ISO format: yyyy-MM-dd)", example = "2025-10-17")
    @ApiResponse(responseCode = "200", description = "Requests retrieved by date successfully")
    @GetMapping("/date/{requestDate}")
    public ResponseEntity<List<RequestResponseDTO>> getByRequestDate(@RequestParam("requestDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate requestDate) {
        List<Request> list = requestService.findByRequestDate(requestDate);
        List<RequestResponseDTO> listResponse = list.stream().map(requestMapper::convertToResponseDTO).collect(Collectors.toList());
        return ResponseEntity.ok(listResponse);
    }

    @Operation(summary = "Get the response history of a request", description = "Retrieves the entire response history of a request along with timestamps.")
    @ApiResponse(responseCode = "200", description = "Response history retrieved successfully")
    @GetMapping("/responseHistory/{id}")
    public ResponseEntity<HashMap<String, String>> getRequestResponseHistory(@PathVariable String id) {
        HashMap<String, String> history = requestService.getRequestHistoryResponses(id);
        return ResponseEntity.ok(history);
    }

    @Operation(summary = "Update additional request data", description = "Allows updating or adding extra data fields associated with a request.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Additional data updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RequestAddDataDTO.class))),
            @ApiResponse(responseCode = "404", description = "Request not found")
    })
    @PatchMapping("/aditional-data/{id}")
    public ResponseEntity<ResponseDTO<RequestAddDataDTO>> updateAditionalData(@PathVariable String id, @RequestBody Object data) {
        Request updatedRequest = requestService.updateAditionalData(id, data);
        RequestAddDataDTO responseDTO = requestMapper.convertToAddDataDTO(updatedRequest);
        return ResponseEntity.ok(ResponseDTO.success(responseDTO, "Aditional data updated successfully"));
    }

    private RequestCreator selectCreatorForUse(RequestDTO dto) {
        for (RequestCreator creator : requestCreators) {
            if (creator.canHandle(dto)) {
                return creator;
            }
        }
        throw new IllegalArgumentException("No suitable creator found for the request type");
    }
}