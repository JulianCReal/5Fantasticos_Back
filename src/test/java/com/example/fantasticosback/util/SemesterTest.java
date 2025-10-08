package com.example.fantasticosback.util;

import com.example.fantasticosback.Model.Entities.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class SemesterTest {

    private Semester semester;
    private Enrollment enrollment1;
    private Enrollment enrollment2;
    private Group group1;
    private Group group2;
    private Subject subject1;
    private Subject subject2;
    private Teacher teacher;

    @BeforeEach
    void setUp() {
        semester = new Semester();


        teacher = new Teacher("Juan", "Perez", 12345678, "Systems");

        subject1 = new Subject("101", "Mathematics", 3, 1);
        subject2 = new Subject("102", "Physics", 4, 2);

        group1 = new Group(1, 1, 30, true, teacher);
        group2 = new Group(2, 2, 25, true, teacher);

        enrollment1 = new Enrollment(group1, subject1, 1, "active", 4.0);
        enrollment2 = new Enrollment(group2, subject2, 2, "active", 3.5);
    }

    @Test
    void testConstructor() {
        Semester semesterWithParameters = new Semester(1, 2023, 2, true);

        assertEquals(1, semesterWithParameters.getId());
        assertTrue(semesterWithParameters.isActive());
        assertNotNull(semesterWithParameters.getSubjects());
        assertTrue(semesterWithParameters.getSubjects().isEmpty());
    }

    @Test
    void testGetId() {
        semester.setId(5);
        assertEquals(5, semester.getId());
    }

    @Test
    void testSetId() {
        semester.setId(10);
        assertEquals(10, semester.getId());
    }

    @Test
    void testIsActive() {
        Semester activeSemester = new Semester(1, 2023, 1, true);
        Semester inactiveSemester = new Semester(2, 2023, 2, false);

        assertTrue(activeSemester.isActive());
        assertFalse(inactiveSemester.isActive());
    }

    @Test
    void testGetSubjects() {
        ArrayList<Enrollment> subjects = semester.getSubjects();

        assertNotNull(subjects);
        assertTrue(subjects.isEmpty());
        assertSame(subjects, semester.getSubjects());
    }

    @Test
    void testAddSubject() {
        semester.addSubject(enrollment1);

        assertEquals(1, semester.getSubjects().size());
        assertTrue(semester.getSubjects().contains(enrollment1));
    }

    @Test
    void testAddMultipleSubjects() {
        semester.addSubject(enrollment1);
        semester.addSubject(enrollment2);

        assertEquals(2, semester.getSubjects().size());
        assertTrue(semester.getSubjects().contains(enrollment1));
        assertTrue(semester.getSubjects().contains(enrollment2));
    }

    @Test
    void testRemoveSubject() {
        semester.addSubject(enrollment1);
        semester.addSubject(enrollment2);

        semester.removeSubject(enrollment1);

        assertEquals(1, semester.getSubjects().size());
        assertFalse(semester.getSubjects().contains(enrollment1));
        assertTrue(semester.getSubjects().contains(enrollment2));
    }

    @Test
    void testRemoveNonExistentSubject() {
        semester.addSubject(enrollment1);
        int initialSize = semester.getSubjects().size();

        Enrollment nonExistentEnrollment = new Enrollment(group2, subject2, 99, "active", 3.0);
        semester.removeSubject(nonExistentEnrollment);

        assertEquals(initialSize, semester.getSubjects().size());
        assertTrue(semester.getSubjects().contains(enrollment1));
    }

    @Test
    void testRemoveSubjectFromEmptyList() {
        semester.removeSubject(enrollment1);
        assertTrue(semester.getSubjects().isEmpty());
    }

    @Test
    void testCancelSubject() {
        assertEquals("active", enrollment1.getStatus());

        semester.cancelSubject(enrollment1);

        assertEquals("cancelled", enrollment1.getStatus());
    }

    @Test
    void testCalculateSemesterAverageWithExtremeGrades() {
        Enrollment highGradeEnrollment = new Enrollment(group1, subject1, 3, "active", 5.0); // 3 credits
        Enrollment lowGradeEnrollment = new Enrollment(group2, subject2, 4, "active", 1.0);  // 4 credits

        semester.addSubject(highGradeEnrollment);
        semester.addSubject(lowGradeEnrollment);

        semester.calculateSemesterAverage();
        assertEquals(2, semester.getSemesterAverage());
    }
}
