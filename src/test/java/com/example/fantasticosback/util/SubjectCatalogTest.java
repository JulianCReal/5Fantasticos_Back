package com.example.fantasticosback.util;

import com.example.fantasticosback.Model.Document.Subject;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SubjectCatalogTest {

    @Test
    void testGetExistingSubject() {
        Subject subject = SubjectCatalog.getSubject("CALD");
        assertNotNull(subject);
        assertEquals("Cálculo Diferencial", subject.getName());
    }

    @Test
    void testAddAndRetrieveSubject() {
        Subject newSubject = new Subject("600", "Sistemas de Bases de Datos", 4, 3);
        SubjectCatalog.addSubject("BBDD", newSubject);

        Subject retrieved = SubjectCatalog.getSubject("BBDD");
        assertEquals("Sistemas de Bases de Datos", retrieved.getName());
    }

    @Test
    void testGetSubjectsNotEmpty() {
        assertFalse(SubjectCatalog.getSubjects().isEmpty());
    }
}
