package com.example.fantasticosback.util;

import com.example.fantasticosback.Model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

public class DecanaturaTest {
    private Decanatura decanatura;
    private Estudiante estudiante;
    private Materia materia1;
    private Materia materia2;
    private Profesor profesor;
    private Grupo grupoOrigen;
    private Grupo grupoDestino;
    private Grupo grupoInactivo;
    private Grupo grupoSinCupos;
    private Solicitud solicitudCambioGrupo;
    private Solicitud solicitudCambioMateria;

    @BeforeEach
    void setUp() {
        decanatura = new Decanatura("DEC001", "Ingeniería de Sistemas");
        Carrera carrera = new Carrera("Ingeniería de Sistemas", 160);
        SemaforoAcademico semaforo = new SemaforoAcademico(1, 0, carrera);

        estudiante = new Estudiante(
                "María", "González", 98765, "Ingeniería de Sistemas",
                "2020001", "est001", 4, semaforo
        );


        decanatura.addEstudiante(estudiante);

        profesor = new Profesor("Dr. Carlos", "Martínez", 123456, "Ingeniería de Sistemas");

        materia1 = CatalogoMaterias.getMateria("AYSR");
        materia2 = CatalogoMaterias.getMateria("DOPO");

        grupoOrigen = new Grupo(1, 1, 25, true, materia1, profesor);
        grupoDestino = new Grupo(2, 2, 30, true, materia1, profesor);
        grupoInactivo = new Grupo(3, 3, 20, false, materia1, profesor);
        grupoSinCupos = new Grupo(4, 4, 0, true, materia2, profesor);

        grupoOrigen.agregarSesion(new SesionClase("Lunes", "07:00", "08:30", "A-101"));
        grupoDestino.agregarSesion(new SesionClase("Martes", "14:30", "16:00", "H-303"));
        grupoInactivo.agregarSesion(new SesionClase("Miércoles", "10:00", "11:30", "C-203"));
        grupoSinCupos.agregarSesion(new SesionClase("Jueves", "16:00", "17:30", "D-307"));

        Semestre semestre = new Semestre(1, 2025, 2, true);
        estudiante.getSemestres().add(semestre);
        estudiante.agregarMateria(grupoOrigen);

        Inscripcion inscripcionOrigen = estudiante.getSemestres().get(0).getMaterias().get(0);
        solicitudCambioGrupo = estudiante.crearSolicitud("grupo", inscripcionOrigen, grupoDestino, "Cambio por horario laboral");
        solicitudCambioMateria = estudiante.crearSolicitud("materia", inscripcionOrigen, grupoSinCupos, "Cambio de carrera");
    }

    @Test
    @DisplayName("Solicitud de cambio de grupo válida debería aprobarse")
    void testCambioGrupoExitoso() {
        decanatura.gestionarSolicitud(estudiante, solicitudCambioGrupo);

        assertEquals("Aceptada", solicitudCambioGrupo.getEstado().getNombreEstado());
    }

    @Test
    @DisplayName("Rechaza cambio de grupo si grupo está inactivo")
    void testCambioGrupoGrupoInactivo() {
        Solicitud solicitud = estudiante.crearSolicitud("grupo",
                estudiante.getSemestres().get(0).getMaterias().get(0), grupoInactivo, "Destino inactivo");

        decanatura.gestionarSolicitud(estudiante, solicitud);

        assertEquals("Rechazada", solicitud.getEstado().getNombreEstado());
    }

    @Test
    @DisplayName("Rechaza cambio de grupo si no hay cupos")
    void testCambioGrupoSinCupos() {
        Solicitud solicitud = estudiante.crearSolicitud("grupo",
                estudiante.getSemestres().get(0).getMaterias().get(0), grupoSinCupos, "Sin cupos");

        decanatura.gestionarSolicitud(estudiante, solicitud);

        assertEquals("Rechazada", solicitud.getEstado().getNombreEstado());
    }

    @Test
    @DisplayName("Rechaza si hay choque de horario")
    void testCambioGrupoConChoque() {
        Grupo grupoConChoque = new Grupo(6, 8, 25, true, materia2, profesor);
        grupoConChoque.agregarSesion(new SesionClase("Martes", "13:00", "16:00", "F-206"));
        estudiante.agregarMateria(grupoConChoque);

        Solicitud solicitud = estudiante.crearSolicitud("grupo",
                estudiante.getSemestres().get(0).getMaterias().get(0), grupoDestino, "Choque horario");

        decanatura.gestionarSolicitud(estudiante, solicitud);

        assertEquals("Rechazada", solicitud.getEstado().getNombreEstado());
    }

    @Test
    @DisplayName("Cambio de materia válido se aprueba")
    void testCambioMateriaExitoso() {
        Grupo grupoDestinoValido = new Grupo(7, 7, 30, true, materia2, profesor);
        grupoDestinoValido.agregarSesion(new SesionClase("Viernes", "10:00", "11:30", "G-106"));

        Solicitud solicitud = estudiante.crearSolicitud("materia",
                estudiante.getSemestres().get(0).getMaterias().get(0), grupoDestinoValido, "Cambio válido");

        decanatura.gestionarSolicitud(estudiante, solicitud);

        assertEquals("Aceptada", solicitud.getEstado().getNombreEstado());
    }

    @Test
    @DisplayName("Rechaza cambio de materia si grupo destino está inactivo")
    void testCambioMateriaGrupoInactivo() {
        Grupo grupoInactivoMateria2 = new Grupo(8, 8, 25, false, materia2, profesor);
        grupoInactivoMateria2.agregarSesion(new SesionClase("Sábado", "08:30", "10:00", "H-808"));

        Solicitud solicitud = estudiante.crearSolicitud("materia",
                estudiante.getSemestres().get(0).getMaterias().get(0), grupoInactivoMateria2, "Inactivo");

        decanatura.gestionarSolicitud(estudiante, solicitud);

        assertEquals("Rechazada", solicitud.getEstado().getNombreEstado());
    }

    @Test
    @DisplayName("Rechaza cambio de materia si no hay cupos")
    void testCambioMateriaSinCupos() {
        decanatura.gestionarSolicitud(estudiante, solicitudCambioMateria);

        assertEquals("Rechazada", solicitudCambioMateria.getEstado().getNombreEstado());
    }

    @Test
    @DisplayName("Tipo de solicitud inválido debería ser rechazada")
    void testTipoInvalido() {
        Solicitud solicitud = estudiante.crearSolicitud("otro",
                estudiante.getSemestres().get(0).getMaterias().get(0), grupoDestino, "Tipo inválido");

        decanatura.gestionarSolicitud(estudiante, solicitud);

        assertEquals("Rechazada", solicitud.getEstado().getNombreEstado());
    }

    @Test
    @DisplayName("No hay choque si el estudiante no tiene semestres")
    void testSinSemestres() {
        Carrera carrera = new Carrera("Ingeniería de Sistemas", 160);
        SemaforoAcademico semaforo = new SemaforoAcademico(2, 0, carrera);
        Estudiante nuevo = new Estudiante("Ana", "López", 54321, "Medicina", "2022001", "est002", 1, semaforo);

        Inscripcion inscripcionOrigen = estudiante.getSemestres().get(0).getMaterias().get(0);
        Solicitud solicitud = nuevo.crearSolicitud("grupo", inscripcionOrigen, grupoDestino, "Sin semestres");

        decanatura.gestionarSolicitud(nuevo, solicitud);

        assertEquals("Aceptada", solicitud.getEstado().getNombreEstado());
    }

    @Test
    @DisplayName("Lista todas las solicitudes de la facultad")
    void testListarSolicitudes() {
        Estudiante estudiante1 = new Estudiante(
                "Luis", "Ramírez", 11223, "Ingeniería de Sistemas",
                "2021002", "est003", 3, new SemaforoAcademico(3, 0, new Carrera("Ingeniería de Sistemas", 160))
        );
        decanatura.addEstudiante(estudiante1);
        estudiante1.getSemestres().add(new Semestre(1, 2025, 2, true));
        estudiante1.agregarMateria(grupoOrigen);
        Inscripcion inscripcion1 = estudiante1.getSemestres().get(0).getMaterias().get(0);
         estudiante1.crearSolicitud("grupo", inscripcion1, grupoDestino, "Cambio por horario");
         assertEquals(3, decanatura.getSolicitudesPorFacultad().size());
    }
}
