package com.example.fantasticosback.util;

import com.example.fantasticosback.Model.Grupo;
import com.example.fantasticosback.Model.Materia;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MateriaTest {

    @Test
    void testGetters() {
        Materia materia = new Materia("1", "Programación", 3, 1);
        assertEquals("1", materia.getIdMateria());
        assertEquals("Programación", materia.getNombre());
        assertEquals(3, materia.getCreditos());
        assertEquals(1, materia.getSemestre());
    }

    @Test
    void testSetCreditos() {
        Materia materia = new Materia("1", "Programación", 3, 1);
        materia.setCreditos(4);
        assertEquals(4, materia.getCreditos());
    }

    @Test
    void testAgregarGrupoYObtenerActivos() {
        Materia materia = new Materia("1", "Programación", 3, 1);

        Grupo grupoActivo = mock(Grupo.class);
        Grupo grupoInactivo = mock(Grupo.class);

        when(grupoActivo.isEstado()).thenReturn(true);
        when(grupoInactivo.isEstado()).thenReturn(false);

        materia.agregarGrupo(grupoActivo);
        materia.agregarGrupo(grupoInactivo);

        List<Grupo> activos = materia.getGrupos();
        assertEquals(1, activos.size());
        assertTrue(activos.contains(grupoActivo));
    }

    @Test
    void testMostrarInformacion() {
        Materia materia = new Materia("1", "Programación", 3, 1);
        assertDoesNotThrow(materia::mostrarInformacion);
    }
}
