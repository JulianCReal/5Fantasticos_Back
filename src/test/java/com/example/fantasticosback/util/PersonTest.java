package com.example.fantasticosback.util;
import com.example.fantasticosback.model.Document.Person;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PersonTest {

    static class PersonDummy extends Person {
        public PersonDummy(String name, String lastName, int document) {
            super(name, lastName, document);
        }

        @Override
        public void showInformation() {
            System.out.println("PersonDummy{name='" + getName() + "', lastName='" + getLastName() + "', document=" + getDocument() + "}");
        }

        @Override
        public String toString() {
            return "PersonDummy{name='" + getName() + "', lastName='" + getLastName() + "', document=" + getDocument() + "}";
        }
    }

    @Test
    void testPersonCreationAndGetters() {
        PersonDummy person = new PersonDummy("Ana", "Garcia", 123);

        assertEquals("Ana", person.getName());
        assertEquals("Garcia", person.getLastName());
        assertEquals(123, person.getDocument());

        person.setName("Laura");
        person.setLastName("Lopez");
        person.setDocument(456);

        assertEquals("Laura", person.getName());
        assertEquals("Lopez", person.getLastName());
        assertEquals(456, person.getDocument());
    }
}
