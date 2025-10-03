package com.example.fantasticosback.util;

import com.example.fantasticosback.Model.*;
import com.example.fantasticosback.Model.Observers.DeanRequestObserver;
import com.example.fantasticosback.Model.Observers.StudentRequestObserver;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

public class ObserverTest {
    @Test
    public void testDeanObserverNotifiedOnSolicitudCreation() {
        // Capturar salida estándar
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        // Arrange
        DeanRequestObserver deanObserver = new DeanRequestObserver();
        Career career = new Career("Ingeniería", 160);
        SemaforoAcademico semaforo = new SemaforoAcademico(1, 0, career);
        Student student = new Student("Ana", "García", 123456, "Ingeniería", "202301", "E01", 5, semaforo);

        // Hacer el método addObserver público en Estudiante
        student.addObserver(deanObserver);
        Subject subject1 = SubjectCatalog.getMateria("AYSR");
        Subject subject2 = SubjectCatalog.getMateria("DOPO");
        Professor professor = new Professor("Dr. Carlos", "Martínez", 123456, "Ingeniería de Sistemas");

        Group groupOrigen = new Group(1, 1, 25, true, subject1, professor);
        Group groupDestino = new Group(2, 2, 30, true, subject2, professor);
        Enrollment enrollmentActual = new Enrollment(groupOrigen, 1, "cursando", 0.0);

        // Act
        student.crearSolicitud("grupo", enrollmentActual, groupDestino, "Cambio por choque de horario");

        // Restaurar salida estándar
        System.setOut(originalOut);

        // Assert
        String output = outContent.toString().trim();
        assertTrue(output.contains("Se ha creado una nueva solicitud"));
    }

    @Test
    public void testStudentObserverNotifiedOnSolicitudStateChange() {
        // Capturar salida estándar
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        // Arrange
        StudentRequestObserver studentObserver = new StudentRequestObserver();
        DeanOffice deanOffice = new DeanOffice("D01", "Ingeniería");

        deanOffice.addObserver(studentObserver);

        Career career = new Career("Ingeniería de Sistemas", 160);
        SemaforoAcademico semaforo = new SemaforoAcademico(1, 0, career);
        Student student = new Student("Carlos", "Lopez", 987654, "Sistemas", "202302", "E02", 6, semaforo);

        Subject subject1 = SubjectCatalog.getMateria("AYSR");
        Subject subject2 = SubjectCatalog.getMateria("DOPO");
        Professor professor = new Professor("Dr. Carlos", "Martínez", 123456, "Ingeniería de Sistemas");
        Group groupOrigen = new Group(1, 1, 25, true, subject1, professor);
        Group groupDestino = new Group(2, 2, 30, true, subject2, professor);

        Enrollment enrollment = new Enrollment(groupOrigen, 1, "activa", 0.0);

        Request request = new Request(
                "555",
                groupOrigen,
                groupDestino,
                "grupo",
                "Cambio de grupo",
                new Date(),
                student.getStudentId()
        );

        // Act
        deanOffice.gestionarSolicitud(student, request);

        // Restaurar salida estándar
        System.setOut(originalOut);

        // Assert
        String output = outContent.toString().trim();
        assertTrue(output.contains("Tu solicitud ha sido modificada a"));
    }
}
