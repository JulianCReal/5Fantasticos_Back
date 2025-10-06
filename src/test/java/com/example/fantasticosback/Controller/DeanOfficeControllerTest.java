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

    private DeanOfficeDTO createDeanOfficeDTODummy() {
        return new DeanOfficeDTO("1", "Engineering", new ArrayList<>());
    }

    private DeanOffice createDeanOfficeDummy() {
        return new DeanOffice("1", "Engineering");
    }

    @Test
    void testCreate() {
        DeanOfficeDTO dto = createDeanOfficeDTODummy();
        DeanOffice deanOffice = createDeanOfficeDummy();
        when(deanOfficeService.fromDTO(dto)).thenReturn(deanOffice);
        when(deanOfficeService.save(deanOffice)).thenReturn(deanOffice);
        when(deanOfficeService.toDTO(deanOffice)).thenReturn(dto);
        ResponseEntity<ResponseDTO<DeanOfficeDTO>> response = deanOfficeController.create(dto);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Success", response.getBody().getStatus());
        assertEquals("Dean office created successfully", response.getBody().getMessage());
        assertEquals(dto, response.getBody().getData());
    }

    @Test
    void testList() {
        DeanOfficeDTO dto = createDeanOfficeDTODummy();
        List<DeanOfficeDTO> dtos = Collections.singletonList(dto);
        when(deanOfficeService.findAll()).thenReturn(Collections.emptyList());
        when(deanOfficeService.toDTOList(any())).thenReturn(dtos);
        ResponseEntity<ResponseDTO<List<DeanOfficeDTO>>> response = deanOfficeController.list();
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Success", response.getBody().getStatus());
        assertEquals("List of dean offices", response.getBody().getMessage());
        assertEquals(dtos, response.getBody().getData());
    }

    @Test
    void testGet_Exists() {
        String id = "1";
        DeanOffice deanOffice = createDeanOfficeDummy();
        DeanOfficeDTO dto = createDeanOfficeDTODummy();
        when(deanOfficeService.findById(id)).thenReturn(deanOffice);
        when(deanOfficeService.toDTO(deanOffice)).thenReturn(dto);
        ResponseEntity<ResponseDTO<DeanOfficeDTO>> response = deanOfficeController.get(id);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Success", response.getBody().getStatus());
        assertEquals("Dean office found", response.getBody().getMessage());
        assertEquals(dto, response.getBody().getData());
    }

    @Test
    void testGet_NotExists() {
        String id = "1";
        when(deanOfficeService.findById(id)).thenReturn(null);
        ResponseEntity<ResponseDTO<DeanOfficeDTO>> response = deanOfficeController.get(id);
        assertEquals(404, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Error", response.getBody().getStatus());
        assertEquals("Dean office not found", response.getBody().getMessage());
        assertNull(response.getBody().getData());
    }

    @Test
    void testUpdate_Exists() {
        String id = "1";
        DeanOfficeDTO dto = createDeanOfficeDTODummy();
        DeanOffice deanOffice = createDeanOfficeDummy();
        when(deanOfficeService.findById(id)).thenReturn(deanOffice);
        when(deanOfficeService.fromDTO(dto)).thenReturn(deanOffice);
        when(deanOfficeService.update(deanOffice)).thenReturn(deanOffice);
        when(deanOfficeService.toDTO(deanOffice)).thenReturn(dto);
        ResponseEntity<ResponseDTO<DeanOfficeDTO>> response = deanOfficeController.update(id, dto);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Success", response.getBody().getStatus());
        assertEquals("Dean office updated successfully", response.getBody().getMessage());
        assertEquals(dto, response.getBody().getData());
    }

    @Test
    void testUpdate_NotExists() {
        String id = "1";
        DeanOfficeDTO dto = createDeanOfficeDTODummy();
        when(deanOfficeService.findById(id)).thenReturn(null);
        ResponseEntity<ResponseDTO<DeanOfficeDTO>> response = deanOfficeController.update(id, dto);
        assertEquals(404, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Error", response.getBody().getStatus());
        assertEquals("Dean office not found", response.getBody().getMessage());
        assertNull(response.getBody().getData());
    }
}
