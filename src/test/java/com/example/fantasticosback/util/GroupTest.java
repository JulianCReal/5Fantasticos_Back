package com.example.fantasticosback.util;

import com.example.fantasticosback.Model.Group;
import com.example.fantasticosback.Model.Subject;
import com.example.fantasticosback.Model.Professor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;

public class GroupTest {

    private Group group;
    private Subject subject;
    private Professor professor;
    private ClassSession sesion1;
    private ClassSession sesion2;

    @BeforeEach
    void setUp() {
        subject = new Subject(1, "Cálculo I", 4, 1);
        professor = new Professor("Juan", "Pérez", 12345678, "Matemáticas");
        group = new Group(1, 101, 30, true, subject, professor);

        sesion1 = new ClassSession("Lunes", "08:00", "10:00", "A101");
        sesion2 = new ClassSession("Miércoles", "10:00", "12:00", "A102");
    }

    @Test
    void testConstructor() {
        assertEquals(1, group.getId());
        assertEquals(101, group.getNumber());
        assertEquals(30, group.getCapacity());
        assertTrue(group.isEstado());
        assertEquals(subject, group.getMateria());
        assertNotNull(group.getSesiones());
        assertTrue(group.getSesiones().isEmpty());
    }

    @Test
    void testConstructorConParametrosNulos() {
        Group groupConNulos = new Group(2, 102, 25, false, null, null);

        assertEquals(2, groupConNulos.getId());
        assertEquals(102, groupConNulos.getNumber());
        assertEquals(25, groupConNulos.getCapacity());
        assertFalse(groupConNulos.isEstado());
        assertNull(groupConNulos.getMateria());
        assertNotNull(groupConNulos.getSesiones());
        assertTrue(groupConNulos.getSesiones().isEmpty());
    }

    @Test
    void testGettersYSetters() {
        group.setId(999);
        assertEquals(999, group.getId());

        assertEquals(101, group.getNumber());


        assertEquals(30, group.getCapacity());

        assertTrue(group.isEstado());

        Subject nuevaSubject = new Subject(2, "Física I", 3, 2);
        group.setMateria(nuevaSubject);
        assertEquals(nuevaSubject, group.getMateria());

        group.setMateria(null);
        assertNull(group.getMateria());
    }

    @Test
    void testGetSesiones() {

        ArrayList<ClassSession> sesiones = group.getSesiones();
        assertNotNull(sesiones);
        assertTrue(sesiones.isEmpty());
        assertEquals(0, sesiones.size());
    }

    @Test
    void testAgregarSesion() {

        group.agregarSesion(sesion1);

        assertEquals(1, group.getSesiones().size());
        assertTrue(group.getSesiones().contains(sesion1));
        assertEquals(sesion1, group.getSesiones().get(0));
    }

    @Test
    void testAgregarMultiplesSesiones() {

        group.agregarSesion(sesion1);
        group.agregarSesion(sesion2);

        assertEquals(2, group.getSesiones().size());
        assertTrue(group.getSesiones().contains(sesion1));
        assertTrue(group.getSesiones().contains(sesion2));
        assertEquals(sesion1, group.getSesiones().get(0));
        assertEquals(sesion2, group.getSesiones().get(1));
    }

    @Test
    void testAgregarSesionNula() {

        group.agregarSesion(null);

        assertEquals(1, group.getSesiones().size());
        assertTrue(group.getSesiones().contains(null));
    }

    @Test
    void testAgregarSesionDuplicada() {

        group.agregarSesion(sesion1);
        group.agregarSesion(sesion1);

        assertEquals(2, group.getSesiones().size());
        assertEquals(sesion1, group.getSesiones().get(0));
        assertEquals(sesion1, group.getSesiones().get(1));
    }

    @Test
    void testEstadoGrupo() {
        Group groupActivo = new Group(1, 101, 30, true, subject, professor);
        assertTrue(groupActivo.isEstado());
        Group groupInactivo = new Group(2, 102, 25, false, subject, professor);
        assertFalse(groupInactivo.isEstado());
    }

    @Test
    void testCapacidadGrupo() {
        Group groupCapacidad50 = new Group(1, 101, 50, true, subject, professor);
        assertEquals(50, groupCapacidad50.getCapacity());

        Group groupCapacidad0 = new Group(2, 102, 0, true, subject, professor);
        assertEquals(0, groupCapacidad0.getCapacity());

        Group groupCapacidadNegativa = new Group(3, 103, -5, true, subject, professor);
        assertEquals(-5, groupCapacidadNegativa.getCapacity());
    }

    @Test
    void testNumeroGrupo() {
        Group group1 = new Group(1, 101, 30, true, subject, professor);
        assertEquals(101, group1.getNumber());

        Group group2 = new Group(2, 999, 30, true, subject, professor);
        assertEquals(999, group2.getNumber());

        Group group3 = new Group(3, 0, 30, true, subject, professor);
        assertEquals(0, group3.getNumber());
    }
}
