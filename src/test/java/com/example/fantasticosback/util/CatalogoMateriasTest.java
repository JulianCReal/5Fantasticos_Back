package com.example.fantasticosback.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CatalogoMateriasTest {

    @Test
    void testGetMateriaExistente() {
        Materia materia = CatalogoMaterias.getMateria("CALD");
        assertNotNull(materia);
        assertEquals("Cálculo Diferencial", materia.getNombre());
    }

    @Test
    void testAgregarYRecuperarMateria() {
        Materia nueva = new Materia(600, "Bases de Datos", 4, 3);
        CatalogoMaterias.agregarMateria("BBDD", nueva);

        Materia recuperada = CatalogoMaterias.getMateria("BBDD");
        assertEquals("Bases de Datos", recuperada.getNombre());
    }

    @Test
    void testGetMateriasNoVacio() {
        assertFalse(CatalogoMaterias.getMaterias().isEmpty());
    }
}
