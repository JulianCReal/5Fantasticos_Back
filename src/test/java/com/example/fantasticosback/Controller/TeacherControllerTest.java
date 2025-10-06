package com.example.fantasticosback.Controller;

import com.example.fantasticosback.Model.Teacher;
import com.example.fantasticosback.Server.TeacherService;
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

class TeacherControllerTest {
    @Mock
    private TeacherService teacherService;

    @InjectMocks
    private TeacherController teacherController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreate() {
        TeacherDTO dto = new TeacherDTO("T1", "Ana", "Gomez", 123, "Math");
        Teacher teacher = new Teacher();
        when(teacherService.fromDTO(dto)).thenReturn(teacher);
        when(teacherService.save(teacher)).thenReturn(teacher);
        when(teacherService.toDTO(teacher)).thenReturn(dto);
        ResponseEntity<ResponseDTO<TeacherDTO>> response = teacherController.create(dto);
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
        when(teacherService.findAll()).thenReturn(Collections.emptyList());
        when(teacherService.toDTOList(any())).thenReturn(dtos);
        ResponseEntity<ResponseDTO<List<TeacherDTO>>> response = teacherController.list();
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
        Teacher teacher = new Teacher();
        when(teacherService.findById(id)).thenReturn(teacher);
        when(teacherService.toDTO(teacher)).thenReturn(dto);
        ResponseEntity<ResponseDTO<TeacherDTO>> response = teacherController.get(id);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Success", response.getBody().getStatus());
        assertEquals("Teacher found", response.getBody().getMessage());
        assertEquals(dto, response.getBody().getData());
    }

    @Test
    void testGet_NotExists() {
        String id = "T1";
        when(teacherService.findById(id)).thenReturn(null);
        ResponseEntity<ResponseDTO<TeacherDTO>> response = teacherController.get(id);
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
        Teacher teacher = new Teacher();
        when(teacherService.findById(id)).thenReturn(teacher);
        when(teacherService.fromDTO(dto)).thenReturn(teacher);
        when(teacherService.update(teacher)).thenReturn(teacher);
        when(teacherService.toDTO(teacher)).thenReturn(dto);
        ResponseEntity<ResponseDTO<TeacherDTO>> response = teacherController.update(id, dto);
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
        when(teacherService.findById(id)).thenReturn(null);
        ResponseEntity<ResponseDTO<TeacherDTO>> response = teacherController.update(id, dto);
        assertEquals(404, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Error", response.getBody().getStatus());
        assertEquals("Teacher not found", response.getBody().getMessage());
        assertNull(response.getBody().getData());
    }

    @Test
    void testDelete_Exists() {
        String id = "T1";
        Teacher teacher = new Teacher();
        when(teacherService.findById(id)).thenReturn(teacher);
        doNothing().when(teacherService).delete(id);
        ResponseEntity<ResponseDTO<Void>> response = teacherController.delete(id);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Success", response.getBody().getStatus());
        assertEquals("Teacher deleted", response.getBody().getMessage());
        assertNull(response.getBody().getData());
    }

    @Test
    void testDelete_NotExists() {
        String id = "T1";
        when(teacherService.findById(id)).thenReturn(null);
        ResponseEntity<ResponseDTO<Void>> response = teacherController.delete(id);
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
        when(teacherService.findByDepartment(department)).thenReturn(Collections.emptyList());
        when(teacherService.toDTOList(any())).thenReturn(dtos);
        ResponseEntity<ResponseDTO<List<TeacherDTO>>> response = teacherController.getByDepartment(department);
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
        when(teacherService.findByName(name)).thenReturn(Collections.emptyList());
        when(teacherService.toDTOList(any())).thenReturn(dtos);
        ResponseEntity<ResponseDTO<List<TeacherDTO>>> response = teacherController.getByName(name);
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
        when(teacherService.findByLastName(lastName)).thenReturn(Collections.emptyList());
        when(teacherService.toDTOList(any())).thenReturn(dtos);
        ResponseEntity<ResponseDTO<List<TeacherDTO>>> response = teacherController.getByLastName(lastName);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Success", response.getBody().getStatus());
        assertEquals("Teachers by last name", response.getBody().getMessage());
        assertEquals(dtos, response.getBody().getData());
    }
}
