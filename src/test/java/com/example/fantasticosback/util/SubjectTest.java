package com.example.fantasticosback.util;

import com.example.fantasticosback.Model.Group;
import com.example.fantasticosback.Model.Subject;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SubjectTest {

    @Test
    void testGetters() {
        Subject subject = new Subject(1, "Programación", 3, 1);
        assertEquals(1, subject.getSubjectId());
        assertEquals("Programación", subject.getName());
        assertEquals(3, subject.getCredits());
        assertEquals(1, subject.getSemester());
    }

    @Test
    void testSetCreditos() {
        Subject subject = new Subject(1, "Programación", 3, 1);
        subject.setCredits(4);
        assertEquals(4, subject.getCredits());
    }

    @Test
    void testAgregarGrupoYObtenerActivos() {
        Subject subject = new Subject(1, "Programación", 3, 1);

        Group groupActivo = mock(Group.class);
        Group groupInactivo = mock(Group.class);

        when(groupActivo.isEstado()).thenReturn(true);
        when(groupInactivo.isEstado()).thenReturn(false);

        subject.agregarGrupo(groupActivo);
        subject.agregarGrupo(groupInactivo);

        List<Group> activos = subject.getGrupos();
        assertEquals(1, activos.size());
        assertTrue(activos.contains(groupActivo));
    }

    @Test
    void testMostrarInformacion() {
        Subject subject = new Subject(1, "Programación", 3, 1);
        assertDoesNotThrow(subject::mostrarInformacion);
    }
}
