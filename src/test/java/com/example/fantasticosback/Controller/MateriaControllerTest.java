package com.example.fantasticosback.Controller;

import com.example.fantasticosback.Model.Materia;
import com.example.fantasticosback.Server.MateriaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

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

    @Test
    void testCrear() {
        Materia materia = new Materia();
        when(materiaService.guardar(materia)).thenReturn(materia);
        ResponseEntity<Materia> response = materiaController.crear(materia);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(materia, response.getBody());
    }

    @Test
    void testListar() {
        List<Materia> lista = Arrays.asList(new Materia(), new Materia());
        when(materiaService.obtenerTodos()).thenReturn(lista);
        ResponseEntity<List<Materia>> response = materiaController.listar();
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(lista, response.getBody());
    }

    @Test
    void testObtener_Existe() {
        Materia materia = new Materia();
        when(materiaService.obtenerPorId("1")).thenReturn(materia);
        ResponseEntity<Materia> response = materiaController.obtener("1");
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(materia, response.getBody());
    }

    @Test
    void testObtener_NoExiste() {
        when(materiaService.obtenerPorId("1")).thenReturn(null);
        ResponseEntity<Materia> response = materiaController.obtener("1");
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testActualizarPorCreditos_Existe() {
        Materia materia = new Materia();
        materia.setCreditos(5);
        Materia existente = new Materia();
        existente.setCreditos(3);
        List<Materia> existentes = Arrays.asList(existente);
        when(materiaService.obtenerPorCreditos(3)).thenReturn(existentes);
        when(materiaService.guardar(any(Materia.class))).thenReturn(materia);
        ResponseEntity<List<Materia>> response = materiaController.actualizarPorCreditos(3, materia);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testActualizarPorCreditos_NoExiste() {
        Materia materia = new Materia();
        when(materiaService.obtenerPorCreditos(3)).thenReturn(Collections.emptyList());
        ResponseEntity<List<Materia>> response = materiaController.actualizarPorCreditos(3, materia);
        assertEquals(404, response.getStatusCodeValue());
    }
}

