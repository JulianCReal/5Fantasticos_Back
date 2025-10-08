package com.example.fantasticosback.util;

import com.example.fantasticosback.Model.Entities.Group;
import com.example.fantasticosback.Model.Entities.Enrollment;
import com.example.fantasticosback.Model.Entities.Subject;
import com.example.fantasticosback.Model.Entities.Teacher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EnrollmentTest {

    private Enrollment enrollment;
    private Group group;
    private Group otherGroup;
    private Subject subject;
    private Teacher teacher;
    private ClassSession session1;
    private ClassSession session2;
    private ClassSession session3;
    private ClassSession session4;

    @BeforeEach
    void setUp() {
        subject = new Subject("1", "Calculus I", 4, 1);
        teacher = new Teacher("Juan", "Perez", 12345678, "Mathematics");

        group = new Group(1, 101, 30, true, teacher);
        session1 = new ClassSession("Monday", "08:00", "10:00", "A101");
        session2 = new ClassSession("Wednesday", "10:00", "12:00", "A102");
        group.addSession(session1);
        group.addSession(session2);

        otherGroup = new Group(2, 102, 25, true, teacher);
        session3 = new ClassSession("Tuesday", "14:00", "16:00", "B201");
        session4 = new ClassSession("Thursday", "08:00", "10:00", "B202");
        otherGroup.addSession(session3);
        otherGroup.addSession(session4);

        // Crear enrollment con subject y group
        enrollment = new Enrollment(group, subject, 1, "enrolled", 0.0);
    }

    @Test
    void testConstructor() {
        assertEquals(group, enrollment.getGroup());
        assertEquals(1, enrollment.getId());
        assertEquals("enrolled", enrollment.getStatus());
        assertEquals(0.0, enrollment.getFinalGrade(), 0.001);
    }

    @Test
    void testConstructorWithVariousParameters() {
        Enrollment enrollment2 = new Enrollment(otherGroup, subject, 999, "passed", 45.5);

        assertEquals(otherGroup, enrollment2.getGroup());
        assertEquals(999, enrollment2.getId());
        assertEquals("passed", enrollment2.getStatus());
        assertEquals(45.5, enrollment2.getFinalGrade(), 0.001);
    }

    @Test
    void testConstructorWithNullGroup() {
        Enrollment nullEnrollment = new Enrollment(null, subject, 2, "pending", 25.0);

        assertNull(nullEnrollment.getGroup());
        assertEquals(2, nullEnrollment.getId());
        assertEquals("pending", nullEnrollment.getStatus());
        assertEquals(25.0, nullEnrollment.getFinalGrade(), 0.001);
    }

    @Test
    void testGetters() {
        assertEquals(group, enrollment.getGroup());
        assertEquals(1, enrollment.getId());
        assertEquals("enrolled", enrollment.getStatus());
        assertEquals(0.0, enrollment.getFinalGrade(), 0.001);
    }

    @Test
    void testSetId() {
        // Test the ID setter
        enrollment.setId(999);
        assertEquals(999, enrollment.getId());

        enrollment.setId(0);
        assertEquals(0, enrollment.getId());

        enrollment.setId(-5);
        assertEquals(-5, enrollment.getId());
    }

    @Test
    void testCancel() {
        // Verify that cancel changes status to "cancelled"
        enrollment.cancel();
        assertEquals("cancelled", enrollment.getStatus());

        // Verify that cancel from another status also works
        Enrollment passedEnrollment = new Enrollment(group, subject, 2, "passed", 40.0);
        passedEnrollment.cancel();
        assertEquals("cancelled", passedEnrollment.getStatus());
    }

    @Test
    void testEvaluatePassed() {
        Enrollment passedEnrollment = new Enrollment(group, subject, 2, "enrolled", 30.0);
        passedEnrollment.evaluate();
        assertEquals("approved", passedEnrollment.getStatus());

        Enrollment highGradeEnrollment = new Enrollment(group, subject, 3, "enrolled", 45.5);
        highGradeEnrollment.evaluate();
        assertEquals("approved", highGradeEnrollment.getStatus());

        Enrollment limitEnrollment = new Enrollment(group, subject, 4, "enrolled", 30.0);
        limitEnrollment.evaluate();
        assertEquals("approved", limitEnrollment.getStatus());
    }

    @Test
    void testEvaluateFailed() {
        Enrollment failedEnrollment = new Enrollment(group, subject, 2, "enrolled", 29.9);
        failedEnrollment.evaluate();
        assertEquals("failed", failedEnrollment.getStatus());

        Enrollment lowGradeEnrollment = new Enrollment(group, subject, 3, "enrolled", 0.0);
        lowGradeEnrollment.evaluate();
        assertEquals("failed", lowGradeEnrollment.getStatus());

        Enrollment negativeGradeEnrollment = new Enrollment(group, subject, 4, "enrolled", -5.0);
        negativeGradeEnrollment.evaluate();
        assertEquals("failed", negativeGradeEnrollment.getStatus());
    }

    @Test
    void testChangeGroup() {
        assertEquals(group, enrollment.getGroup());

        enrollment.changeGroup(otherGroup);
        assertEquals(otherGroup, enrollment.getGroup());

        enrollment.changeGroup(null);
        assertNull(enrollment.getGroup());
    }

    @Test
    void testValidateConflictNoConflict() {
        // Create enrollment with schedules that don't conflict
        Enrollment otherEnrollment = new Enrollment(otherGroup, subject, 2, "enrolled", 0.0);

        // The schedules are different: Monday 08:00-10:00, Wednesday 10:00-12:00 vs Tuesday 14:00-16:00, Thursday 08:00-10:00
        assertFalse(enrollment.validateConflict(otherEnrollment));
        assertFalse(otherEnrollment.validateConflict(enrollment));
    }

    @Test
    void testValidateConflictHasConflict() {
        // Create group with conflicting schedule
        Group conflictGroup = new Group(3, 103, 20, true, teacher);
        ClassSession conflictSession = new ClassSession("Monday", "08:00", "09:00", "C301");
        conflictGroup.addSession(conflictSession);

        Enrollment conflictEnrollment = new Enrollment(conflictGroup, subject, 3, "enrolled", 0.0);

        // Should have conflict because both have Monday session at 08:00
        assertTrue(enrollment.validateConflict(conflictEnrollment));
        assertTrue(conflictEnrollment.validateConflict(enrollment));
    }

    @Test
    void testValidateConflictSameEndTime() {
        Group finalConflictGroup = new Group(4, 104, 15, true, teacher);
        ClassSession finalConflictSession = new ClassSession("Monday", "09:00", "10:00", "D401");
        finalConflictGroup.addSession(finalConflictSession);

        Enrollment finalConflictEnrollment = new Enrollment(finalConflictGroup, subject, 4, "enrolled", 0.0);

        assertTrue(enrollment.validateConflict(finalConflictEnrollment));
        assertTrue(finalConflictEnrollment.validateConflict(enrollment));
    }

    @Test
    void testValidateConflictGroupWithoutSessions() {
        // Create group without sessions
        Group emptyGroup = new Group(5, 105, 10, true, teacher);
        Enrollment emptyEnrollment = new Enrollment(emptyGroup, subject, 5, "enrolled", 0.0);

        // Should not have conflict if one of the groups has no sessions
        assertFalse(enrollment.validateConflict(emptyEnrollment));
        assertFalse(emptyEnrollment.validateConflict(enrollment));
    }

    @Test
    void testValidateConflictBothGroupsWithoutSessions() {
        Group emptyGroup1 = new Group(6, 106, 10, true, teacher);
        Group emptyGroup2 = new Group(7, 107, 10, true, teacher);

        Enrollment emptyEnrollment1 = new Enrollment(emptyGroup1, subject, 6, "enrolled", 0.0);
        Enrollment emptyEnrollment2 = new Enrollment(emptyGroup2, subject, 7, "enrolled", 0.0);

        assertFalse(emptyEnrollment1.validateConflict(emptyEnrollment2));
        assertFalse(emptyEnrollment2.validateConflict(emptyEnrollment1));
    }
}
