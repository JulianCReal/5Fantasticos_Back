package com.example.fantasticosback.Controller;

import com.example.fantasticosback.Model.Estudiante;
import com.example.fantasticosback.Server.StudentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentControllerTest {
    @Mock
    private StudentService studentService;

    @InjectMocks
    private StudentController studentController;

    @Test
    void testCrear() {
        Estudiante estudiante = new Estudiante("Juan", "Perez", 123, "Ing", "C001", "E001", 1);
        when(studentService.guardar(estudiante)).thenReturn(estudiante);
        ResponseEntity<Estudiante> response = studentController.crear(estudiante);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(estudiante, response.getBody());
    }

    @Test
    void testListar() {
        Estudiante e1 = new Estudiante("Juan", "Perez", 123, "Ing", "C001", "E001", 1);
        Estudiante e2 = new Estudiante("Ana", "Lopez", 456, "Med", "C002", "E002", 2);
        List<Estudiante> lista = Arrays.asList(e1, e2);
        when(studentService.obtenerTodos()).thenReturn(lista);
        ResponseEntity<List<Estudiante>> response = studentController.listar();
        assertEquals(200, response.getStatusCode().value());
        assertEquals(lista, response.getBody());
    }

    @Test
    void testObtener_Existe() {
        Estudiante estudiante = new Estudiante("Juan", "Perez", 123, "Ing", "C001", "E001", 1);
        when(studentService.obtenerPorId("1")).thenReturn(estudiante);
        ResponseEntity<Estudiante> response = studentController.obtener("1");
        assertEquals(200, response.getStatusCode().value());
        assertEquals(estudiante, response.getBody());
    }

    @Test
    void testObtener_NoExiste() {
        when(studentService.obtenerPorId("1")).thenReturn(null);
        ResponseEntity<Estudiante> response = studentController.obtener("1");
        assertEquals(404, response.getStatusCode().value());
    }

    @Test
    void testActualizar_Existe() {
        Estudiante estudiante = new Estudiante("Juan", "Perez", 123, "Ing", "C001", "E001", 1);
        when(studentService.obtenerPorId("1")).thenReturn(estudiante);
        when(studentService.actualizar(any(Estudiante.class))).thenReturn(estudiante);
        ResponseEntity<Estudiante> response = studentController.actualizar("1", estudiante);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(estudiante, response.getBody());
    }

    @Test
    void testActualizar_NoExiste() {
        Estudiante estudiante = new Estudiante("Juan", "Perez", 123, "Ing", "C001", "E001", 1);
        when(studentService.obtenerPorId("1")).thenReturn(null);
        ResponseEntity<Estudiante> response = studentController.actualizar("1", estudiante);
        assertEquals(404, response.getStatusCode().value());
    }
}
