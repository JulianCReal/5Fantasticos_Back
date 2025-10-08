package com.example.fantasticosback.Controller;

import com.example.fantasticosback.Dtos.RequestDTO;
import com.example.fantasticosback.Dtos.ResponseDTO;
import com.example.fantasticosback.Model.Entities.Request;
import com.example.fantasticosback.Model.Entities.Group;
import com.example.fantasticosback.Persistence.Controller.RequestController;
import com.example.fantasticosback.Persistence.Server.RequestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RequestControllerTest {
    @Mock
    private RequestService requestService;
    @Mock
    private Group sourceGroup;
    @Mock
    private Group destinationGroup;

    @InjectMocks
    private RequestController requestController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private Request createRequestDummy() {
        return new Request("1", sourceGroup, destinationGroup, "type", "obs", new java.util.Date(), "E001");
    }

    private RequestDTO createRequestDTODummy() {
        return new RequestDTO("1", "E001", sourceGroup, destinationGroup, "type", "obs", "status", new java.util.Date(), 1, true);
    }

    @Test
    void testCreate() {
        RequestDTO dto = createRequestDTODummy();
        Request request = createRequestDummy();
        when(requestService.fromDTO(dto)).thenReturn(request);
        when(requestService.save(request)).thenReturn(request);
        when(requestService.toDTO(request)).thenReturn(dto);
        ResponseEntity<ResponseDTO<RequestDTO>> response = requestController.create(dto);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Success", response.getBody().getStatus());
        assertEquals("Request created successfully", response.getBody().getMessage());
        assertEquals(dto, response.getBody().getData());
    }

    @Test
    void testList() {
        RequestDTO dto = createRequestDTODummy();
        List<RequestDTO> dtos = Collections.singletonList(dto);
        when(requestService.findAll()).thenReturn(Collections.emptyList());
        when(requestService.toDTOList(any())).thenReturn(dtos);
        ResponseEntity<ResponseDTO<List<RequestDTO>>> response = requestController.list();
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Success", response.getBody().getStatus());
        assertEquals("List of requests", response.getBody().getMessage());
        assertEquals(dtos, response.getBody().getData());
    }

    @Test
    void testGet_Exists() {
        String id = "1";
        Request request = createRequestDummy();
        RequestDTO dto = createRequestDTODummy();
        when(requestService.findById(id)).thenReturn(request);
        when(requestService.toDTO(request)).thenReturn(dto);
        ResponseEntity<ResponseDTO<RequestDTO>> response = requestController.getById(id);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Success", response.getBody().getStatus());
        assertEquals("Request found", response.getBody().getMessage());
        assertEquals(dto, response.getBody().getData());
    }

    @Test
    void testGet_NotExists() {
        String id = "1";
        when(requestService.findById(id)).thenReturn(null);
        ResponseEntity<ResponseDTO<RequestDTO>> response = requestController.getById(id);
        assertEquals(404, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Error", response.getBody().getStatus());
        assertEquals("Request not found", response.getBody().getMessage());
        assertNull(response.getBody().getData());
    }

    @Test
    void testUpdate_Exists() {
        String id = "1";
        RequestDTO dto = createRequestDTODummy();
        Request request = createRequestDummy();
        when(requestService.findById(id)).thenReturn(request);
        when(requestService.fromDTO(dto)).thenReturn(request);
        when(requestService.update(request)).thenReturn(request);
        when(requestService.toDTO(request)).thenReturn(dto);
        ResponseEntity<ResponseDTO<RequestDTO>> response = requestController.update(id, dto);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Success", response.getBody().getStatus());
        assertEquals("Request updated successfully", response.getBody().getMessage());
        assertEquals(dto, response.getBody().getData());
    }

    @Test
    void testUpdate_NotExists() {
        String id = "1";
        RequestDTO dto = createRequestDTODummy();
        when(requestService.findById(id)).thenReturn(null);
        ResponseEntity<ResponseDTO<RequestDTO>> response = requestController.update(id, dto);
        assertEquals(404, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Error", response.getBody().getStatus());
        assertEquals("Request not found", response.getBody().getMessage());
        assertNull(response.getBody().getData());
    }

    @Test
    void testDelete_Exists() {
        String id = "1";
        Request request = createRequestDummy();
        when(requestService.findById(id)).thenReturn(request);
        doNothing().when(requestService).delete(id);
        ResponseEntity<ResponseDTO<Void>> response = requestController.delete(id);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Success", response.getBody().getStatus());
        assertEquals("Request deleted successfully", response.getBody().getMessage());
        assertNull(response.getBody().getData());
    }

    @Test
    void testDelete_NotExists() {
        String id = "1";
        when(requestService.findById(id)).thenReturn(null);
        ResponseEntity<ResponseDTO<Void>> response = requestController.delete(id);
        assertEquals(404, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Error", response.getBody().getStatus());
        assertEquals("Request not found", response.getBody().getMessage());
        assertNull(response.getBody().getData());
    }

    @Test
    void testGetByStatus() {
        String statusName = "Pending";
        RequestDTO dto = createRequestDTODummy();
        List<RequestDTO> dtos = Collections.singletonList(dto);
        when(requestService.findByStateName(statusName)).thenReturn(Collections.emptyList());
        when(requestService.toDTOList(any())).thenReturn(dtos);
        ResponseEntity<ResponseDTO<List<RequestDTO>>> response = requestController.getByStatus(statusName);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Success", response.getBody().getStatus());
        assertEquals("Requests by status", response.getBody().getMessage());
        assertEquals(dtos, response.getBody().getData());
    }
}
