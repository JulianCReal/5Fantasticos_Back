package com.example.fantasticosback.Server;

import com.example.fantasticosback.Model.Solicitud;
import com.example.fantasticosback.Model.Grupo;
import com.example.fantasticosback.Repository.SolicitudRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SolicitudServiceTest {
    @Mock
    private SolicitudRepository solicitudRepository;
    @Mock
    private Grupo grupoOrigen;
    @Mock
    private Grupo grupoDestino;
    @InjectMocks
    private SolicitudService solicitudService;

    private Solicitud getSolicitudDummy() {
        return new Solicitud("1", grupoOrigen, grupoDestino, "tipo", "obs", new Date(), "E001");
    }

    @Test
    void testGuardar() {
        Solicitud solicitud = getSolicitudDummy();
        when(solicitudRepository.save(solicitud)).thenReturn(solicitud);
        assertEquals(solicitud, solicitudService.guardar(solicitud));
    }

    @Test
    void testEncontrarTodos() {
        List<Solicitud> lista = Arrays.asList(getSolicitudDummy());
        when(solicitudRepository.findAll()).thenReturn(lista);
        assertEquals(lista, solicitudService.encontrarTodos());
    }

    @Test
    void testObtenerPorId() {
        Solicitud solicitud = getSolicitudDummy();
        when(solicitudRepository.findById("1")).thenReturn(Optional.of(solicitud));
        assertEquals(solicitud, solicitudService.obtenerPorId("1"));
        when(solicitudRepository.findById("2")).thenReturn(Optional.empty());
        assertNull(solicitudService.obtenerPorId("2"));
    }

    @Test
    void testActualizar() {
        Solicitud solicitud = getSolicitudDummy();
        when(solicitudRepository.save(solicitud)).thenReturn(solicitud);
        assertEquals(solicitud, solicitudService.actualizar(solicitud));
    }

    @Test
    void testEliminar() {
        solicitudService.eliminar("1");
        verify(solicitudRepository).deleteById("1");
    }

    @Test
    void testObtenerPorNombreEstado() {
        List<Solicitud> lista = Arrays.asList(getSolicitudDummy());
        when(solicitudRepository.findByNombreEstado("Pendiente")).thenReturn(lista);
        assertEquals(lista, solicitudService.obtenerPorNombreEstado("Pendiente"));
    }
}
