package com.example.fantasticosback.util;

import com.example.fantasticosback.Model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class SemesterTest {

    private Semester semester;
    private Enrollment enrollment1;
    private Enrollment enrollment2;
    private Group group1;
    private Group group2;
    private Subject subject1;
    private Subject subject2;
    private Professor professor;

    @BeforeEach
    void setUp() {
        semester = new Semester();

        // Crear objetos reales para las pruebas
        professor = new Professor("Juan", "Pérez", 12345678, "Sistemas");

        subject1 = new Subject(101, "Matemáticas", 3, 1);
        subject2 = new Subject(102, "Física", 4, 2);

        group1 = new Group(1, 1, 30, true, subject1, professor);
        group2 = new Group(2, 2, 25, true, subject2, professor);

        enrollment1 = new Enrollment(group1, 1, "activa", 4.0);
        enrollment2 = new Enrollment(group2, 2, "activa", 3.5);
    }

    @Test
    void testConstructor() {
        Semester semesterConParametros = new Semester(1, 2023, 2, true);

        assertEquals(1, semesterConParametros.getId());
        assertTrue(semesterConParametros.isState());
        assertNotNull(semesterConParametros.getSubjects());
        assertTrue(semesterConParametros.getSubjects().isEmpty());
    }

    @Test
    void testGetId() {
        semester.setId(5);
        assertEquals(5, semester.getId());
    }

    @Test
    void testSetId() {
        semester.setId(10);
        assertEquals(10, semester.getId());
    }

    @Test
    void testIsEstado() {
        Semester semesterActivo = new Semester(1, 2023, 1, true);
        Semester semesterInactivo = new Semester(2, 2023, 2, false);

        assertTrue(semesterActivo.isState());
        assertFalse(semesterInactivo.isState());
    }

    @Test
    void testGetMaterias() {
        ArrayList<Enrollment> materias = semester.getSubjects();

        assertNotNull(materias);
        assertTrue(materias.isEmpty());
        assertSame(materias, semester.getSubjects());
    }

    @Test
    void testAgregarMateria() {
        semester.agregarMateria(enrollment1);

        assertEquals(1, semester.getSubjects().size());
        assertTrue(semester.getSubjects().contains(enrollment1));
    }

    @Test
    void testAgregarVariasMaterias() {
        semester.agregarMateria(enrollment1);
        semester.agregarMateria(enrollment2);

        assertEquals(2, semester.getSubjects().size());
        assertTrue(semester.getSubjects().contains(enrollment1));
        assertTrue(semester.getSubjects().contains(enrollment2));
    }

    @Test
    void testQuitarMateria() {
        semester.agregarMateria(enrollment1);
        semester.agregarMateria(enrollment2);

        semester.quitarMateria(enrollment1);

        assertEquals(1, semester.getSubjects().size());
        assertFalse(semester.getSubjects().contains(enrollment1));
        assertTrue(semester.getSubjects().contains(enrollment2));
    }

    @Test
    void testQuitarMateriaQueNoExiste() {
        semester.agregarMateria(enrollment1);
        int tamanoInicial = semester.getSubjects().size();

        Enrollment enrollmentNoExistente = new Enrollment(group2, 99, "activa", 3.0);
        semester.quitarMateria(enrollmentNoExistente);

        assertEquals(tamanoInicial, semester.getSubjects().size());
        assertTrue(semester.getSubjects().contains(enrollment1));
    }

    @Test
    void testQuitarMateriaDeListaVacia() {
        semester.quitarMateria(enrollment1);
        assertTrue(semester.getSubjects().isEmpty());
    }

    @Test
    void testCancelarMateria() {
        assertEquals("activa", enrollment1.getState());

        semester.cancelarMateria(enrollment1);

        assertEquals("cancelada", enrollment1.getState());
    }



    @Test
    void testCalcularPromedioSemestreConNotasExtremas() {
        Enrollment enrollmentNotaAlta = new Enrollment(group1, 3, "activa", 5.0);
        Enrollment enrollmentNotaBaja = new Enrollment(group2, 4, "activa", 1.0);

        semester.agregarMateria(enrollmentNotaAlta);
        semester.agregarMateria(enrollmentNotaBaja);

        semester.calcularPromedioSemestre();
        assertEquals(2, semester.getAverageSemesterGrade());
    }

}
