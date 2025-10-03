package com.example.fantasticosback.util;

import com.example.fantasticosback.Model.Subject;
import com.example.fantasticosback.Model.Professor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import static org.junit.jupiter.api.Assertions.*;

public class ProfessorTest {

    private Professor professor;

    @BeforeEach
    void setUp() {
        professor = new Professor("Carlos", "Ramírez", 98765, "Ingeniería de Sistemas");
    }

    @Test
    void testConstructorVacio() {
        Professor prof = new Professor();
        assertNotNull(prof.getEnrolledSubjects());
        assertTrue(prof.getEnrolledSubjects().isEmpty());
    }

    @Test
    void testGettersAndSetters() {
        professor.setId("P123");
        assertEquals("P123", professor.getId());

        professor.setDepartament("Matemáticas");
        assertEquals("Matemáticas", professor.getDepartament());

        HashMap<String, Subject> mapa = new HashMap<>();
        Subject subject = new Subject(1, "Álgebra", 3, 1);
        mapa.put("1", subject);
        professor.setEnrolledSubjects(mapa);

        assertEquals(1, professor.getEnrolledSubjects().size());
        assertEquals("Álgebra", professor.getEnrolledSubjects().get("1").getName());
    }

    @Test
    void testAsignarMateriaNueva() {
        Subject subject = new Subject(10, "Programación", 3, 1);
        professor.asignarMateria(subject);

        assertEquals(1, professor.getEnrolledSubjects().size());
        assertTrue(professor.getEnrolledSubjects().containsKey("10"));
    }

    @Test
    void testAsignarMateriaDuplicada() {
        Subject subject = new Subject(20, "Bases de Datos", 4, 3);
        professor.asignarMateria(subject);
        professor.asignarMateria(subject);

        assertEquals(1, professor.getEnrolledSubjects().size());
    }

    @Test
    void testMostrarInformacion() {
        Subject subject = new Subject(30, "Redes", 4, 5);
        professor.asignarMateria(subject);

        assertDoesNotThrow(professor::mostrarInformacion);
    }
}
