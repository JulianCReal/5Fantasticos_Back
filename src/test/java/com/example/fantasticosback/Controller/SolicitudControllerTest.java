package com.example.fantasticosback.Controller;

import com.example.fantasticosback.Dtos.RequestDTO;
import com.example.fantasticosback.Dtos.ResponseDTO;
import com.example.fantasticosback.Model.Solicitud;
import com.example.fantasticosback.Model.Grupo;
import com.example.fantasticosback.Server.SolicitudService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SolicitudControllerTest {
    @Mock
    private SolicitudService solicitudService;
    @Mock
    private Grupo grupoOrigen;
    @Mock
    private Grupo grupoDestino;

    @InjectMocks
    private SolicitudController solicitudController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private Solicitud crearSolicitudDummy() {
        return new Solicitud("1", grupoOrigen, grupoDestino, "tipo", "obs", new java.util.Date(), "E001");
    }

    private RequestDTO crearRequestDTODummy() {
        return new RequestDTO("1", "E001", grupoOrigen, grupoDestino, "tipo", "obs", "estado", new java.util.Date(), 1, true);
    }

    @Test
    void testCrear() {
        RequestDTO dto = crearRequestDTODummy();
        Solicitud solicitud = crearSolicitudDummy();
        when(solicitudService.fromDTO(dto)).thenReturn(solicitud);
        when(solicitudService.guardar(solicitud)).thenReturn(solicitud);
        when(solicitudService.toDTO(solicitud)).thenReturn(dto);
        ResponseEntity<ResponseDTO<RequestDTO>> response = solicitudController.create(dto);
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
        when(solicitudService.encontrarTodos()).thenReturn(Collections.emptyList());
        when(solicitudService.toDTOList(any())).thenReturn(dtos);
        ResponseEntity<ResponseDTO<List<RequestDTO>>> response = solicitudController.list();
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Success", response.getBody().getStatus());
        assertEquals("List of requests", response.getBody().getMessage());
        assertEquals(dtos, response.getBody().getData());
    }

    @Test
    void testObtener_Existe() {
        String id = "1";
        Solicitud solicitud = crearSolicitudDummy();
        RequestDTO dto = crearRequestDTODummy();
        when(solicitudService.obtenerPorId(id)).thenReturn(solicitud);
        when(solicitudService.toDTO(solicitud)).thenReturn(dto);
        ResponseEntity<ResponseDTO<RequestDTO>> response = solicitudController.getById(id);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Success", response.getBody().getStatus());
        assertEquals("Request found", response.getBody().getMessage());
        assertEquals(dto, response.getBody().getData());
    }

    @Test
    void testObtener_NoExiste() {
        String id = "1";
        when(solicitudService.obtenerPorId(id)).thenReturn(null);
        ResponseEntity<ResponseDTO<RequestDTO>> response = solicitudController.getById(id);
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
        Solicitud solicitud = crearSolicitudDummy();
        when(solicitudService.obtenerPorId(id)).thenReturn(solicitud);
        when(solicitudService.fromDTO(dto)).thenReturn(solicitud);
        when(solicitudService.actualizar(any(Solicitud.class))).thenReturn(solicitud);
        when(solicitudService.toDTO(solicitud)).thenReturn(dto);
        ResponseEntity<ResponseDTO<RequestDTO>> response = solicitudController.update(id, dto);
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
        when(solicitudService.obtenerPorId(id)).thenReturn(null);
        ResponseEntity<ResponseDTO<RequestDTO>> response = solicitudController.update(id, dto);
        assertEquals(404, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Error", response.getBody().getStatus());
        assertEquals("Request not found", response.getBody().getMessage());
        assertNull(response.getBody().getData());
    }

    @Test
    void testDelete_Existe() {
        String id = "1";
        Solicitud solicitud = crearSolicitudDummy();
        when(solicitudService.obtenerPorId(id)).thenReturn(solicitud);
        doNothing().when(solicitudService).eliminar(id);
        ResponseEntity<ResponseDTO<Void>> response = solicitudController.delete(id);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Success", response.getBody().getStatus());
        assertEquals("Request deleted successfully", response.getBody().getMessage());
        assertNull(response.getBody().getData());
    }

    @Test
    void testDelete_NoExiste() {
        String id = "1";
        when(solicitudService.obtenerPorId(id)).thenReturn(null);
        ResponseEntity<ResponseDTO<Void>> response = solicitudController.delete(id);
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
        when(solicitudService.obtenerPorNombreEstado(estado)).thenReturn(Collections.emptyList());
        when(solicitudService.toDTOList(any())).thenReturn(dtos);
        ResponseEntity<ResponseDTO<List<RequestDTO>>> response = solicitudController.getByStatus(estado);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Success", response.getBody().getStatus());
        assertEquals("Requests by status", response.getBody().getMessage());
        assertEquals(dtos, response.getBody().getData());
    }
}
