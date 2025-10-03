package com.example.fantasticosback.Server;

import com.example.fantasticosback.Model.Career;
import com.example.fantasticosback.Model.Student;
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

    private Student getEstudianteDummy() {
        Career career = new Career("Ingeniería de Sistemas", 160);
        SemaforoAcademico semaforo = new SemaforoAcademico(1, 0, career);
        return new Student("Juan", "Perez", 123, "Ing", "C001", "E001", 1, semaforo);
    }

    @Test
    void testGuardar() {
        Student student = getEstudianteDummy();
        when(studentRepository.save(student)).thenReturn(student);
        assertEquals(student, studentService.guardar(student));
    }

    @Test
    void testObtenerTodos() {
        List<Student> lista = Arrays.asList(getEstudianteDummy());
        when(studentRepository.findAll()).thenReturn(lista);
        assertEquals(lista, studentService.obtenerTodos());
    }

    @Test
    void testObtenerPorId() {
        Student student = getEstudianteDummy();
        when(studentRepository.findById("1")).thenReturn(Optional.of(student));
        assertEquals(student, studentService.obtenerPorId("1"));
        when(studentRepository.findById("2")).thenReturn(Optional.empty());
        assertNull(studentService.obtenerPorId("2"));
    }

    @Test
    void testActualizar() {
        Student student = getEstudianteDummy();
        when(studentRepository.save(student)).thenReturn(student);
        assertEquals(student, studentService.actualizar(student));
    }

    @Test
    void testEliminar() {
        studentService.eliminar("1");
        verify(studentRepository).deleteById("1");
    }

    @Test
    void testObtenerPorCarrera() {
        List<Student> lista = Arrays.asList(getEstudianteDummy());
        when(studentRepository.findByDegreeProgram("Ing")).thenReturn(lista);
        assertEquals(lista, studentService.obtenerPorCarrera("Ing"));
    }

    @Test
    void testObtenerPorSemestre() {
        List<Student> lista = Arrays.asList(getEstudianteDummy());
        when(studentRepository.findBySemester(1)).thenReturn(lista);
        assertEquals(lista, studentService.obtenerPorSemestre(1));
    }
}
