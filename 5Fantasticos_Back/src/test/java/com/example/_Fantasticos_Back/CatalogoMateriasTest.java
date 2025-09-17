package com.example._Fantasticos_Back;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CatalogoMateriasTest {

    @Test
    void testObtenerMateriaExistente() {
        Materia m = CatalogoMaterias.getMateria("CALD");
        assertNotNull(m);
        assertEquals("Cálculo Diferencial", m.getNombre());
    }

    @Test
    void testObtenerMateriaInexistente() {
        Materia m = CatalogoMaterias.getMateria("XXX999");
        assertNull(m);
    }

    @Test
    void testAgregarMateria() {
        Materia nueva = new Materia(401, "Modelos y Servicios de Datos", 4, 3);
        CatalogoMaterias.agregarMateria("MYSD", nueva);
        assertEquals(nueva, CatalogoMaterias.getMateria("MYSD"));
    }
}