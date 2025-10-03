package com.example.fantasticosback.Server;

import com.example.fantasticosback.Model.Subject;
import com.example.fantasticosback.Repository.SubjectRepository;
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
class SubjectServiceTest {
    @Mock
    private SubjectRepository subjectRepository;
    @InjectMocks
    private SubjectService subjectService;

    private Subject getMateriaDummy() {
        return new Subject(1, "Matemáticas", 3, 1);
    }

    @Test
    void testGuardar() {
        Subject subject = getMateriaDummy();
        when(subjectRepository.save(subject)).thenReturn(subject);
        assertEquals(subject, subjectService.guardar(subject));
    }

    @Test
    void testObtenerTodos() {
        List<Subject> lista = Arrays.asList(getMateriaDummy());
        when(subjectRepository.findAll()).thenReturn(lista);
        assertEquals(lista, subjectService.obtenerTodos());
    }

    @Test
    void testObtenerPorId() {
        Subject subject = getMateriaDummy();
        when(subjectRepository.findById("1")).thenReturn(Optional.of(subject));
        assertEquals(subject, subjectService.obtenerPorId("1"));
        when(subjectRepository.findById("2")).thenReturn(Optional.empty());
        assertNull(subjectService.obtenerPorId("2"));
    }

    @Test
    void testActualizar() {
        Subject subject = getMateriaDummy();
        when(subjectRepository.save(subject)).thenReturn(subject);
        assertEquals(subject, subjectService.actualizar(subject));
    }

    @Test
    void testEliminar() {
        subjectService.eliminar("1");
        verify(subjectRepository).deleteById("1");
    }

    @Test
    void testObtenerPorNombre() {
        List<Subject> lista = Arrays.asList(getMateriaDummy());
        when(subjectRepository.findByName("Matemáticas")).thenReturn(lista);
        assertEquals(lista, subjectService.obtenerPorNombre("Matemáticas"));
    }

    @Test
    void testObtenerPorSemestre() {
        List<Subject> lista = Arrays.asList(getMateriaDummy());
        when(subjectRepository.findBySemester(1)).thenReturn(lista);
        assertEquals(lista, subjectService.obtenerPorSemestre(1));
    }

    @Test
    void testObtenerPorCreditos() {
        List<Subject> lista = Arrays.asList(getMateriaDummy());
        when(subjectRepository.findByCredits(3)).thenReturn(lista);
        assertEquals(lista, subjectService.obtenerPorCreditos(3));
    }
}
