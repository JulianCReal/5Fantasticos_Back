package com.example.fantasticosback.Server;

import com.example.fantasticosback.Model.Professor;
import com.example.fantasticosback.Repository.ProfessorRepository;
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
class ProfessorServiceTest {
    @Mock
    private ProfessorRepository professorRepository;
    @InjectMocks
    private ProfessorService professorService;

    private Professor getProfesorDummy() {
        Professor professor = new Professor("Juan", "Perez", 123, "Matemáticas");
        professor.setId("1");
        return professor;
    }

    @Test
    void testGuardar() {
        Professor professor = getProfesorDummy();
        when(professorRepository.save(professor)).thenReturn(professor);
        assertEquals(professor, professorService.guardar(professor));
    }

    @Test
    void testObtenerTodos() {
        List<Professor> lista = Arrays.asList(getProfesorDummy());
        when(professorRepository.findAll()).thenReturn(lista);
        assertEquals(lista, professorService.obtenerTodos());
    }

    @Test
    void testObtenerPorId() {
        Professor professor = getProfesorDummy();
        when(professorRepository.findById("1")).thenReturn(Optional.of(professor));
        assertEquals(professor, professorService.obtenerPorId("1"));
        when(professorRepository.findById("2")).thenReturn(Optional.empty());
        assertNull(professorService.obtenerPorId("2"));
    }

    @Test
    void testActualizar() {
        Professor professor = getProfesorDummy();
        when(professorRepository.save(professor)).thenReturn(professor);
        assertEquals(professor, professorService.actualizar(professor));
    }

    @Test
    void testEliminar() {
        professorService.eliminar("1");
        verify(professorRepository).deleteById("1");
    }

    @Test
    void testObtenerPorDepartamento() {
        List<Professor> lista = Arrays.asList(getProfesorDummy());
        when(professorRepository.findByDepartment("Departamento")).thenReturn(lista);
        assertEquals(lista, professorService.obtenerPorDepartamento("Departamento"));
    }

    @Test
    void testObtenerPorNombre() {
        List<Professor> lista = Arrays.asList(getProfesorDummy());
        when(professorRepository.findByFirstName("Juan")).thenReturn(lista);
        assertEquals(lista, professorService.obtenerPorNombre("Juan"));
    }

    @Test
    void testObtenerPorApellido() {
        List<Professor> lista = Arrays.asList(getProfesorDummy());
        when(professorRepository.findByLastName("Perez")).thenReturn(lista);
        assertEquals(lista, professorService.obtenerPorApellido("Perez"));
    }
}
