package com.example.fantasticosback.util;

import com.example.fantasticosback.Model.Subject;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SubjectCatalogTest {

    @Test
    void testGetMateriaExistente() {
        Subject subject = SubjectCatalog.getMateria("CALD");
        assertNotNull(subject);
        assertEquals("Cálculo Diferencial", subject.getName());
    }

    @Test
    void testAgregarYRecuperarMateria() {
        Subject nueva = new Subject(600, "Bases de Datos", 4, 3);
        SubjectCatalog.agregarMateria("BBDD", nueva);

        Subject recuperada = SubjectCatalog.getMateria("BBDD");
        assertEquals("Bases de Datos", recuperada.getName());
    }

    @Test
    void testGetMateriasNoVacio() {
        assertFalse(SubjectCatalog.getMaterias().isEmpty());
    }
}
