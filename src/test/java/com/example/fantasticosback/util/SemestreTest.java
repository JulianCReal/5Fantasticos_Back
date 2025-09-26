package com.example.fantasticosback.util;

import com.example.fantasticosback.Model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class SemestreTest {

    private Semestre semestre;
    private Inscripcion inscripcion1;
    private Inscripcion inscripcion2;
    private Grupo grupo1;
    private Grupo grupo2;
    private Materia materia1;
    private Materia materia2;
    private Profesor profesor;

    @BeforeEach
    void setUp() {
        semestre = new Semestre();

        // Crear objetos reales para las pruebas
        profesor = new Profesor("Juan", "Pérez", 12345678, "Sistemas");

        materia1 = new Materia(101, "Matemáticas", 3, 1);
        materia2 = new Materia(102, "Física", 4, 2);

        grupo1 = new Grupo(1, 1, 30, true, materia1, profesor);
        grupo2 = new Grupo(2, 2, 25, true, materia2, profesor);

        inscripcion1 = new Inscripcion(grupo1, 1, "activa", 4.0);
        inscripcion2 = new Inscripcion(grupo2, 2, "activa", 3.5);
    }

    @Test
    void testConstructor() {
        Semestre semestreConParametros = new Semestre(1, 2023, 2, true);

        assertEquals(1, semestreConParametros.getId());
        assertTrue(semestreConParametros.isEstado());
        assertNotNull(semestreConParametros.getMaterias());
        assertTrue(semestreConParametros.getMaterias().isEmpty());
    }

    @Test
    void testGetId() {
        semestre.setId(5);
        assertEquals(5, semestre.getId());
    }

    @Test
    void testSetId() {
        semestre.setId(10);
        assertEquals(10, semestre.getId());
    }

    @Test
    void testIsEstado() {
        Semestre semestreActivo = new Semestre(1, 2023, 1, true);
        Semestre semestreInactivo = new Semestre(2, 2023, 2, false);

        assertTrue(semestreActivo.isEstado());
        assertFalse(semestreInactivo.isEstado());
    }

    @Test
    void testGetMaterias() {
        ArrayList<Inscripcion> materias = semestre.getMaterias();

        assertNotNull(materias);
        assertTrue(materias.isEmpty());
        assertSame(materias, semestre.getMaterias());
    }

    @Test
    void testAgregarMateria() {
        semestre.agregarMateria(inscripcion1);

        assertEquals(1, semestre.getMaterias().size());
        assertTrue(semestre.getMaterias().contains(inscripcion1));
    }

    @Test
    void testAgregarVariasMaterias() {
        semestre.agregarMateria(inscripcion1);
        semestre.agregarMateria(inscripcion2);

        assertEquals(2, semestre.getMaterias().size());
        assertTrue(semestre.getMaterias().contains(inscripcion1));
        assertTrue(semestre.getMaterias().contains(inscripcion2));
    }

    @Test
    void testQuitarMateria() {
        semestre.agregarMateria(inscripcion1);
        semestre.agregarMateria(inscripcion2);

        semestre.quitarMateria(inscripcion1);

        assertEquals(1, semestre.getMaterias().size());
        assertFalse(semestre.getMaterias().contains(inscripcion1));
        assertTrue(semestre.getMaterias().contains(inscripcion2));
    }

    @Test
    void testQuitarMateriaQueNoExiste() {
        semestre.agregarMateria(inscripcion1);
        int tamanoInicial = semestre.getMaterias().size();

        Inscripcion inscripcionNoExistente = new Inscripcion(grupo2, 99, "activa", 3.0);
        semestre.quitarMateria(inscripcionNoExistente);

        assertEquals(tamanoInicial, semestre.getMaterias().size());
        assertTrue(semestre.getMaterias().contains(inscripcion1));
    }

    @Test
    void testQuitarMateriaDeListaVacia() {
        semestre.quitarMateria(inscripcion1);
        assertTrue(semestre.getMaterias().isEmpty());
    }

    @Test
    void testCancelarMateria() {
        assertEquals("activa", inscripcion1.getEstado());

        semestre.cancelarMateria(inscripcion1);

        assertEquals("cancelada", inscripcion1.getEstado());
    }



    @Test
    void testCalcularPromedioSemestreConNotasExtremas() {
        Inscripcion inscripcionNotaAlta = new Inscripcion(grupo1, 3, "activa", 5.0);
        Inscripcion inscripcionNotaBaja = new Inscripcion(grupo2, 4, "activa", 1.0);

        semestre.agregarMateria(inscripcionNotaAlta);
        semestre.agregarMateria(inscripcionNotaBaja);

        semestre.calcularPromedioSemestre();
        assertEquals(2, semestre.getPromedioSemestre());
    }

}
