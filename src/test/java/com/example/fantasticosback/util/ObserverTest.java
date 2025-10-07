package com.example.fantasticosback.util;

import com.example.fantasticosback.Model.*;
import com.example.fantasticosback.Model.Observers.DeanObserver;
import com.example.fantasticosback.Model.Observers.RequestObserver;
import com.example.fantasticosback.Model.Observers.StudentObserver;
import com.example.fantasticosback.util.SubjectCatalog;
import com.example.fantasticosback.util.AcademicTrafficLight;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class ObserverTest {
    @Test
    public void testDeanObserverNotifiedOnRequestCreation() {
        // Capture standard output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        // Arrange
        DeanObserver deanObserver = new DeanObserver();
        Career career = new Career("Engineering", 160);
        AcademicTrafficLight trafficLight = new AcademicTrafficLight(1, 0, career);
        Student student = new Student("Ana", "Garcia", 123456, "Engineering", "202301", "E01", 5, trafficLight);

        // Make the addObserver method public in Student
        student.addObserver(deanObserver);
        Subject subject1 = SubjectCatalog.getSubject("AYSR");
        Subject subject2 = SubjectCatalog.getSubject("DOPO");
        Teacher teacher = new Teacher("Dr. Carlos", "Martinez", 123456, "Systems Engineering");

        Group originGroup = new Group(1, 1, 25, true, teacher); // Eliminado el parámetro subject
        Group destinationGroup = new Group(2, 2, 30, true, teacher); // Eliminado el parámetro subject
        Enrollment currentEnrollment = new Enrollment(originGroup, subject1, 1, "enrolled", 0.0); // Agregado subject

        // Act
        student.createRequest("group", currentEnrollment, destinationGroup, subject2, "Change due to schedule conflict"); // Agregado destinationSubject

        // Restore standard output
        System.setOut(originalOut);

        // Assert
        String output = outContent.toString().trim();
        assertTrue(output.contains("A new request has been created"));
    }

    @Test
    public void testStudentObserverNotifiedOnRequestStateChange() {
        // Capture standard output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        // Arrange
        StudentObserver studentObserver = new StudentObserver();
        DeanOffice deanOffice = new DeanOffice("D01", "Engineering");

        deanOffice.addObserver(studentObserver);

        Career career = new Career("Systems Engineering", 160);
        AcademicTrafficLight trafficLight = new AcademicTrafficLight(1, 0, career);
        Student student = new Student("Carlos", "Lopez", 987654, "Systems", "202302", "E02", 6, trafficLight);

        Subject subject1 = SubjectCatalog.getSubject("AYSR");
        Subject subject2 = SubjectCatalog.getSubject("DOPO");
        Teacher teacher = new Teacher("Dr. Carlos", "Martinez", 123456, "Systems Engineering");
        Group originGroup = new Group(1, 1, 25, true, teacher); // Eliminado el parámetro subject
        Group destinationGroup = new Group(2, 2, 30, true, teacher); // Eliminado el parámetro subject

        Enrollment enrollment = new Enrollment(originGroup, subject1, 1, "active", 0.0); // Agregado subject

        Request request = new Request(
                "555",
                originGroup,
                destinationGroup,
                "group",
                "Group change",
                new Date(),
                student.getStudentId()
        );

        // Act
        deanOffice.manageRequest(student, request);

        // Restore standard output
        System.setOut(originalOut);

        // Assert
        String output = outContent.toString().trim();
        assertTrue(output.contains("Your request has been modified to"));
    }
}
