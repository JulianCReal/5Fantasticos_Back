package com.example.fantasticosback.Controller;

import com.example.fantasticosback.Model.Materia;
import com.example.fantasticosback.Server.MateriaService;
import com.example.fantasticosback.Dtos.SubjectDTO;
import com.example.fantasticosback.Dtos.ResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MateriaControllerTest {
    @Mock
    private MateriaService materiaService;

    @InjectMocks
    private MateriaController materiaController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private SubjectDTO crearSubjectDTODummy() {
        return new SubjectDTO("1", "Matemáticas", 4, 1, new ArrayList<>());
    }

    private Materia crearMateriaDummy() {
        Materia materia = new Materia();
        materia.setCreditos(4);
        return materia;
    }

    @Test
    void testCrear() {
        SubjectDTO dto = crearSubjectDTODummy();
        Materia materia = crearMateriaDummy();
        when(materiaService.fromDTO(dto)).thenReturn(materia);
        when(materiaService.guardar(materia)).thenReturn(materia);
        when(materiaService.toDTO(materia)).thenReturn(dto);
        ResponseEntity<ResponseDTO<SubjectDTO>> response = materiaController.crear(dto);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Success", response.getBody().getStatus());
        assertEquals("Subject created successfully", response.getBody().getMessage());
        assertEquals(dto, response.getBody().getData());
    }

    @Test
    void testListar() {
        SubjectDTO dto = crearSubjectDTODummy();
        List<Materia> materias = Arrays.asList(crearMateriaDummy(), crearMateriaDummy());
        List<SubjectDTO> dtos = Arrays.asList(dto, dto);
        when(materiaService.obtenerTodos()).thenReturn(materias);
        when(materiaService.toDTOList(materias)).thenReturn(dtos);
        ResponseEntity<ResponseDTO<List<SubjectDTO>>> response = materiaController.listar();
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Success", response.getBody().getStatus());
        assertEquals("List of subjects", response.getBody().getMessage());
        assertEquals(dtos, response.getBody().getData());
    }

    @Test
    void testObtener_Existe() {
        SubjectDTO dto = crearSubjectDTODummy();
        Materia materia = crearMateriaDummy();
        when(materiaService.obtenerPorId("1")).thenReturn(materia);
        when(materiaService.toDTO(materia)).thenReturn(dto);
        ResponseEntity<ResponseDTO<SubjectDTO>> response = materiaController.obtener("1");
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Success", response.getBody().getStatus());
        assertEquals("Subject found", response.getBody().getMessage());
        assertEquals(dto, response.getBody().getData());
    }

    @Test
    void testObtener_NoExiste() {
        when(materiaService.obtenerPorId("1")).thenReturn(null);
        ResponseEntity<ResponseDTO<SubjectDTO>> response = materiaController.obtener("1");
        assertEquals(404, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Error", response.getBody().getStatus());
        assertEquals("Subject not found", response.getBody().getMessage());
        assertNull(response.getBody().getData());
    }

    @Test
    void testActualizarPorCreditos_Existe() {
        SubjectDTO dto = crearSubjectDTODummy();
        Materia existente = crearMateriaDummy();
        existente.setCreditos(3);
        List<Materia> existentes = Arrays.asList(existente);
        List<SubjectDTO> dtos = Arrays.asList(dto);
        when(materiaService.obtenerPorCreditos(3)).thenReturn(existentes);
        when(materiaService.toDTOList(existentes)).thenReturn(dtos);
        when(materiaService.guardar(any(Materia.class))).thenReturn(existente);
        ResponseEntity<ResponseDTO<List<SubjectDTO>>> response = materiaController.actualizarPorCreditos(3, dto);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Success", response.getBody().getStatus());
        assertEquals("Subjects updated by credits", response.getBody().getMessage());
        assertEquals(dtos, response.getBody().getData());
    }

    @Test
    void testActualizarPorCreditos_NoExiste() {
        SubjectDTO dto = crearSubjectDTODummy();
        when(materiaService.obtenerPorCreditos(3)).thenReturn(Collections.emptyList());
        ResponseEntity<ResponseDTO<List<SubjectDTO>>> response = materiaController.actualizarPorCreditos(3, dto);
        assertEquals(404, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Error", response.getBody().getStatus());
        assertEquals("No subjects found with the specified credits", response.getBody().getMessage());
        assertNull(response.getBody().getData());
    }
}
