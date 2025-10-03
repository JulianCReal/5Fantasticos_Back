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
    private Professor professor;
    private Group groupOrigen;
    private Group groupDestino;
    private Group groupInactivo;
    private Group groupSinCupos;
    private Request requestCambioGrupo;
    private Request requestCambioMateria;

    @BeforeEach
    void setUp() {
        deanOffice = new DeanOffice("DEC001", "Ingeniería de Sistemas");
        Career career = new Career("Ingeniería de Sistemas", 160);
        SemaforoAcademico semaforo = new SemaforoAcademico(1, 0, career);

        student = new Student(
                "María", "González", 98765, "Ingeniería de Sistemas",
                "2020001", "est001", 4, semaforo
        );


        deanOffice.addEstudiante(student);

        professor = new Professor("Dr. Carlos", "Martínez", 123456, "Ingeniería de Sistemas");

        subject1 = SubjectCatalog.getMateria("AYSR");
        subject2 = SubjectCatalog.getMateria("DOPO");

        groupOrigen = new Group(1, 1, 25, true, subject1, professor);
        groupDestino = new Group(2, 2, 30, true, subject1, professor);
        groupInactivo = new Group(3, 3, 20, false, subject1, professor);
        groupSinCupos = new Group(4, 4, 0, true, subject2, professor);

        groupOrigen.agregarSesion(new ClassSession("Lunes", "07:00", "08:30", "A-101"));
        groupDestino.agregarSesion(new ClassSession("Martes", "14:30", "16:00", "H-303"));
        groupInactivo.agregarSesion(new ClassSession("Miércoles", "10:00", "11:30", "C-203"));
        groupSinCupos.agregarSesion(new ClassSession("Jueves", "16:00", "17:30", "D-307"));

        Semester semester = new Semester(1, 2025, 2, true);
        student.getSemestres().add(semester);
        student.agregarMateria(groupOrigen);

        Enrollment enrollmentOrigen = student.getSemestres().get(0).getSubjects().get(0);
        requestCambioGrupo = student.crearSolicitud("grupo", enrollmentOrigen, groupDestino, "Cambio por horario laboral");
        requestCambioMateria = student.crearSolicitud("materia", enrollmentOrigen, groupSinCupos, "Cambio de carrera");
    }

    @Test
    @DisplayName("Solicitud de cambio de grupo válida debería aprobarse")
    void testCambioGrupoExitoso() {
        deanOffice.gestionarSolicitud(student, requestCambioGrupo);

        assertEquals("Aceptada", requestCambioGrupo.getState().getStateName());
    }

    @Test
    @DisplayName("Rechaza cambio de grupo si grupo está inactivo")
    void testCambioGrupoGrupoInactivo() {
        Request request = student.crearSolicitud("grupo",
                student.getSemestres().get(0).getSubjects().get(0), groupInactivo, "Destino inactivo");

        deanOffice.gestionarSolicitud(student, request);

        assertEquals("Rechazada", request.getState().getStateName());
    }

    @Test
    @DisplayName("Rechaza cambio de grupo si no hay cupos")
    void testCambioGrupoSinCupos() {
        Request request = student.crearSolicitud("grupo",
                student.getSemestres().get(0).getSubjects().get(0), groupSinCupos, "Sin cupos");

        deanOffice.gestionarSolicitud(student, request);

        assertEquals("Rechazada", request.getState().getStateName());
    }

    @Test
    @DisplayName("Rechaza si hay choque de horario")
    void testCambioGrupoConChoque() {
        Group groupConChoque = new Group(6, 8, 25, true, subject2, professor);
        groupConChoque.agregarSesion(new ClassSession("Martes", "13:00", "16:00", "F-206"));
        student.agregarMateria(groupConChoque);

        Request request = student.crearSolicitud("grupo",
                student.getSemestres().get(0).getSubjects().get(0), groupDestino, "Choque horario");

        deanOffice.gestionarSolicitud(student, request);

        assertEquals("Rechazada", request.getState().getStateName());
    }

    @Test
    @DisplayName("Cambio de materia válido se aprueba")
    void testCambioMateriaExitoso() {
        Group groupDestinoValido = new Group(7, 7, 30, true, subject2, professor);
        groupDestinoValido.agregarSesion(new ClassSession("Viernes", "10:00", "11:30", "G-106"));

        Request request = student.crearSolicitud("materia",
                student.getSemestres().get(0).getSubjects().get(0), groupDestinoValido, "Cambio válido");

        deanOffice.gestionarSolicitud(student, request);

        assertEquals("Aceptada", request.getState().getStateName());
    }

    @Test
    @DisplayName("Rechaza cambio de materia si grupo destino está inactivo")
    void testCambioMateriaGrupoInactivo() {
        Group groupInactivoMateria2 = new Group(8, 8, 25, false, subject2, professor);
        groupInactivoMateria2.agregarSesion(new ClassSession("Sábado", "08:30", "10:00", "H-808"));

        Request request = student.crearSolicitud("materia",
                student.getSemestres().get(0).getSubjects().get(0), groupInactivoMateria2, "Inactivo");

        deanOffice.gestionarSolicitud(student, request);

        assertEquals("Rechazada", request.getState().getStateName());
    }

    @Test
    @DisplayName("Rechaza cambio de materia si no hay cupos")
    void testCambioMateriaSinCupos() {
        deanOffice.gestionarSolicitud(student, requestCambioMateria);

        assertEquals("Rechazada", requestCambioMateria.getState().getStateName());
    }

    @Test
    @DisplayName("Tipo de solicitud inválido debería ser rechazada")
    void testTipoInvalido() {
        Request request = student.crearSolicitud("otro",
                student.getSemestres().get(0).getSubjects().get(0), groupDestino, "Tipo inválido");

        deanOffice.gestionarSolicitud(student, request);

        assertEquals("Rechazada", request.getState().getStateName());
    }

    @Test
    @DisplayName("No hay choque si el estudiante no tiene semestres")
    void testSinSemestres() {
        Career careerTemp = new Career("Medicina", 200);
        SemaforoAcademico semaforoTemp = new SemaforoAcademico(1, 0, careerTemp);
        Student nuevo = new Student("Ana", "López", 54321, "Medicina", "2022001", "est002", 1, semaforoTemp);

        Enrollment enrollmentOrigen = student.getSemestres().get(0).getSubjects().get(0);
        Request request = nuevo.crearSolicitud("grupo", enrollmentOrigen, groupDestino, "Sin semestres");

        deanOffice.gestionarSolicitud(nuevo, request);

        assertEquals("Aceptada", request.getState().getStateName());
    }

    @Test
    @DisplayName("Lista todas las solicitudes de la facultad")
    void testListarSolicitudes() {
        Career careerTemp = new Career("Ingeniería de Sistemas", 160);
        SemaforoAcademico semaforoTemp = new SemaforoAcademico(1, 0, careerTemp);
        Student student1 = new Student(
                "Luis", "Ramírez", 11223, "Ingeniería de Sistemas",
                "2021002", "est003", 3, semaforoTemp
        );
        deanOffice.addEstudiante(student1);
        student1.getSemestres().add(new Semester(1, 2025, 2, true));
        student1.agregarMateria(groupOrigen);
        Enrollment enrollment1 = student1.getSemestres().get(0).getSubjects().get(0);
         student1.crearSolicitud("grupo", enrollment1, groupDestino, "Cambio por horario");
         assertEquals(3, deanOffice.getSolicitudesPorFacultad().size());
    }
}
