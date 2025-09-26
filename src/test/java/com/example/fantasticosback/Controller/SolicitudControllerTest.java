package com.example.fantasticosback.Controller;

import com.example.fantasticosback.Model.Solicitud;
import com.example.fantasticosback.Model.Grupo;
import com.example.fantasticosback.Server.SolicitudService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SolicitudControllerTest {
    @Mock
    private SolicitudService solicitudService;
    @Mock
    private Grupo grupoOrigen;
    @Mock
    private Grupo grupoDestino;

    @InjectMocks
    private SolicitudController solicitudController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private Solicitud crearSolicitudDummy() {
        return new Solicitud(1, grupoOrigen, grupoDestino, "tipo", "obs", new java.util.Date(), "E001");
    }

    @Test
    void testCrear() {
        Solicitud solicitud = crearSolicitudDummy();
        when(solicitudService.guardar(solicitud)).thenReturn(solicitud);
        ResponseEntity<Solicitud> response = solicitudController.crear(solicitud);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(solicitud, response.getBody());
    }

    @Test
    void testListar() {
        Solicitud s1 = crearSolicitudDummy();
        Solicitud s2 = crearSolicitudDummy();
        List<Solicitud> lista = Arrays.asList(s1, s2);
        when(solicitudService.encontrarTodos()).thenReturn(lista);
        ResponseEntity<List<Solicitud>> response = solicitudController.listar();
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(lista, response.getBody());
    }

    @Test
    void testObtener_Existe() {
        Solicitud solicitud = crearSolicitudDummy();
        when(solicitudService.obtenerPorId("1")).thenReturn(solicitud);
        ResponseEntity<Solicitud> response = solicitudController.obtener("1");
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(solicitud, response.getBody());
    }

    @Test
    void testObtener_NoExiste() {
        when(solicitudService.obtenerPorId("1")).thenReturn(null);
        ResponseEntity<Solicitud> response = solicitudController.obtener("1");
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testActualizar_Existe() {
        Solicitud solicitud = crearSolicitudDummy();
        when(solicitudService.obtenerPorId("1")).thenReturn(solicitud);
        when(solicitudService.actualizar(any(Solicitud.class))).thenReturn(solicitud);
        ResponseEntity<Solicitud> response = solicitudController.actualizar("1", solicitud);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(solicitud, response.getBody());
    }

    @Test
    void testActualizar_NoExiste() {
        Solicitud solicitud = crearSolicitudDummy();
        when(solicitudService.obtenerPorId("1")).thenReturn(null);
        ResponseEntity<Solicitud> response = solicitudController.actualizar("1", solicitud);
        assertEquals(404, response.getStatusCodeValue());
    }
}
