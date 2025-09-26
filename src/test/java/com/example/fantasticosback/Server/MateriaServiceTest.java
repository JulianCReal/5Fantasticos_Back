package com.example.fantasticosback.Server;

import com.example.fantasticosback.Model.Materia;
import com.example.fantasticosback.Repository.MateriaRepository;
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
class MateriaServiceTest {
    @Mock
    private MateriaRepository materiaRepository;
    @InjectMocks
    private MateriaService materiaService;

    private Materia getMateriaDummy() {
        return new Materia(1, "Matemáticas", 3, 1);
    }

    @Test
    void testGuardar() {
        Materia materia = getMateriaDummy();
        when(materiaRepository.save(materia)).thenReturn(materia);
        assertEquals(materia, materiaService.guardar(materia));
    }

    @Test
    void testObtenerTodos() {
        List<Materia> lista = Arrays.asList(getMateriaDummy());
        when(materiaRepository.findAll()).thenReturn(lista);
        assertEquals(lista, materiaService.obtenerTodos());
    }

    @Test
    void testObtenerPorId() {
        Materia materia = getMateriaDummy();
        when(materiaRepository.findById("1")).thenReturn(Optional.of(materia));
        assertEquals(materia, materiaService.obtenerPorId("1"));
        when(materiaRepository.findById("2")).thenReturn(Optional.empty());
        assertNull(materiaService.obtenerPorId("2"));
    }

    @Test
    void testActualizar() {
        Materia materia = getMateriaDummy();
        when(materiaRepository.save(materia)).thenReturn(materia);
        assertEquals(materia, materiaService.actualizar(materia));
    }

    @Test
    void testEliminar() {
        materiaService.eliminar("1");
        verify(materiaRepository).deleteById("1");
    }

    @Test
    void testObtenerPorNombre() {
        List<Materia> lista = Arrays.asList(getMateriaDummy());
        when(materiaRepository.findByNombre("Matemáticas")).thenReturn(lista);
        assertEquals(lista, materiaService.obtenerPorNombre("Matemáticas"));
    }

    @Test
    void testObtenerPorSemestre() {
        List<Materia> lista = Arrays.asList(getMateriaDummy());
        when(materiaRepository.findBySemestre(1)).thenReturn(lista);
        assertEquals(lista, materiaService.obtenerPorSemestre(1));
    }

    @Test
    void testObtenerPorCreditos() {
        List<Materia> lista = Arrays.asList(getMateriaDummy());
        when(materiaRepository.findByCreditos(3)).thenReturn(lista);
        assertEquals(lista, materiaService.obtenerPorCreditos(3));
    }
}
