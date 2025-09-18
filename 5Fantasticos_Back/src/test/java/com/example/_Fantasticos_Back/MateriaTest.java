package com.example._Fantasticos_Back;

import com.example._Fantasticos_Back.util.Materia;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MateriaTest {

    @Test
    void testCrearMateria() {
        Materia m = new Materia(501, "Probabilidad y Estadística", 3, 3);
        assertEquals(501, m.getIdMateria());
        assertEquals("Probabilidad y Estadística", m.getNombre());
        assertEquals(3, m.getCreditos());
        assertEquals(3, m.getSemestre());
    }

    @Test
    void testGetters() {
        Materia m = new Materia(202, "Algebra lineal", 3, 1);
        assertAll(
                () -> assertEquals(202, m.getIdMateria()),
                () -> assertEquals("Algebra lineal", m.getNombre()),
                () -> assertEquals(3, m.getCreditos()),
                () -> assertEquals(1, m.getSemestre())
        );
    }

}
