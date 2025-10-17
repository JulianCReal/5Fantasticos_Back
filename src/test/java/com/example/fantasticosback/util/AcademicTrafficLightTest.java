/**package com.example.fantasticosback.util;

import com.example.fantasticosback.model.Document.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AcademicTrafficLightTest {
    private AcademicTrafficLight trafficLight;
    private Semester semester1;

    @BeforeEach
    void setUp() {
        Career career = new Career("Systems Engineering", 160);
        trafficLight = new AcademicTrafficLight(1, 0, career);

        semester1 = new Semester(1, 2024, 2, false);

        Group group1 = new Group(1, 1, 30, true, new Teacher("Dr. Carlos", "Martinez", 123456, "Systems Engineering"));
        Enrollment enrollment1 = new Enrollment(group1, SubjectCatalog.getSubject("AYSR"), 1, "approved", 4.0);

        Group group2 = new Group(2, 1, 30, true, new Teacher("Dr. Ana", "Lopez", 654321, "Systems Engineering"));
        Enrollment enrollment2 = new Enrollment(group2, SubjectCatalog.getSubject("DOPO"), 2, "approved", 3.5);

        Group group3 = new Group(3, 1, 30, true, new Teacher("Dr. Juan", "Perez", 112233, "Mathematics"));
        Enrollment enrollment3 = new Enrollment(group3, SubjectCatalog.getSubject("CALD"), 3, "failed", 2.7);

        semester1.addSubject(enrollment1);
        semester1.addSubject(enrollment2);
        semester1.addSubject(enrollment3);
    }

    @Test
    void testAcademicTrafficLightCreation() {
        // Test basic creation
        assertNotNull(trafficLight);
        assertEquals(1, trafficLight.getId());
        assertEquals(0, trafficLight.getProgressPercentage());
        assertEquals(0.0, trafficLight.getCumulativeAverage());
    }

    @Test
    void testAddSemester() {
        // Test adding semester
        trafficLight.addSemester(semester1);
        assertEquals(1, trafficLight.getSemesters().size());
        assertEquals(semester1, trafficLight.getSemesters().get(0));
    }

    @Test
    void testUpdateProgress() {
        // Test updating progress with semester data
        trafficLight.addSemester(semester1);
        trafficLight.updateProgress(semester1.getSubjects());
        assertEquals(5, trafficLight.getProgressPercentage());
        assertEquals(3.5, trafficLight.getCumulativeAverage(), 0.001);
    }
}**/
