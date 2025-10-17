/**package com.example.fantasticosback.util;

import com.example.fantasticosback.model.Document.Group;
import com.example.fantasticosback.model.Document.Subject;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SubjectTest {

    @Test
    void testSubjectCreation() {
        Subject subject = new Subject("1", "Programming", 3, 1);
        assertEquals("1", subject.getSubjectId());
        assertEquals("Programming", subject.getName());
        assertEquals(3, subject.getCredits());
        assertEquals(1, subject.getSemester());
    }

    @Test
    void testSetCredits() {
        Subject subject = new Subject("1", "Programming", 3, 1);
        subject.setCredits(4);
        assertEquals(4, subject.getCredits());
    }

    @Test
    void testAddGroups() {
        Subject subject = new Subject("1", "Programming", 3, 1);

        Group activeGroup = mock(Group.class);
        Group inactiveGroup = mock(Group.class);

        when(activeGroup.isActive()).thenReturn(true);
        when(inactiveGroup.isActive()).thenReturn(false);

        subject.addGroup(activeGroup);
        subject.addGroup(inactiveGroup);

        // getGroups() only returns active groups
        List<Group> groups = subject.getGroups();
        assertEquals(1, groups.size()); // Only active group is returned
        assertTrue(groups.contains(activeGroup));
        assertFalse(groups.contains(inactiveGroup)); // Inactive group is filtered out

        assertEquals(2, subject.getAvailableGroups().size());
        assertTrue(subject.getAvailableGroups().contains(activeGroup));
        assertTrue(subject.getAvailableGroups().contains(inactiveGroup));
    }

    @Test
    void testShowInformation() {
        Subject subject = new Subject("1", "Programming", 3, 1);
        assertDoesNotThrow(subject::showInformation);
    }
}**/
