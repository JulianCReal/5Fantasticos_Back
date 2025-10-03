package com.example.fantasticosback.Controller;

import com.example.fantasticosback.Model.Professor;
import com.example.fantasticosback.Server.ProfessorService;
import com.example.fantasticosback.Dtos.TeacherDTO;
import com.example.fantasticosback.Dtos.ResponseDTO;
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

class ProfessorControllerTest {
    @Mock
    private ProfessorService professorService;

    @InjectMocks
    private ProfessorController professorController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreate() {
        TeacherDTO dto = new TeacherDTO("T1", "Ana", "Gomez", 123, "Math");
        Professor professor = new Professor();
        when(professorService.fromDTO(dto)).thenReturn(professor);
        when(professorService.guardar(professor)).thenReturn(professor);
        when(professorService.toDTO(professor)).thenReturn(dto);
        ResponseEntity<ResponseDTO<TeacherDTO>> response = professorController.create(dto);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Success", response.getBody().getStatus());
        assertEquals("Teacher created", response.getBody().getMessage());
        assertEquals(dto, response.getBody().getData());
    }

    @Test
    void testList() {
        TeacherDTO dto = new TeacherDTO("T1", "Ana", "Gomez", 123, "Math");
        List<TeacherDTO> dtos = Collections.singletonList(dto);
        when(professorService.obtenerTodos()).thenReturn(Collections.emptyList());
        when(professorService.toDTOList(any())).thenReturn(dtos);
        ResponseEntity<ResponseDTO<List<TeacherDTO>>> response = professorController.list();
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Success", response.getBody().getStatus());
        assertEquals("List of teachers", response.getBody().getMessage());
        assertEquals(dtos, response.getBody().getData());
    }

    @Test
    void testGet_Exists() {
        String id = "T1";
        TeacherDTO dto = new TeacherDTO(id, "Ana", "Gomez", 123, "Math");
        Professor professor = new Professor();
        when(professorService.obtenerPorId(id)).thenReturn(professor);
        when(professorService.toDTO(professor)).thenReturn(dto);
        ResponseEntity<ResponseDTO<TeacherDTO>> response = professorController.get(id);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Success", response.getBody().getStatus());
        assertEquals("Teacher found", response.getBody().getMessage());
        assertEquals(dto, response.getBody().getData());
    }

    @Test
    void testGet_NotExists() {
        String id = "T1";
        when(professorService.obtenerPorId(id)).thenReturn(null);
        ResponseEntity<ResponseDTO<TeacherDTO>> response = professorController.get(id);
        assertEquals(404, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Error", response.getBody().getStatus());
        assertEquals("Teacher not found", response.getBody().getMessage());
        assertNull(response.getBody().getData());
    }

    @Test
    void testUpdate_Exists() {
        String id = "T1";
        TeacherDTO dto = new TeacherDTO(id, "Ana", "Gomez", 123, "Math");
        Professor professor = new Professor();
        when(professorService.obtenerPorId(id)).thenReturn(professor);
        when(professorService.fromDTO(dto)).thenReturn(professor);
        when(professorService.actualizar(professor)).thenReturn(professor);
        when(professorService.toDTO(professor)).thenReturn(dto);
        ResponseEntity<ResponseDTO<TeacherDTO>> response = professorController.update(id, dto);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Success", response.getBody().getStatus());
        assertEquals("Teacher updated", response.getBody().getMessage());
        assertEquals(dto, response.getBody().getData());
    }

    @Test
    void testUpdate_NotExists() {
        String id = "T1";
        TeacherDTO dto = new TeacherDTO(id, "Ana", "Gomez", 123, "Math");
        when(professorService.obtenerPorId(id)).thenReturn(null);
        ResponseEntity<ResponseDTO<TeacherDTO>> response = professorController.update(id, dto);
        assertEquals(404, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Error", response.getBody().getStatus());
        assertEquals("Teacher not found", response.getBody().getMessage());
        assertNull(response.getBody().getData());
    }

    @Test
    void testDelete_Exists() {
        String id = "T1";
        Professor professor = new Professor();
        when(professorService.obtenerPorId(id)).thenReturn(professor);
        doNothing().when(professorService).eliminar(id);
        ResponseEntity<ResponseDTO<Void>> response = professorController.delete(id);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Success", response.getBody().getStatus());
        assertEquals("Teacher deleted", response.getBody().getMessage());
        assertNull(response.getBody().getData());
    }

    @Test
    void testDelete_NotExists() {
        String id = "T1";
        when(professorService.obtenerPorId(id)).thenReturn(null);
        ResponseEntity<ResponseDTO<Void>> response = professorController.delete(id);
        assertEquals(404, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Error", response.getBody().getStatus());
        assertEquals("Teacher not found", response.getBody().getMessage());
        assertNull(response.getBody().getData());
    }

    @Test
    void testByDepartment() {
        String department = "Math";
        TeacherDTO dto = new TeacherDTO("T1", "Ana", "Gomez", 123, department);
        List<TeacherDTO> dtos = Collections.singletonList(dto);
        when(professorService.obtenerPorDepartamento(department)).thenReturn(Collections.emptyList());
        when(professorService.toDTOList(any())).thenReturn(dtos);
        ResponseEntity<ResponseDTO<List<TeacherDTO>>> response = professorController.byDepartment(department);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Success", response.getBody().getStatus());
        assertEquals("Teachers by department", response.getBody().getMessage());
        assertEquals(dtos, response.getBody().getData());
    }

    @Test
    void testByName() {
        String name = "Ana";
        TeacherDTO dto = new TeacherDTO("T1", name, "Gomez", 123, "Math");
        List<TeacherDTO> dtos = Collections.singletonList(dto);
        when(professorService.obtenerPorNombre(name)).thenReturn(Collections.emptyList());
        when(professorService.toDTOList(any())).thenReturn(dtos);
        ResponseEntity<ResponseDTO<List<TeacherDTO>>> response = professorController.byName(name);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Success", response.getBody().getStatus());
        assertEquals("Teachers by name", response.getBody().getMessage());
        assertEquals(dtos, response.getBody().getData());
    }

    @Test
    void testByLastName() {
        String lastName = "Gomez";
        TeacherDTO dto = new TeacherDTO("T1", "Ana", lastName, 123, "Math");
        List<TeacherDTO> dtos = Collections.singletonList(dto);
        when(professorService.obtenerPorApellido(lastName)).thenReturn(Collections.emptyList());
        when(professorService.toDTOList(any())).thenReturn(dtos);
        ResponseEntity<ResponseDTO<List<TeacherDTO>>> response = professorController.byLastName(lastName);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Success", response.getBody().getStatus());
        assertEquals("Teachers by last name", response.getBody().getMessage());
        assertEquals(dtos, response.getBody().getData());
    }
}
