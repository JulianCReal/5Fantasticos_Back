package com.example.fantasticosback.Controller;

import com.example.fantasticosback.Dtos.DeanOfficeDTO;
import com.example.fantasticosback.Dtos.ResponseDTO;
import com.example.fantasticosback.Model.DeanOffice;
import com.example.fantasticosback.Server.DeanOfficeService;
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

class DeanOfficeControllerTest {
    @Mock
    private DeanOfficeService deanOfficeService;

    @InjectMocks
    private DeanOfficeController deanOfficeController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private DeanOfficeDTO crearDeanOfficeDTODummy() {
        return new DeanOfficeDTO("1", "Ingeniería", new ArrayList<>());
    }

    private DeanOffice crearDecanaturaDummy() {
        return new DeanOffice("1", "Ingeniería");
    }

    @Test
    void testCrear() {
        DeanOfficeDTO dto = crearDeanOfficeDTODummy();
        DeanOffice deanOffice = crearDecanaturaDummy();
        when(deanOfficeService.fromDTO(dto)).thenReturn(deanOffice);
        when(deanOfficeService.guardar(deanOffice)).thenReturn(deanOffice);
        when(deanOfficeService.toDTO(deanOffice)).thenReturn(dto);
        ResponseEntity<ResponseDTO<DeanOfficeDTO>> response = deanOfficeController.crear(dto);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Success", response.getBody().getStatus());
        assertEquals("Dean office created successfully", response.getBody().getMessage());
        assertEquals(dto, response.getBody().getData());
    }

    @Test
    void testListar() {
        DeanOfficeDTO dto = crearDeanOfficeDTODummy();
        List<DeanOffice> deanOffices = Collections.singletonList(crearDecanaturaDummy());
        List<DeanOfficeDTO> dtos = Collections.singletonList(dto);
        when(deanOfficeService.obtenerTodos()).thenReturn(deanOffices);
        when(deanOfficeService.toDTOList(deanOffices)).thenReturn(dtos);
        ResponseEntity<ResponseDTO<List<DeanOfficeDTO>>> response = deanOfficeController.listar();
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Success", response.getBody().getStatus());
        assertEquals("List of dean offices", response.getBody().getMessage());
        assertEquals(dtos, response.getBody().getData());
    }

    @Test
    void testObtener_Existe() {
        DeanOfficeDTO dto = crearDeanOfficeDTODummy();
        DeanOffice deanOffice = crearDecanaturaDummy();
        when(deanOfficeService.obtenerPorId("1")).thenReturn(deanOffice);
        when(deanOfficeService.toDTO(deanOffice)).thenReturn(dto);
        ResponseEntity<ResponseDTO<DeanOfficeDTO>> response = deanOfficeController.obtener("1");
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Success", response.getBody().getStatus());
        assertEquals("Dean office found", response.getBody().getMessage());
        assertEquals(dto, response.getBody().getData());
    }

    @Test
    void testObtener_NoExiste() {
        when(deanOfficeService.obtenerPorId("1")).thenReturn(null);
        ResponseEntity<ResponseDTO<DeanOfficeDTO>> response = deanOfficeController.obtener("1");
        assertEquals(404, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Error", response.getBody().getStatus());
        assertEquals("Dean office not found", response.getBody().getMessage());
        assertNull(response.getBody().getData());
    }

    @Test
    void testActualizar_Existe() {
        DeanOfficeDTO dto = crearDeanOfficeDTODummy();
        DeanOffice deanOffice = crearDecanaturaDummy();
        when(deanOfficeService.obtenerPorId("1")).thenReturn(deanOffice);
        when(deanOfficeService.fromDTO(dto)).thenReturn(deanOffice);
        when(deanOfficeService.actualizar(deanOffice)).thenReturn(deanOffice);
        when(deanOfficeService.toDTO(deanOffice)).thenReturn(dto);
        ResponseEntity<ResponseDTO<DeanOfficeDTO>> response = deanOfficeController.actualizar("1", dto);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Success", response.getBody().getStatus());
        assertEquals("Dean office updated successfully", response.getBody().getMessage());
        assertEquals(dto, response.getBody().getData());
    }

    @Test
    void testActualizar_NoExiste() {
        DeanOfficeDTO dto = crearDeanOfficeDTODummy();
        when(deanOfficeService.obtenerPorId("1")).thenReturn(null);
        ResponseEntity<ResponseDTO<DeanOfficeDTO>> response = deanOfficeController.actualizar("1", dto);
        assertEquals(404, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Error", response.getBody().getStatus());
        assertEquals("Dean office not found", response.getBody().getMessage());
        assertNull(response.getBody().getData());
    }
}
