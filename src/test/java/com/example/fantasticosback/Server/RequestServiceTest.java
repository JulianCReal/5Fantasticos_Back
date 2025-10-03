package com.example.fantasticosback.Server;

import com.example.fantasticosback.Model.Request;
import com.example.fantasticosback.Model.Group;
import com.example.fantasticosback.Repository.RequestRepository;
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
class RequestServiceTest {
    @Mock
    private RequestRepository requestRepository;
    @Mock
    private Group groupOrigen;
    @Mock
    private Group groupDestino;
    @InjectMocks
    private RequestService requestService;

    private Request getSolicitudDummy() {
        return new Request("1", groupOrigen, groupDestino, "tipo", "obs", new Date(), "E001");
    }

    @Test
    void testGuardar() {
        Request request = getSolicitudDummy();
        when(requestRepository.save(request)).thenReturn(request);
        assertEquals(request, requestService.guardar(request));
    }

    @Test
    void testEncontrarTodos() {
        List<Request> lista = Arrays.asList(getSolicitudDummy());
        when(requestRepository.findAll()).thenReturn(lista);
        assertEquals(lista, requestService.encontrarTodos());
    }

    @Test
    void testObtenerPorId() {
        Request request = getSolicitudDummy();
        when(requestRepository.findById("1")).thenReturn(Optional.of(request));
        assertEquals(request, requestService.obtenerPorId("1"));
        when(requestRepository.findById("2")).thenReturn(Optional.empty());
        assertNull(requestService.obtenerPorId("2"));
    }

    @Test
    void testActualizar() {
        Request request = getSolicitudDummy();
        when(requestRepository.save(request)).thenReturn(request);
        assertEquals(request, requestService.actualizar(request));
    }

    @Test
    void testEliminar() {
        requestService.eliminar("1");
        verify(requestRepository).deleteById("1");
    }

    @Test
    void testObtenerPorNombreEstado() {
        List<Request> lista = Arrays.asList(getSolicitudDummy());
        when(requestRepository.findByStatusName("Pendiente")).thenReturn(lista);
        assertEquals(lista, requestService.obtenerPorNombreEstado("Pendiente"));
    }
}
