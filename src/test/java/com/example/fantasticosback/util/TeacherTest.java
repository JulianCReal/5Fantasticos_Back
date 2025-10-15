package com.example.fantasticosback.util;

import com.example.fantasticosback.model.Document.Subject;
import com.example.fantasticosback.model.Document.Teacher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import static org.junit.jupiter.api.Assertions.*;

public class TeacherTest {

    private Teacher teacher;

    @BeforeEach
    void setUp() {
        teacher = new Teacher("Carlos", "Ramirez", 98765, "Systems Engineering");
    }

    @Test
    void testEmptyConstructor() {
        Teacher prof = new Teacher();
        assertNotNull(prof.getAssignedSubjects());
        assertTrue(prof.getAssignedSubjects().isEmpty());
    }

    @Test
    void testGettersAndSetters() {
        teacher.setId("T123");
        assertEquals("T123", teacher.getId());

        teacher.setDepartment("Mathematics");
        assertEquals("Mathematics", teacher.getDepartment());

        assertEquals("Carlos", teacher.getName());
        assertEquals("Ramirez", teacher.getLastName());
        assertEquals(98765, teacher.getDocument());
    }

    @Test
    void testAssignSubject() {
        Subject subject = new Subject("MAT101", "Calculus I", 4, 1);
        teacher.assignSubject(subject);

        assertTrue(teacher.getAssignedSubjects().containsKey("MAT101"));
        assertEquals(subject, teacher.getAssignedSubjects().get("MAT101"));
        assertEquals(1, teacher.getAssignedSubjects().size());
    }

    @Test
    void testAssignMultipleSubjects() {
        Subject subject1 = new Subject("MAT101", "Calculus I", 4, 1);
        Subject subject2 = new Subject("PHY101", "Physics I", 3, 1);

        teacher.assignSubject(subject1);
        teacher.assignSubject(subject2);

        assertEquals(2, teacher.getAssignedSubjects().size());
        assertTrue(teacher.getAssignedSubjects().containsKey("MAT101"));
        assertTrue(teacher.getAssignedSubjects().containsKey("PHY101"));
        assertEquals(subject1, teacher.getAssignedSubjects().get("MAT101"));
        assertEquals(subject2, teacher.getAssignedSubjects().get("PHY101"));
    }

    @Test
    void testAssignDuplicateSubject() {
        Subject subject = new Subject("MAT101", "Calculus I", 4, 1);
        teacher.assignSubject(subject);
        teacher.assignSubject(subject); // Assign the same subject again

        // Should still have only one subject
        assertEquals(1, teacher.getAssignedSubjects().size());
        assertTrue(teacher.getAssignedSubjects().containsKey("MAT101"));
    }

    @Test
    void testShowInformation() {
        assertDoesNotThrow(() -> teacher.showInformation());
    }

    @Test
    void testSetAssignedSubjects() {
        HashMap<String, Subject> subjects = new HashMap<>();
        subjects.put("MAT101", new Subject("MAT101", "Calculus I", 4, 1));
        subjects.put("PHY101", new Subject("PHY101", "Physics I", 3, 1));

        teacher.setAssignedSubjects(subjects);
        assertEquals(2, teacher.getAssignedSubjects().size());
        assertTrue(teacher.getAssignedSubjects().containsKey("MAT101"));
        assertTrue(teacher.getAssignedSubjects().containsKey("PHY101"));
    }

    @Test
    void testGetAssignedSubjects() {
        HashMap<String, Subject> assignedSubjects = teacher.getAssignedSubjects();
        assertNotNull(assignedSubjects);
        assertTrue(assignedSubjects.isEmpty());

        Subject subject = new Subject("MAT101", "Calculus I", 4, 1);
        teacher.assignSubject(subject);

        assertEquals(1, assignedSubjects.size());
        assertTrue(assignedSubjects.containsKey("MAT101"));
    }
}
