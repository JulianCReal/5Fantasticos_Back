package com.example.fantasticosback.util;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.sql.SQLOutput;

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

        estudiante = new Estudiante(
                "María", "González", 98765, "Ingeniería de Sistemas",
                "2020001", "est001", 4
        );

        profesor = new Profesor("Dr. Carlos", "Martínez", 123456, "Ingeniería de Sistemas");

        materia1 = CatalogoMaterias.getMateria("AYSR"); // Arquitectura y Servicios de Red
        materia2 = CatalogoMaterias.getMateria("DOPO"); // Desarrollo Orientado Por Objetos

        grupoOrigen = new Grupo(1, 1, 25, true, materia1, profesor);
        grupoDestino = new Grupo(2, 2, 30, true, materia1, profesor);
        grupoInactivo = new Grupo(3, 3, 20, false, materia1, profesor);

        grupoOrigen.agregarSesion(new SesionClase("Lunes", "07:00", "08:30", "A-101"));
        grupoDestino.agregarSesion(new SesionClase("Martes", "14:30", "16:00", "H-303"));
        grupoInactivo.agregarSesion(new SesionClase("Miércoles", "10:00", "11:30", "C-203"));

        Semestre semestre = new Semestre(1, 2025, 2, true);
        estudiante.getSemestres().add(semestre);
        estudiante.agregarMateria(grupoOrigen);

        Inscripcion inscripcionOrigen = estudiante.getSemestres().get(0).getMaterias().get(0);
        solicitudCambioGrupo = estudiante.crearSolicitud("grupo", inscripcionOrigen, grupoDestino, "Cambio por horario laboral");

        grupoSinCupos = new Grupo(4, 4, 0, true, materia2, profesor);
        grupoSinCupos.agregarSesion(new SesionClase("Jueves", "16:00", "17:30", "D-307"));
        solicitudCambioMateria = estudiante.crearSolicitud("materia", inscripcionOrigen, grupoSinCupos, "Cambio de carrera");
    }

    @Test
    @DisplayName("Debería inicializar correctamente una decanatura con ID y facultad")
    void testConstructorYGetters() {
        assertEquals("DEC001", decanatura.getId());
        assertEquals("Ingeniería de Sistemas", decanatura.getFacultad());
    }

    @Test
    @DisplayName("Debería permitir cambiar el ID de la decanatura")
    void testSetId() {
        decanatura.setId("DEC002");
        assertEquals("DEC002", decanatura.getId());
    }

    @Test
    @DisplayName("Debería aprobar una solicitud de cambio de grupo cuando cumple todos los requisitos")
    void testEvaluarSolicitudCambioGrupoExitoso() {
        boolean resultado = decanatura.evaluarSolicitud(estudiante, solicitudCambioGrupo);

        assertTrue(resultado);
        assertEquals("aprobada", solicitudCambioGrupo.getEstado());
    }

    @Test
    @DisplayName("No debería aprobar una solicitud de cambio de grupo cuando el grupo destino está inactivo")
    void testEvaluarSolicitudCambioGrupoInactivo() {
        Inscripcion inscripcionOrigen = estudiante.getSemestres().get(0).getMaterias().get(0);
        Solicitud solicitudGrupoInactivo = estudiante.crearSolicitud("grupo", inscripcionOrigen, grupoInactivo, "Test grupo inactivo");

        boolean resultado = decanatura.evaluarSolicitud(estudiante, solicitudGrupoInactivo);

        assertFalse(resultado);
        assertEquals("rechazada", solicitudGrupoInactivo.getEstado());
    }

    @Test
    @DisplayName("No debería aprobar una solicitud de cambio de grupo cuando no hay cupos disponibles")
    void testEvaluarSolicitudCambioGrupoSinCupos() {

        Inscripcion inscripcionOrigen = estudiante.getSemestres().get(0).getMaterias().get(0);
        Solicitud solicitudSinCupos = estudiante.crearSolicitud("grupo", inscripcionOrigen, grupoSinCupos, "Test sin cupos");

        boolean resultado = decanatura.evaluarSolicitud(estudiante, solicitudSinCupos);

        assertFalse(resultado);
        assertEquals("rechazada", solicitudSinCupos.getEstado());
    }

    @Test
    @DisplayName("No debería aprobar una solicitud de cambio de grupo cuando hay choque de horario")
    void testEvaluarSolicitudCambioGrupoConChoque() {
        Grupo grupoConChoque = new Grupo(6, 8, 25, true, materia2, profesor);
        grupoConChoque.agregarSesion(new SesionClase("Martes", "13:00", "16:00", "F-206"));
        estudiante.agregarMateria(grupoConChoque);

        Inscripcion inscripcionOrigen = estudiante.getSemestres().get(0).getMaterias().get(0);
        Solicitud solicitudConChoque = estudiante.crearSolicitud("grupo", inscripcionOrigen, grupoDestino, "Test choque horario");

        boolean resultado = decanatura.evaluarSolicitud(estudiante, solicitudConChoque);

        assertFalse(resultado);
        assertEquals("rechazada", solicitudConChoque.getEstado());
    }

    @Test
    @DisplayName("Debería aprobar una solicitud de cambio de materia cuando cumple todos los requisitos")
    void testEvaluarSolicitudCambioMateriaExitoso() {
        // Crear grupo destino válido para cambio de materia
        Grupo grupoDestinoValido = new Grupo(7, 7, 30, true, materia2, profesor);
        grupoDestinoValido.agregarSesion(new SesionClase("Viernes", "10:00", "11:30", "G-106"));

        Inscripcion inscripcionOrigen = estudiante.getSemestres().get(0).getMaterias().get(0);
        Solicitud solicitudCambioMateriaValida = estudiante.crearSolicitud("materia", inscripcionOrigen, grupoDestinoValido, "Cambio válido");

        boolean resultado = decanatura.evaluarSolicitud(estudiante, solicitudCambioMateriaValida);

        assertTrue(resultado);
        assertEquals("aprobada", solicitudCambioMateriaValida.getEstado());
    }

    @Test
    @DisplayName("No debería aprobar una solicitud de cambio de materia cuando el grupo destino está inactivo")
    void testEvaluarSolicitudCambioMateriaGrupoInactivo() {
        Grupo grupoInactivoMateria2 = new Grupo(8, 8, 25, false, materia2, profesor);
        grupoInactivoMateria2.agregarSesion(new SesionClase("Sábado", "08:30", "10:00", "H-808"));

        Inscripcion inscripcionOrigen = estudiante.getSemestres().get(0).getMaterias().get(0);
        Solicitud solicitudMateriaInactiva = estudiante.crearSolicitud("materia", inscripcionOrigen, grupoInactivoMateria2, "Test materia inactiva");

        boolean resultado = decanatura.evaluarSolicitud(estudiante, solicitudMateriaInactiva);

        assertFalse(resultado);
        assertEquals("rechazada", solicitudMateriaInactiva.getEstado());
    }

    @Test
    @DisplayName("No debería aprobar una solicitud de cambio de materia cuando no hay cupos disponibles")
    void testEvaluarSolicitudCambioMateriaSinCupos() {
        boolean resultado = decanatura.evaluarSolicitud(estudiante, solicitudCambioMateria);

        assertFalse(resultado);
        assertEquals("rechazada", solicitudCambioMateria.getEstado());
    }

    /**
    @Test
    @DisplayName("No debería aprobar una solicitud de cambio de materia cuando hay choque de horario")
    void testEvaluarSolicitudCambioMateriaConChoque() {
        Grupo grupoChoqueMateria2 = new Grupo(9, 9, 30, true, materia2, profesor);
        grupoChoqueMateria2.agregarSesion(new SesionClase("Lunes", "07:00", "10:00", "E-203"));

        Inscripcion inscripcionOrigen = estudiante.getSemestres().get(0).getMaterias().get(0);
        Solicitud solicitudMateriaChoque = estudiante.crearSolicitud("materia", inscripcionOrigen, grupoChoqueMateria2, "Test choque materia");

        boolean resultado = decanatura.evaluarSolicitud(estudiante, solicitudMateriaChoque);

        assertFalse(resultado);
        assertEquals("rechazada", solicitudMateriaChoque.getEstado());
    }
     **/

    @Test
    @DisplayName("No debería aprobar una solicitud con tipo no reconocido")
    void testEvaluarSolicitudTipoInvalido() {
        Inscripcion inscripcionOrigen = estudiante.getSemestres().get(0).getMaterias().get(0);
        Solicitud solicitudInvalida = estudiante.crearSolicitud("tipo_invalido", inscripcionOrigen, grupoDestino, "Test tipo inválido");

        boolean resultado = decanatura.evaluarSolicitud(estudiante, solicitudInvalida);

        assertFalse(resultado);
        assertEquals("rechazada", solicitudInvalida.getEstado());
    }


    @Test
    @DisplayName("No debería detectar choque de horario cuando el estudiante no tiene semestres")
    void testVerificarChoqueHorarioEstudianteSinSemestres() {
        Estudiante estudianteSinSemestres = new Estudiante(
                "Ana", "López", 54321, "Medicina", "2022001", "est002", 1
        );

        Inscripcion inscripcionOrigen = estudiante.getSemestres().get(0).getMaterias().get(0);
        Solicitud solicitudSinSemestres = estudiante.crearSolicitud("grupo", inscripcionOrigen, grupoDestino, "Test sin semestres");

        boolean resultado = decanatura.evaluarSolicitud(estudianteSinSemestres, solicitudSinSemestres);

        assertTrue(resultado);
        assertEquals("aprobada", solicitudSinSemestres.getEstado());
    }


}