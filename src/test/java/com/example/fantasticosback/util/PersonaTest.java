package com.example.fantasticosback.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PersonaTest {

    static class PersonaDummy extends Persona {
        public PersonaDummy(String nombre, String apellido, int documento) {
            super(nombre, apellido, documento);
        }

        @Override
        public void mostrarInformacion() {
            // no-op
        }
    }

    @Test
    void testGettersAndSetters() {
        PersonaDummy persona = new PersonaDummy("Ana", "García", 123);
        assertEquals("Ana", persona.getNombre());
        assertEquals("García", persona.getApellido());
        assertEquals(123, persona.getDocumento());

        persona.setNombre("Laura");
        persona.setApellido("López");
        persona.setDocumento(456);

        assertEquals("Laura", persona.getNombre());
        assertEquals("López", persona.getApellido());
        assertEquals(456, persona.getDocumento());
    }

    @Test
    void testToString() {
        PersonaDummy persona = new PersonaDummy("Ana", "García", 123);
        String result = persona.toString();
        assertTrue(result.contains("Ana"));
        assertTrue(result.contains("García"));
        assertTrue(result.contains("123"));
    }

    @Test
    void testMostrarInformacion() {
        PersonaDummy persona = new PersonaDummy("Ana", "García", 123);
        assertDoesNotThrow(persona::mostrarInformacion);
    }
}
