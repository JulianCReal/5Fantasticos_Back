package com.example.fantasticosback;

import com.example.fantasticosback.util.Estudiante;
import com.example.fantasticosback.util.Materia;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class EstudianteTest {

    private Estudiante estudiante;

    @BeforeEach
    void setUp() {
        estudiante = new Estudiante("Sergio Alejandro", "Idarraga Torres", 1023456789, "Ingeniería", 6);
    }

    @Test
    void testAgregarMateriaCorrecta() {
        estudiante.agregarMateriaAlHorario("AYSR");
        assertTrue(estudiante.getHorario().containsKey("AYSR"));
    }

    @Test
    void testAgregarMateriaDuplicada() {
        estudiante.agregarMateriaAlHorario("AYSR");
        estudiante.agregarMateriaAlHorario("AYSR");
        assertEquals(1, estudiante.getHorario().size());
    }

    @Test
    void testAgregarMateriaInexistente() {
        estudiante.agregarMateriaAlHorario("000");
        assertFalse(estudiante.getHorario().containsKey("000"));
    }

    @Test
    void testGetHorario() {
        estudiante.agregarMateriaAlHorario("AYSR");
        Materia m = estudiante.getHorario().get("AYSR");
        assertNotNull(m);
        assertEquals("Arquitectura y Servicios de Red", m.getNombre());
    }


}
