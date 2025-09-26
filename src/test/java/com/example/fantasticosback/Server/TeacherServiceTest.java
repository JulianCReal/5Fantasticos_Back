package com.example.fantasticosback.Server;

import com.example.fantasticosback.Model.Profesor;
import com.example.fantasticosback.Repository.TeacherRepository;
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
class TeacherServiceTest {
    @Mock
    private TeacherRepository teacherRepository;
    @InjectMocks
    private TeacherService teacherService;

    private Profesor getProfesorDummy() {
        Profesor profesor = new Profesor("Juan", "Perez", 123, "Matemáticas");
        profesor.setId("1");
        return profesor;
    }

    @Test
    void testGuardar() {
        Profesor profesor = getProfesorDummy();
        when(teacherRepository.save(profesor)).thenReturn(profesor);
        assertEquals(profesor, teacherService.guardar(profesor));
    }

    @Test
    void testObtenerTodos() {
        List<Profesor> lista = Arrays.asList(getProfesorDummy());
        when(teacherRepository.findAll()).thenReturn(lista);
        assertEquals(lista, teacherService.obtenerTodos());
    }

    @Test
    void testObtenerPorId() {
        Profesor profesor = getProfesorDummy();
        when(teacherRepository.findById("1")).thenReturn(Optional.of(profesor));
        assertEquals(profesor, teacherService.obtenerPorId("1"));
        when(teacherRepository.findById("2")).thenReturn(Optional.empty());
        assertNull(teacherService.obtenerPorId("2"));
    }

    @Test
    void testActualizar() {
        Profesor profesor = getProfesorDummy();
        when(teacherRepository.save(profesor)).thenReturn(profesor);
        assertEquals(profesor, teacherService.actualizar(profesor));
    }

    @Test
    void testEliminar() {
        teacherService.eliminar("1");
        verify(teacherRepository).deleteById("1");
    }

    @Test
    void testObtenerPorDepartamento() {
        List<Profesor> lista = Arrays.asList(getProfesorDummy());
        when(teacherRepository.findByDepartamento("Departamento")).thenReturn(lista);
        assertEquals(lista, teacherService.obtenerPorDepartamento("Departamento"));
    }

    @Test
    void testObtenerPorNombre() {
        List<Profesor> lista = Arrays.asList(getProfesorDummy());
        when(teacherRepository.findByNombre("Juan")).thenReturn(lista);
        assertEquals(lista, teacherService.obtenerPorNombre("Juan"));
    }

    @Test
    void testObtenerPorApellido() {
        List<Profesor> lista = Arrays.asList(getProfesorDummy());
        when(teacherRepository.findByApellido("Perez")).thenReturn(lista);
        assertEquals(lista, teacherService.obtenerPorApellido("Perez"));
    }
}
