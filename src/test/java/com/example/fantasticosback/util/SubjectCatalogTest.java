package com.example.fantasticosback.util;

import com.example.fantasticosback.Model.Subject;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SubjectCatalogTest {

    @Test
    void testGetExistingSubject() {
        Subject subject = SubjectCatalog.getSubject("CALD");
        assertNotNull(subject);
        assertEquals("Differential Calculus", subject.getName());
    }

    @Test
    void testAddAndRetrieveSubject() {
        Subject newSubject = new Subject("600", "Database Systems", 4, 3);
        SubjectCatalog.addSubject("BBDD", newSubject);

        Subject retrieved = SubjectCatalog.getSubject("BBDD");
        assertEquals("Database Systems", retrieved.getName());
    }

    @Test
    void testGetSubjectsNotEmpty() {
        assertFalse(SubjectCatalog.getSubjects().isEmpty());
    }
}
