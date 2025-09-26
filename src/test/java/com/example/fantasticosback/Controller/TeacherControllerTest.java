package com.example.fantasticosback.Controller;

import com.example.fantasticosback.Model.Profesor;
import com.example.fantasticosback.Server.TeacherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
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
    void testCrear() {
        Profesor profesor = new Profesor();
        when(teacherService.guardar(profesor)).thenReturn(profesor);
        ResponseEntity<Profesor> response = teacherController.crear(profesor);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(profesor, response.getBody());
    }

    @Test
    void testListar() {
        List<Profesor> lista = Arrays.asList(new Profesor(), new Profesor());
        when(teacherService.obtenerTodos()).thenReturn(lista);
        ResponseEntity<List<Profesor>> response = teacherController.listar();
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(lista, response.getBody());
    }

    @Test
    void testObtener_Existe() {
        Profesor profesor = new Profesor();
        when(teacherService.obtenerPorId("1")).thenReturn(profesor);
        ResponseEntity<Profesor> response = teacherController.obtener("1");
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(profesor, response.getBody());
    }

    @Test
    void testObtener_NoExiste() {
        when(teacherService.obtenerPorId("1")).thenReturn(null);
        ResponseEntity<Profesor> response = teacherController.obtener("1");
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testActualizar_Existe() {
        Profesor profesor = new Profesor();
        when(teacherService.obtenerPorId("1")).thenReturn(profesor);
        when(teacherService.actualizar(any(Profesor.class))).thenReturn(profesor);
        ResponseEntity<Profesor> response = teacherController.actualizar("1", profesor);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(profesor, response.getBody());
    }

    @Test
    void testActualizar_NoExiste() {
        Profesor profesor = new Profesor();
        when(teacherService.obtenerPorId("1")).thenReturn(null);
        ResponseEntity<Profesor> response = teacherController.actualizar("1", profesor);
        assertEquals(404, response.getStatusCodeValue());
    }
}

