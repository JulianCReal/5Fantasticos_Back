package com.example.fantasticosback.Server;

import com.example.fantasticosback.Model.Carrera;
import com.example.fantasticosback.Model.Estudiante;
import com.example.fantasticosback.Repository.StudentRepository;
import com.example.fantasticosback.util.SemaforoAcademico;
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
class StudentServiceTest {
    @Mock
    private StudentRepository studentRepository;
    @InjectMocks
    private StudentService studentService;

    private Estudiante getEstudianteDummy() {
        Carrera carrera = new Carrera("Ingeniería de Sistemas", 160);
        SemaforoAcademico semaforo = new SemaforoAcademico(1, 0, carrera);
        return new Estudiante("Juan", "Perez", 123, "Ing", "C001", "E001", 1, semaforo);
    }

    @Test
    void testGuardar() {
        Estudiante estudiante = getEstudianteDummy();
        when(studentRepository.save(estudiante)).thenReturn(estudiante);
        assertEquals(estudiante, studentService.guardar(estudiante));
    }

    @Test
    void testObtenerTodos() {
        List<Estudiante> lista = Arrays.asList(getEstudianteDummy());
        when(studentRepository.findAll()).thenReturn(lista);
        assertEquals(lista, studentService.obtenerTodos());
    }

    @Test
    void testObtenerPorId() {
        Estudiante estudiante = getEstudianteDummy();
        when(studentRepository.findById("1")).thenReturn(Optional.of(estudiante));
        assertEquals(estudiante, studentService.obtenerPorId("1"));
        when(studentRepository.findById("2")).thenReturn(Optional.empty());
        assertNull(studentService.obtenerPorId("2"));
    }

    @Test
    void testActualizar() {
        Estudiante estudiante = getEstudianteDummy();
        when(studentRepository.save(estudiante)).thenReturn(estudiante);
        assertEquals(estudiante, studentService.actualizar(estudiante));
    }

    @Test
    void testEliminar() {
        studentService.eliminar("1");
        verify(studentRepository).deleteById("1");
    }

    @Test
    void testObtenerPorCarrera() {
        List<Estudiante> lista = Arrays.asList(getEstudianteDummy());
        when(studentRepository.findByCarrera("Ing")).thenReturn(lista);
        assertEquals(lista, studentService.obtenerPorCarrera("Ing"));
    }

    @Test
    void testObtenerPorSemestre() {
        List<Estudiante> lista = Arrays.asList(getEstudianteDummy());
        when(studentRepository.findBySemestre(1)).thenReturn(lista);
        assertEquals(lista, studentService.obtenerPorSemestre(1));
    }
}
