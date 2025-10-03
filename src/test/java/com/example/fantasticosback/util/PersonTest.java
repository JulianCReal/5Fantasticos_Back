package com.example.fantasticosback.util;

import com.example.fantasticosback.Model.Person;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PersonTest {

    static class PersonDummy extends Person {
        public PersonDummy(String nombre, String apellido, int documento) {
            super(nombre, apellido, documento);
        }

        @Override
        public void mostrarInformacion() {
            // no-op
        }
    }

    @Test
    void testGettersAndSetters() {
        PersonDummy persona = new PersonDummy("Ana", "García", 123);
        assertEquals("Ana", persona.getNombre());
        assertEquals("García", persona.getLastName());
        assertEquals(123, persona.getDocument());

        persona.setNombre("Laura");
        persona.setLastName("López");
        persona.setDocument(456);

        assertEquals("Laura", persona.getNombre());
        assertEquals("López", persona.getLastName());
        assertEquals(456, persona.getDocument());
    }

    @Test
    void testToString() {
        PersonDummy persona = new PersonDummy("Ana", "García", 123);
        String result = persona.toString();
        assertTrue(result.contains("Ana"));
        assertTrue(result.contains("García"));
        assertTrue(result.contains("123"));
    }

    @Test
    void testMostrarInformacion() {
        PersonDummy persona = new PersonDummy("Ana", "García", 123);
        assertDoesNotThrow(persona::mostrarInformacion);
    }
}
