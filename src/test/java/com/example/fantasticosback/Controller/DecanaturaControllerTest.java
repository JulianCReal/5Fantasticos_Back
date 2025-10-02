package com.example.fantasticosback.Controller;

import com.example.fantasticosback.Dtos.DeanOfficeDTO;
import com.example.fantasticosback.Dtos.ResponseDTO;
import com.example.fantasticosback.Model.Decanatura;
import com.example.fantasticosback.Server.DecanaturaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DecanaturaControllerTest {
    @Mock
    private DecanaturaService decanaturaService;

    @InjectMocks
    private DecanaturaController decanaturaController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private DeanOfficeDTO crearDeanOfficeDTODummy() {
        return new DeanOfficeDTO("1", "Ingeniería", new ArrayList<>());
    }

    private Decanatura crearDecanaturaDummy() {
        return new Decanatura("1", "Ingeniería");
    }

    @Test
    void testCrear() {
        DeanOfficeDTO dto = crearDeanOfficeDTODummy();
        Decanatura decanatura = crearDecanaturaDummy();
        when(decanaturaService.fromDTO(dto)).thenReturn(decanatura);
        when(decanaturaService.guardar(decanatura)).thenReturn(decanatura);
        when(decanaturaService.toDTO(decanatura)).thenReturn(dto);
        ResponseEntity<ResponseDTO<DeanOfficeDTO>> response = decanaturaController.crear(dto);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Success", response.getBody().getStatus());
        assertEquals("Dean office created successfully", response.getBody().getMessage());
        assertEquals(dto, response.getBody().getData());
    }

    @Test
    void testListar() {
        DeanOfficeDTO dto = crearDeanOfficeDTODummy();
        List<Decanatura> decanaturas = Collections.singletonList(crearDecanaturaDummy());
        List<DeanOfficeDTO> dtos = Collections.singletonList(dto);
        when(decanaturaService.obtenerTodos()).thenReturn(decanaturas);
        when(decanaturaService.toDTOList(decanaturas)).thenReturn(dtos);
        ResponseEntity<ResponseDTO<List<DeanOfficeDTO>>> response = decanaturaController.listar();
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Success", response.getBody().getStatus());
        assertEquals("List of dean offices", response.getBody().getMessage());
        assertEquals(dtos, response.getBody().getData());
    }

    @Test
    void testObtener_Existe() {
        DeanOfficeDTO dto = crearDeanOfficeDTODummy();
        Decanatura decanatura = crearDecanaturaDummy();
        when(decanaturaService.obtenerPorId("1")).thenReturn(decanatura);
        when(decanaturaService.toDTO(decanatura)).thenReturn(dto);
        ResponseEntity<ResponseDTO<DeanOfficeDTO>> response = decanaturaController.obtener("1");
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Success", response.getBody().getStatus());
        assertEquals("Dean office found", response.getBody().getMessage());
        assertEquals(dto, response.getBody().getData());
    }

    @Test
    void testObtener_NoExiste() {
        when(decanaturaService.obtenerPorId("1")).thenReturn(null);
        ResponseEntity<ResponseDTO<DeanOfficeDTO>> response = decanaturaController.obtener("1");
        assertEquals(404, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Error", response.getBody().getStatus());
        assertEquals("Dean office not found", response.getBody().getMessage());
        assertNull(response.getBody().getData());
    }

    @Test
    void testActualizar_Existe() {
        DeanOfficeDTO dto = crearDeanOfficeDTODummy();
        Decanatura decanatura = crearDecanaturaDummy();
        when(decanaturaService.obtenerPorId("1")).thenReturn(decanatura);
        when(decanaturaService.fromDTO(dto)).thenReturn(decanatura);
        when(decanaturaService.actualizar(decanatura)).thenReturn(decanatura);
        when(decanaturaService.toDTO(decanatura)).thenReturn(dto);
        ResponseEntity<ResponseDTO<DeanOfficeDTO>> response = decanaturaController.actualizar("1", dto);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Success", response.getBody().getStatus());
        assertEquals("Dean office updated successfully", response.getBody().getMessage());
        assertEquals(dto, response.getBody().getData());
    }

    @Test
    void testActualizar_NoExiste() {
        DeanOfficeDTO dto = crearDeanOfficeDTODummy();
        when(decanaturaService.obtenerPorId("1")).thenReturn(null);
        ResponseEntity<ResponseDTO<DeanOfficeDTO>> response = decanaturaController.actualizar("1", dto);
        assertEquals(404, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Error", response.getBody().getStatus());
        assertEquals("Dean office not found", response.getBody().getMessage());
        assertNull(response.getBody().getData());
    }
}
