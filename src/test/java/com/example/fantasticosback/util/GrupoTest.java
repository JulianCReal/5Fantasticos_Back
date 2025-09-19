package com.example.fantasticosback.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;

public class GrupoTest {

    private Grupo grupo;
    private Materia materia;
    private Profesor profesor;
    private SesionClase sesion1;
    private SesionClase sesion2;

    @BeforeEach
    void setUp() {
        materia = new Materia(1, "Cálculo I", 4, 1);
        profesor = new Profesor("Juan", "Pérez", 12345678, "Matemáticas");
        grupo = new Grupo(1, 101, 30, true, materia, profesor);

        sesion1 = new SesionClase("Lunes", "08:00", "10:00", "A101");
        sesion2 = new SesionClase("Miércoles", "10:00", "12:00", "A102");
    }

    @Test
    void testConstructor() {
        assertEquals(1, grupo.getId());
        assertEquals(101, grupo.getNumero());
        assertEquals(30, grupo.getCapacidad());
        assertTrue(grupo.isEstado());
        assertEquals(materia, grupo.getMateria());
        assertNotNull(grupo.getSesiones());
        assertTrue(grupo.getSesiones().isEmpty());
    }

    @Test
    void testConstructorConParametrosNulos() {
        Grupo grupoConNulos = new Grupo(2, 102, 25, false, null, null);

        assertEquals(2, grupoConNulos.getId());
        assertEquals(102, grupoConNulos.getNumero());
        assertEquals(25, grupoConNulos.getCapacidad());
        assertFalse(grupoConNulos.isEstado());
        assertNull(grupoConNulos.getMateria());
        assertNotNull(grupoConNulos.getSesiones());
        assertTrue(grupoConNulos.getSesiones().isEmpty());
    }

    @Test
    void testGettersYSetters() {
        grupo.setId(999);
        assertEquals(999, grupo.getId());

        assertEquals(101, grupo.getNumero());


        assertEquals(30, grupo.getCapacidad());

        assertTrue(grupo.isEstado());

        Materia nuevaMateria = new Materia(2, "Física I", 3, 2);
        grupo.setMateria(nuevaMateria);
        assertEquals(nuevaMateria, grupo.getMateria());

        grupo.setMateria(null);
        assertNull(grupo.getMateria());
    }

    @Test
    void testGetSesiones() {

        ArrayList<SesionClase> sesiones = grupo.getSesiones();
        assertNotNull(sesiones);
        assertTrue(sesiones.isEmpty());
        assertEquals(0, sesiones.size());
    }

    @Test
    void testAgregarSesion() {

        grupo.agregarSesion(sesion1);

        assertEquals(1, grupo.getSesiones().size());
        assertTrue(grupo.getSesiones().contains(sesion1));
        assertEquals(sesion1, grupo.getSesiones().get(0));
    }

    @Test
    void testAgregarMultiplesSesiones() {

        grupo.agregarSesion(sesion1);
        grupo.agregarSesion(sesion2);

        assertEquals(2, grupo.getSesiones().size());
        assertTrue(grupo.getSesiones().contains(sesion1));
        assertTrue(grupo.getSesiones().contains(sesion2));
        assertEquals(sesion1, grupo.getSesiones().get(0));
        assertEquals(sesion2, grupo.getSesiones().get(1));
    }

    @Test
    void testAgregarSesionNula() {

        grupo.agregarSesion(null);

        assertEquals(1, grupo.getSesiones().size());
        assertTrue(grupo.getSesiones().contains(null));
    }

    @Test
    void testAgregarSesionDuplicada() {

        grupo.agregarSesion(sesion1);
        grupo.agregarSesion(sesion1);

        assertEquals(2, grupo.getSesiones().size());
        assertEquals(sesion1, grupo.getSesiones().get(0));
        assertEquals(sesion1, grupo.getSesiones().get(1));
    }

    @Test
    void testEstadoGrupo() {
        Grupo grupoActivo = new Grupo(1, 101, 30, true, materia, profesor);
        assertTrue(grupoActivo.isEstado());
        Grupo grupoInactivo = new Grupo(2, 102, 25, false, materia, profesor);
        assertFalse(grupoInactivo.isEstado());
    }

    @Test
    void testCapacidadGrupo() {
        Grupo grupoCapacidad50 = new Grupo(1, 101, 50, true, materia, profesor);
        assertEquals(50, grupoCapacidad50.getCapacidad());

        Grupo grupoCapacidad0 = new Grupo(2, 102, 0, true, materia, profesor);
        assertEquals(0, grupoCapacidad0.getCapacidad());

        Grupo grupoCapacidadNegativa = new Grupo(3, 103, -5, true, materia, profesor);
        assertEquals(-5, grupoCapacidadNegativa.getCapacidad());
    }

    @Test
    void testNumeroGrupo() {
        Grupo grupo1 = new Grupo(1, 101, 30, true, materia, profesor);
        assertEquals(101, grupo1.getNumero());

        Grupo grupo2 = new Grupo(2, 999, 30, true, materia, profesor);
        assertEquals(999, grupo2.getNumero());

        Grupo grupo3 = new Grupo(3, 0, 30, true, materia, profesor);
        assertEquals(0, grupo3.getNumero());
    }
}
