package com.example.fantasticosback.util;
import com.example.fantasticosback.Model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

public class DeanOfficeTest {
    private DeanOffice deanOffice;
    private Student student;
    private Subject subject1;
    private Subject subject2;
    private Teacher teacher;
    private Group originGroup;
    private Group destinationGroup;
    private Group inactiveGroup;
    private Group fullGroup;
    private Request groupChangeRequest;
    private Request subjectChangeRequest;

    @BeforeEach
    void setUp() {
        deanOffice = new DeanOffice("DEC001", "Systems Engineering");
        Career career = new Career("Systems Engineering", 160);
        AcademicTrafficLight trafficLight = new AcademicTrafficLight(1, 0, career);

        student = new Student(
                "Maria", "Gonzalez", 98765, "Systems Engineering",
                "2020001", "est001", 4, trafficLight
        );

        deanOffice.addStudent(student);

        teacher = new Teacher("Dr. Carlos", "Martinez", 123456, "Systems Engineering");

        subject1 = SubjectCatalog.getSubject("AYSR");
        subject2 = SubjectCatalog.getSubject("DOPO");

        // Crear grupos sin referencia a subject
        originGroup = new Group(1, 1, 25, true, teacher);
        destinationGroup = new Group(2, 2, 30, true, teacher);
        inactiveGroup = new Group(3, 3, 20, false, teacher);
        fullGroup = new Group(4, 4, 0, true, teacher);

        originGroup.addSession(new ClassSession("Monday", "07:00", "08:30", "A-101"));
        destinationGroup.addSession(new ClassSession("Tuesday", "14:30", "16:00", "H-303"));
        inactiveGroup.addSession(new ClassSession("Wednesday", "10:00", "11:30", "C-203"));
        fullGroup.addSession(new ClassSession("Thursday", "16:00", "17:30", "D-307"));

        Semester semester = new Semester(1, 2025, 2, true);
        student.getSemesters().add(semester);
        student.addSubject(originGroup, subject1); // Ahora requiere tanto Group como Subject

        Enrollment originEnrollment = student.getSemesters().get(0).getSubjects().get(0);
        groupChangeRequest = student.createRequest("group", originEnrollment, destinationGroup, subject1, "Change due to work schedule");
        subjectChangeRequest = student.createRequest("subject", originEnrollment, fullGroup, subject2, "Career change");
    }

    @Test
    @DisplayName("Valid group change request should be approved")
    void testSuccessfulGroupChange() {
        subject1.addGroup(destinationGroup);

        deanOffice.manageRequest(student, groupChangeRequest);

        assertEquals("Accepted", groupChangeRequest.getState().getStateName());
    }

    @Test
    @DisplayName("Reject group change if group is inactive")
    void testGroupChangeInactiveGroup() {
        Request request = student.createRequest("group",
                student.getSemesters().get(0).getSubjects().get(0), inactiveGroup, subject1, "Inactive destination");

        deanOffice.manageRequest(student, request);

        assertEquals("Rejected", request.getState().getStateName());
    }

    @Test
    @DisplayName("Reject group change if no available spots")
    void testGroupChangeNoSpots() {
        Request request = student.createRequest("group",
                student.getSemesters().get(0).getSubjects().get(0), fullGroup, subject2, "No spots");

        deanOffice.manageRequest(student, request);

        assertEquals("Rejected", request.getState().getStateName());
    }

    @Test
    @DisplayName("Reject if there is schedule conflict")
    void testGroupChangeWithConflict() {
        Group conflictGroup = new Group(6, 8, 25, true, teacher);
        conflictGroup.addSession(new ClassSession("Tuesday", "13:00", "16:00", "F-206"));
        student.addSubject(conflictGroup, subject2);

        Request request = student.createRequest("group",
                student.getSemesters().get(0).getSubjects().get(0), destinationGroup, subject1, "Schedule conflict");

        deanOffice.manageRequest(student, request);

        assertEquals("Rejected", request.getState().getStateName());
    }

    @Test
    @DisplayName("Valid subject change is approved")
    void testSuccessfulSubjectChange() {
        Subject catalogSubject = SubjectCatalog.getSubject("CALD"); // Usar una materia del catálogo
        Group validDestinationGroup = new Group(7, 7, 30, true, teacher);
        validDestinationGroup.addSession(new ClassSession("Friday", "10:00", "11:30", "G-106"));

        catalogSubject.addGroup(validDestinationGroup);

        Request request = student.createRequest("subject",
                student.getSemesters().get(0).getSubjects().get(0), validDestinationGroup, catalogSubject, "Valid change");

        deanOffice.manageRequest(student, request);

        assertEquals("Accepted", request.getState().getStateName());
    }

    @Test
    @DisplayName("Reject subject change if destination group is inactive")
    void testSubjectChangeInactiveGroup() {
        Group inactiveSubject2Group = new Group(8, 8, 25, false, teacher);
        inactiveSubject2Group.addSession(new ClassSession("Saturday", "08:30", "10:00", "H-808"));

        Request request = student.createRequest("subject",
                student.getSemesters().get(0).getSubjects().get(0), inactiveSubject2Group, subject2, "Inactive");

        deanOffice.manageRequest(student, request);

        assertEquals("Rejected", request.getState().getStateName());
    }

    @Test
    @DisplayName("Reject subject change if no available spots")
    void testSubjectChangeNoSpots() {
        deanOffice.manageRequest(student, subjectChangeRequest);

        assertEquals("Rejected", subjectChangeRequest.getState().getStateName());
    }

    @Test
    @DisplayName("Invalid request type should be rejected")
    void testInvalidType() {
        Request request = student.createRequest("other",
                student.getSemesters().get(0).getSubjects().get(0), destinationGroup, subject1, "Invalid type");

        deanOffice.manageRequest(student, request);

        assertEquals("Rejected", request.getState().getStateName());
    }

    @Test
    @DisplayName("No conflict if student has no semesters")
    void testNoSemesters() {
        Career tempCareer = new Career("Medicine", 200);
        AcademicTrafficLight tempTrafficLight = new AcademicTrafficLight(1, 0, tempCareer);
        Student newStudent = new Student("Ana", "Lopez", 54321, "Medicine", "2022001", "est002", 1, tempTrafficLight);

        Enrollment originEnrollment = student.getSemesters().get(0).getSubjects().get(0);
        Request request = newStudent.createRequest("group", originEnrollment, destinationGroup, subject1, "No semesters");

        deanOffice.manageRequest(newStudent, request);

        assertEquals("Accepted", request.getState().getStateName());
    }

    @Test
    @DisplayName("List all requests from the faculty")
    void testListRequests() {
        Career tempCareer = new Career("Systems Engineering", 160);
        AcademicTrafficLight tempTrafficLight = new AcademicTrafficLight(1, 0, tempCareer);
        Student student1 = new Student(
                "Luis", "Ramirez", 11223, "Systems Engineering",
                "2021002", "est003", 3, tempTrafficLight
        );
        deanOffice.addStudent(student1);
        student1.getSemesters().add(new Semester(1, 2025, 2, true));
        student1.addSubject(originGroup, subject1);
        Enrollment enrollment1 = student1.getSemesters().get(0).getSubjects().get(0);
        student1.createRequest("group", enrollment1, destinationGroup, subject1, "Schedule change");
        assertEquals(3, deanOffice.getRequestsByFaculty().size());
    }
}
