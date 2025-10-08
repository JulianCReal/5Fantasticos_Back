package com.example.fantasticosback.util;

import com.example.fantasticosback.Model.Entities.Group;
import com.example.fantasticosback.Model.Entities.Subject;
import com.example.fantasticosback.Model.Entities.Teacher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class GroupTest {
    private Group group;
    private Subject subject;
    private Teacher teacher;
    private ClassSession session1;
    private ClassSession session2;

    @BeforeEach
    void setUp() {
        subject = new Subject("1", "Calculus I", 4, 1);
        teacher = new Teacher("John", "Perez", 12345678, "Mathematics");
        group = new Group(1, 101, 30, true, subject, teacher);

        session1 = new ClassSession("Monday", "08:00", "10:00", "A101");
        session2 = new ClassSession("Wednesday", "10:00", "12:00", "A102");
    }

    @Test
    void testGroupCreation() {
        assertEquals(1, group.getId());
        assertEquals(101, group.getNumber());
        assertEquals(30, group.getCapacity());
        assertTrue(group.isActive());
        assertEquals(subject, group.getSubject());
        assertNotNull(group.getSessions());
        assertTrue(group.getSessions().isEmpty());
    }

    @Test
    void testGroupCreationWithNulls() {
        Group groupWithNulls = new Group(2, 102, 25, false, null, null);

        assertEquals(2, groupWithNulls.getId());
        assertEquals(102, groupWithNulls.getNumber());
        assertEquals(25, groupWithNulls.getCapacity());
        assertFalse(groupWithNulls.isActive());
        assertNull(groupWithNulls.getSubject());
        assertNotNull(groupWithNulls.getSessions());
        assertTrue(groupWithNulls.getSessions().isEmpty());
    }

    @Test
    void testSetId() {
        group.setId(999);
        assertEquals(999, group.getId());
    }

    @Test
    void testSetNumber() {
        assertEquals(101, group.getNumber());
    }

    @Test
    void testSetCapacity() {
        assertEquals(30, group.getCapacity());
    }

    @Test
    void testSetActive() {
        assertTrue(group.isActive());
    }

    @Test
    void testSetSubject() {
        Subject newSubject = new Subject("2", "Physics I", 3, 2);
        group.setSubject(newSubject);
        assertEquals(newSubject, group.getSubject());

        group.setSubject(null);
        assertNull(group.getSubject());
    }

    @Test
    void testGetSessions() {
        // Test initial empty sessions
        ArrayList<ClassSession> sessions = group.getSessions();
        assertNotNull(sessions);
        assertTrue(sessions.isEmpty());
        assertEquals(0, sessions.size());
    }

    @Test
    void testAddSession() {
        // Add first session
        group.addSession(session1);

        assertEquals(1, group.getSessions().size());
        assertTrue(group.getSessions().contains(session1));
        assertEquals(session1, group.getSessions().get(0));
    }

    @Test
    void testAddMultipleSessions() {
        // Add first session
        group.addSession(session1);
        group.addSession(session2);

        assertEquals(2, group.getSessions().size());
        assertTrue(group.getSessions().contains(session1));
        assertTrue(group.getSessions().contains(session2));
        assertEquals(session1, group.getSessions().get(0));
        assertEquals(session2, group.getSessions().get(1));
    }

    @Test
    void testAddNullSession() {
        // Add null session
        group.addSession(null);

        assertEquals(1, group.getSessions().size());
        assertTrue(group.getSessions().contains(null));
    }

    @Test
    void testAddDuplicateSession() {
        // Add the same session twice
        group.addSession(session1);
        group.addSession(session1);

        assertEquals(2, group.getSessions().size());
        assertEquals(session1, group.getSessions().get(0));
        assertEquals(session1, group.getSessions().get(1));
    }

    @Test
    void testActiveState() {
        Group activeGroup = new Group(1, 101, 30, true, subject, teacher);
        assertTrue(activeGroup.isActive());
        Group inactiveGroup = new Group(2, 102, 25, false, subject, teacher);
        assertFalse(inactiveGroup.isActive());
    }

    @Test
    void testCapacityVariations() {
        Group groupCapacity50 = new Group(1, 101, 50, true, subject, teacher);
        assertEquals(50, groupCapacity50.getCapacity());

        Group groupCapacity0 = new Group(2, 102, 0, true, subject, teacher);
        assertEquals(0, groupCapacity0.getCapacity());

        Group groupNegativeCapacity = new Group(3, 103, -5, true, subject, teacher);
        assertEquals(-5, groupNegativeCapacity.getCapacity());
    }

    @Test
    void testNumberVariations() {
        Group group1 = new Group(1, 101, 30, true, subject, teacher);
        assertEquals(101, group1.getNumber());

        Group group2 = new Group(2, 999, 30, true, subject, teacher);
        assertEquals(999, group2.getNumber());

        Group group3 = new Group(3, 0, 30, true, subject, teacher);
        assertEquals(0, group3.getNumber());
    }
}
