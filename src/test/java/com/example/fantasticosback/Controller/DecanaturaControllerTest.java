package com.example.fantasticosback.Controller;

import com.example.fantasticosback.Model.Decanatura;
import com.example.fantasticosback.Server.DecanaturaService;
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

class DecanaturaControllerTest {
    @Mock
    private DecanaturaService decanaturaService;

    @InjectMocks
    private DecanaturaController decanaturaController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCrear() {
        Decanatura decanatura = new Decanatura("1", "Ingeniería");
        when(decanaturaService.guardar(decanatura)).thenReturn(decanatura);
        ResponseEntity<Decanatura> response = decanaturaController.crear(decanatura);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(decanatura, response.getBody());
    }

    @Test
    void testListar() {
        List<Decanatura> lista = Arrays.asList(
            new Decanatura("1", "Ingeniería"),
            new Decanatura("2", "Ciencias")
        );
        when(decanaturaService.obtenerTodos()).thenReturn(lista);
        ResponseEntity<List<Decanatura>> response = decanaturaController.listar();
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(lista, response.getBody());
    }

    @Test
    void testObtener_Existe() {
        Decanatura decanatura = new Decanatura("1", "Ingeniería");
        when(decanaturaService.obtenerPorId("1")).thenReturn(decanatura);
        ResponseEntity<Decanatura> response = decanaturaController.obtener("1");
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(decanatura, response.getBody());
    }

    @Test
    void testObtener_NoExiste() {
        when(decanaturaService.obtenerPorId("1")).thenReturn(null);
        ResponseEntity<Decanatura> response = decanaturaController.obtener("1");
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testActualizar_Existe() {
        Decanatura decanatura = new Decanatura("1", "Ingeniería");
        when(decanaturaService.obtenerPorId("1")).thenReturn(decanatura);
        when(decanaturaService.actualizar(any(Decanatura.class))).thenReturn(decanatura);
        ResponseEntity<Decanatura> response = decanaturaController.actualizar("1", decanatura);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(decanatura, response.getBody());
    }

    @Test
    void testActualizar_NoExiste() {
        Decanatura decanatura = new Decanatura("1", "Ingeniería");
        when(decanaturaService.obtenerPorId("1")).thenReturn(null);
        ResponseEntity<Decanatura> response = decanaturaController.actualizar("1", decanatura);
        assertEquals(404, response.getStatusCodeValue());
    }
}
