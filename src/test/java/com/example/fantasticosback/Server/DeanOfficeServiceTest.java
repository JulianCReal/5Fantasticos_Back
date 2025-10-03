package com.example.fantasticosback.Server;

import com.example.fantasticosback.Model.DeanOffice;
import com.example.fantasticosback.Repository.DeanOfficeRepository;
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
class DeanOfficeServiceTest {
    @Mock
    private DeanOfficeRepository deanOfficeRepository;
    @InjectMocks
    private DeanOfficeService deanOfficeService;

    private DeanOffice getDecanaturaDummy() {
        return new DeanOffice("1", "Ingeniería");
    }

    @Test
    void testGuardar() {
        DeanOffice deanOffice = getDecanaturaDummy();
        when(deanOfficeRepository.save(deanOffice)).thenReturn(deanOffice);
        assertEquals(deanOffice, deanOfficeService.guardar(deanOffice));
    }

    @Test
    void testObtenerTodos() {
        List<DeanOffice> lista = Arrays.asList(getDecanaturaDummy());
        when(deanOfficeRepository.findAll()).thenReturn(lista);
        assertEquals(lista, deanOfficeService.obtenerTodos());
    }

    @Test
    void testObtenerPorId() {
        DeanOffice deanOffice = getDecanaturaDummy();
        when(deanOfficeRepository.findById("1")).thenReturn(Optional.of(deanOffice));
        assertEquals(deanOffice, deanOfficeService.obtenerPorId("1"));
        when(deanOfficeRepository.findById("2")).thenReturn(Optional.empty());
        assertNull(deanOfficeService.obtenerPorId("2"));
    }

    @Test
    void testActualizar() {
        DeanOffice deanOffice = getDecanaturaDummy();
        when(deanOfficeRepository.save(deanOffice)).thenReturn(deanOffice);
        assertEquals(deanOffice, deanOfficeService.actualizar(deanOffice));
    }

    @Test
    void testEliminar() {
        deanOfficeService.eliminar("1");
        verify(deanOfficeRepository).deleteById("1");
    }

    @Test
    void testObtenerPorFacultad() {
        List<DeanOffice> lista = Arrays.asList(getDecanaturaDummy());
        when(deanOfficeRepository.findByFaculty("Ingeniería")).thenReturn(lista);
        assertEquals(lista, deanOfficeService.obtenerPorFacultad("Ingeniería"));
    }
}
