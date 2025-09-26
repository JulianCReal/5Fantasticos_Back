package com.example.fantasticosback.Server;

import com.example.fantasticosback.Model.Decanatura;
import com.example.fantasticosback.Repository.DecanaturaRepository;
import com.example.fantasticosback.Server.DecanaturaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DecanaturaServiceTest {
    @Mock
    private DecanaturaRepository decanaturaRepository;
    @InjectMocks
    private DecanaturaService decanaturaService;

    private Decanatura getDecanaturaDummy() {
        return new Decanatura("1", "Ingeniería");
    }

    @Test
    void testGuardar() {
        Decanatura decanatura = getDecanaturaDummy();
        when(decanaturaRepository.save(decanatura)).thenReturn(decanatura);
        assertEquals(decanatura, decanaturaService.guardar(decanatura));
    }

    @Test
    void testObtenerTodos() {
        List<Decanatura> lista = Arrays.asList(getDecanaturaDummy());
        when(decanaturaRepository.findAll()).thenReturn(lista);
        assertEquals(lista, decanaturaService.obtenerTodos());
    }

    @Test
    void testObtenerPorId() {
        Decanatura decanatura = getDecanaturaDummy();
        when(decanaturaRepository.findById("1")).thenReturn(Optional.of(decanatura));
        assertEquals(decanatura, decanaturaService.obtenerPorId("1"));
        when(decanaturaRepository.findById("2")).thenReturn(Optional.empty());
        assertNull(decanaturaService.obtenerPorId("2"));
    }

    @Test
    void testActualizar() {
        Decanatura decanatura = getDecanaturaDummy();
        when(decanaturaRepository.save(decanatura)).thenReturn(decanatura);
        assertEquals(decanatura, decanaturaService.actualizar(decanatura));
    }

    @Test
    void testEliminar() {
        decanaturaService.eliminar("1");
        verify(decanaturaRepository).deleteById("1");
    }

    @Test
    void testObtenerPorFacultad() {
        List<Decanatura> lista = Arrays.asList(getDecanaturaDummy());
        when(decanaturaRepository.findByFacultad("Ingeniería")).thenReturn(lista);
        assertEquals(lista, decanaturaService.obtenerPorFacultad("Ingeniería"));
    }
}
