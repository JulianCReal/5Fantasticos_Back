package com.example.fantasticosback.Controller;

import com.example.fantasticosback.Dtos.RequestDTO;
import com.example.fantasticosback.Dtos.ResponseDTO;
import com.example.fantasticosback.Model.Request;
import com.example.fantasticosback.Model.Group;
import com.example.fantasticosback.Server.RequestService;
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
    private Group groupOrigen;
    @Mock
    private Group groupDestino;

    @InjectMocks
    private RequestController requestController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private Request crearSolicitudDummy() {
        return new Request("1", groupOrigen, groupDestino, "tipo", "obs", new java.util.Date(), "E001");
    }

    private RequestDTO crearRequestDTODummy() {
        return new RequestDTO("1", "E001", groupOrigen, groupDestino, "tipo", "obs", "estado", new java.util.Date(), 1, true);
    }

    @Test
    void testCrear() {
        RequestDTO dto = crearRequestDTODummy();
        Request request = crearSolicitudDummy();
        when(requestService.fromDTO(dto)).thenReturn(request);
        when(requestService.guardar(request)).thenReturn(request);
        when(requestService.toDTO(request)).thenReturn(dto);
        ResponseEntity<ResponseDTO<RequestDTO>> response = requestController.create(dto);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Success", response.getBody().getStatus());
        assertEquals("Request created successfully", response.getBody().getMessage());
        assertEquals(dto, response.getBody().getData());
    }

    @Test
    void testListar() {
        RequestDTO dto = crearRequestDTODummy();
        List<RequestDTO> dtos = Collections.singletonList(dto);
        when(requestService.encontrarTodos()).thenReturn(Collections.emptyList());
        when(requestService.toDTOList(any())).thenReturn(dtos);
        ResponseEntity<ResponseDTO<List<RequestDTO>>> response = requestController.list();
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Success", response.getBody().getStatus());
        assertEquals("List of requests", response.getBody().getMessage());
        assertEquals(dtos, response.getBody().getData());
    }

    @Test
    void testObtener_Existe() {
        String id = "1";
        Request request = crearSolicitudDummy();
        RequestDTO dto = crearRequestDTODummy();
        when(requestService.obtenerPorId(id)).thenReturn(request);
        when(requestService.toDTO(request)).thenReturn(dto);
        ResponseEntity<ResponseDTO<RequestDTO>> response = requestController.getById(id);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Success", response.getBody().getStatus());
        assertEquals("Request found", response.getBody().getMessage());
        assertEquals(dto, response.getBody().getData());
    }

    @Test
    void testObtener_NoExiste() {
        String id = "1";
        when(requestService.obtenerPorId(id)).thenReturn(null);
        ResponseEntity<ResponseDTO<RequestDTO>> response = requestController.getById(id);
        assertEquals(404, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Error", response.getBody().getStatus());
        assertEquals("Request not found", response.getBody().getMessage());
        assertNull(response.getBody().getData());
    }

    @Test
    void testActualizar_Existe() {
        String id = "1";
        RequestDTO dto = crearRequestDTODummy();
        Request request = crearSolicitudDummy();
        when(requestService.obtenerPorId(id)).thenReturn(request);
        when(requestService.fromDTO(dto)).thenReturn(request);
        when(requestService.actualizar(any(Request.class))).thenReturn(request);
        when(requestService.toDTO(request)).thenReturn(dto);
        ResponseEntity<ResponseDTO<RequestDTO>> response = requestController.update(id, dto);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Success", response.getBody().getStatus());
        assertEquals("Request updated successfully", response.getBody().getMessage());
        assertEquals(dto, response.getBody().getData());
    }

    @Test
    void testActualizar_NoExiste() {
        String id = "1";
        RequestDTO dto = crearRequestDTODummy();
        when(requestService.obtenerPorId(id)).thenReturn(null);
        ResponseEntity<ResponseDTO<RequestDTO>> response = requestController.update(id, dto);
        assertEquals(404, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Error", response.getBody().getStatus());
        assertEquals("Request not found", response.getBody().getMessage());
        assertNull(response.getBody().getData());
    }

    @Test
    void testDelete_Existe() {
        String id = "1";
        Request request = crearSolicitudDummy();
        when(requestService.obtenerPorId(id)).thenReturn(request);
        doNothing().when(requestService).eliminar(id);
        ResponseEntity<ResponseDTO<Void>> response = requestController.delete(id);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Success", response.getBody().getStatus());
        assertEquals("Request deleted successfully", response.getBody().getMessage());
        assertNull(response.getBody().getData());
    }

    @Test
    void testDelete_NoExiste() {
        String id = "1";
        when(requestService.obtenerPorId(id)).thenReturn(null);
        ResponseEntity<ResponseDTO<Void>> response = requestController.delete(id);
        assertEquals(404, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Error", response.getBody().getStatus());
        assertEquals("Request not found", response.getBody().getMessage());
        assertNull(response.getBody().getData());
    }

    @Test
    void testGetByStatus() {
        String estado = "Aprobada";
        RequestDTO dto = crearRequestDTODummy();
        List<RequestDTO> dtos = Collections.singletonList(dto);
        when(requestService.obtenerPorNombreEstado(estado)).thenReturn(Collections.emptyList());
        when(requestService.toDTOList(any())).thenReturn(dtos);
        ResponseEntity<ResponseDTO<List<RequestDTO>>> response = requestController.getByStatus(estado);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Success", response.getBody().getStatus());
        assertEquals("Requests by status", response.getBody().getMessage());
        assertEquals(dtos, response.getBody().getData());
    }
}
