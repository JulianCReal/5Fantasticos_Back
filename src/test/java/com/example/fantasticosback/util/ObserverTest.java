package com.example.fantasticosback.util;

import com.example.fantasticosback.Model.*;
import com.example.fantasticosback.Model.Observers.DeanObserver;
import com.example.fantasticosback.Model.Observers.ObserverSolicitud;
import com.example.fantasticosback.Model.Observers.StudentObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Date;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
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
        DeanObserver deanObserver = new DeanObserver();
        Estudiante estudiante = new Estudiante("Ana", "García", 123456, "Ingeniería", "202301", "E01", 5);

        // Hacer el método addObserver público en Estudiante
        estudiante.addObserver(deanObserver);
        Materia materia1 = CatalogoMaterias.getMateria("AYSR");
        Materia materia2 = CatalogoMaterias.getMateria("DOPO");
        Profesor profesor = new Profesor("Dr. Carlos", "Martínez", 123456, "Ingeniería de Sistemas");

        Grupo grupoOrigen = new Grupo(1, 1, 25, true, materia1, profesor);
        Grupo grupoDestino = new Grupo(2, 2, 30, true, materia2, profesor);
        Inscripcion inscripcionActual = new Inscripcion(grupoOrigen, 1, "cursando", 0.0);

        // Act
        estudiante.crearSolicitud("grupo", inscripcionActual, grupoDestino, "Cambio por choque de horario");

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
        StudentObserver studentObserver = new StudentObserver();
        Decanatura decanatura = new Decanatura("D01", "Ingeniería");

        decanatura.addObserver(studentObserver);

        Estudiante estudiante = new Estudiante("Carlos", "Lopez", 987654, "Sistemas", "202302", "E02", 6);

        Materia materia1 = CatalogoMaterias.getMateria("AYSR");
        Materia materia2 = CatalogoMaterias.getMateria("DOPO");
        Profesor profesor = new Profesor("Dr. Carlos", "Martínez", 123456, "Ingeniería de Sistemas");
        Grupo grupoOrigen = new Grupo(1, 1, 25, true, materia1, profesor);
        Grupo grupoDestino = new Grupo(2, 2, 30, true, materia2, profesor);

        Inscripcion inscripcion = new Inscripcion(grupoOrigen, 1, "activa", 0.0);

        Solicitud solicitud = new Solicitud(
                555,
                grupoOrigen,
                grupoDestino,
                "grupo",
                "Cambio de grupo",
                new Date()
        );

        // Act
        decanatura.gestionarSolicitud(estudiante, solicitud);

        // Restaurar salida estándar
        System.setOut(originalOut);

        // Assert
        String output = outContent.toString().trim();
        assertTrue(output.contains("Tu solicitud ha sido modificada a"));
    }
}
