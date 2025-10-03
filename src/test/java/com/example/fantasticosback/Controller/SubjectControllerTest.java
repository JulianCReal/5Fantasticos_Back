package com.example.fantasticosback.Controller;

import com.example.fantasticosback.Model.Subject;
import com.example.fantasticosback.Server.SubjectService;
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

class SubjectControllerTest {
    @Mock
    private SubjectService subjectService;

    @InjectMocks
    private SubjectController subjectController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private SubjectDTO crearSubjectDTODummy() {
        return new SubjectDTO("1", "Matemáticas", 4, 1, new ArrayList<>());
    }

    private Subject crearMateriaDummy() {
        Subject subject = new Subject();
        subject.setCredits(4);
        return subject;
    }

    @Test
    void testCrear() {
        SubjectDTO dto = crearSubjectDTODummy();
        Subject subject = crearMateriaDummy();
        when(subjectService.fromDTO(dto)).thenReturn(subject);
        when(subjectService.guardar(subject)).thenReturn(subject);
        when(subjectService.toDTO(subject)).thenReturn(dto);
        ResponseEntity<ResponseDTO<SubjectDTO>> response = subjectController.crear(dto);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Success", response.getBody().getStatus());
        assertEquals("Subject created successfully", response.getBody().getMessage());
        assertEquals(dto, response.getBody().getData());
    }

    @Test
    void testListar() {
        SubjectDTO dto = crearSubjectDTODummy();
        List<Subject> subjects = Arrays.asList(crearMateriaDummy(), crearMateriaDummy());
        List<SubjectDTO> dtos = Arrays.asList(dto, dto);
        when(subjectService.obtenerTodos()).thenReturn(subjects);
        when(subjectService.toDTOList(subjects)).thenReturn(dtos);
        ResponseEntity<ResponseDTO<List<SubjectDTO>>> response = subjectController.listar();
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Success", response.getBody().getStatus());
        assertEquals("List of subjects", response.getBody().getMessage());
        assertEquals(dtos, response.getBody().getData());
    }

    @Test
    void testObtener_Existe() {
        SubjectDTO dto = crearSubjectDTODummy();
        Subject subject = crearMateriaDummy();
        when(subjectService.obtenerPorId("1")).thenReturn(subject);
        when(subjectService.toDTO(subject)).thenReturn(dto);
        ResponseEntity<ResponseDTO<SubjectDTO>> response = subjectController.obtener("1");
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Success", response.getBody().getStatus());
        assertEquals("Subject found", response.getBody().getMessage());
        assertEquals(dto, response.getBody().getData());
    }

    @Test
    void testObtener_NoExiste() {
        when(subjectService.obtenerPorId("1")).thenReturn(null);
        ResponseEntity<ResponseDTO<SubjectDTO>> response = subjectController.obtener("1");
        assertEquals(404, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Error", response.getBody().getStatus());
        assertEquals("Subject not found", response.getBody().getMessage());
        assertNull(response.getBody().getData());
    }

    @Test
    void testActualizarPorCreditos_Existe() {
        SubjectDTO dto = crearSubjectDTODummy();
        Subject existente = crearMateriaDummy();
        existente.setCredits(3);
        List<Subject> existentes = Arrays.asList(existente);
        List<SubjectDTO> dtos = Arrays.asList(dto);
        when(subjectService.obtenerPorCreditos(3)).thenReturn(existentes);
        when(subjectService.toDTOList(existentes)).thenReturn(dtos);
        when(subjectService.guardar(any(Subject.class))).thenReturn(existente);
        ResponseEntity<ResponseDTO<List<SubjectDTO>>> response = subjectController.actualizarPorCreditos(3, dto);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Success", response.getBody().getStatus());
        assertEquals("Subjects updated by credits", response.getBody().getMessage());
        assertEquals(dtos, response.getBody().getData());
    }

    @Test
    void testActualizarPorCreditos_NoExiste() {
        SubjectDTO dto = crearSubjectDTODummy();
        when(subjectService.obtenerPorCreditos(3)).thenReturn(Collections.emptyList());
        ResponseEntity<ResponseDTO<List<SubjectDTO>>> response = subjectController.actualizarPorCreditos(3, dto);
        assertEquals(404, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Error", response.getBody().getStatus());
        assertEquals("No subjects found with the specified credits", response.getBody().getMessage());
        assertNull(response.getBody().getData());
    }
}
