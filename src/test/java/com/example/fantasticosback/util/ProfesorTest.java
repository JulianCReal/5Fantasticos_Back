package com.example.fantasticosback.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProfesorTest {

    private Profesor profesor;

    @BeforeEach
    void setUp() {
        profesor = new Profesor("Carlos", "Ramírez", 98765, "Ingeniería de Sistemas");
    }

    @Test
    void testConstructorVacio() {
        Profesor prof = new Profesor();
        assertNotNull(prof.getMateriasAsignadas());
        assertTrue(prof.getMateriasAsignadas().isEmpty());
    }

    @Test
    void testGettersAndSetters() {
        profesor.setId("P123");
        assertEquals("P123", profesor.getId());

        profesor.setDepartamento("Matemáticas");
        assertEquals("Matemáticas", profesor.getDepartamento());

        HashMap<String, Materia> mapa = new HashMap<>();
        Materia materia = new Materia(1, "Álgebra", 3, 1);
        mapa.put("1", materia);
        profesor.setMateriasAsignadas(mapa);

        assertEquals(1, profesor.getMateriasAsignadas().size());
        assertEquals("Álgebra", profesor.getMateriasAsignadas().get("1").getNombre());
    }

    @Test
    void testAsignarMateriaNueva() {
        Materia materia = new Materia(10, "Programación", 3, 1);
        profesor.asignarMateria(materia);

        assertEquals(1, profesor.getMateriasAsignadas().size());
        assertTrue(profesor.getMateriasAsignadas().containsKey("10"));
    }

    @Test
    void testAsignarMateriaDuplicada() {
        Materia materia = new Materia(20, "Bases de Datos", 4, 3);
        profesor.asignarMateria(materia);
        profesor.asignarMateria(materia);

        assertEquals(1, profesor.getMateriasAsignadas().size());
    }

    @Test
    void testMostrarInformacion() {
        Materia materia = new Materia(30, "Redes", 4, 5);
        profesor.asignarMateria(materia);

        assertDoesNotThrow(profesor::mostrarInformacion);
    }
}
