package com.example.fantasticosback;

import com.example.fantasticosback.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;


public class EstudianteTest {
    private Estudiante estudiante;
    private Materia materia1;
    private Materia materia2;
    private Profesor profesor;
    private Grupo grupo1;
    private Grupo grupo2;
    private Grupo grupoInactivo;

    @BeforeEach
    void setUp() {
        // Crear estudiante de prueba
        estudiante = new Estudiante(
            "Juan",
            "Pérez",
            12345,
            "Ingeniería de Sistemas",
            "1000087623",
            "est001",
            5
        );

        profesor = new Profesor("Ana", "García", 98765, "Ingeniería");

        materia1 = CatalogoMaterias.getMateria("AYSR"); // Arquitectura y Servicios de Red
        materia2 = CatalogoMaterias.getMateria("DOPO"); // Desarrollo Orientado Por Objetos

        grupo1 = new Grupo(1, 1, 30, true, materia1, profesor);
        grupo2 = new Grupo(2, 2, 25, true, materia2, profesor);
        grupoInactivo = new Grupo(3, 3, 30, false, materia1, profesor);

        grupo1.agregarSesion(new SesionClase("Lunes", "08:30", "10:00", "A-101"));
        grupo2.agregarSesion(new SesionClase("Martes", "10:00", "11:30", "G-206"));


        Semestre semestre = new Semestre(1, 2025, 2, true);
        estudiante.getSemestres().add(semestre);
    }

    @Test
    @DisplayName("Debería inicializar correctamente un estudiante con todos sus atributos y colecciones")
    void testConstructorYGetters() {
        assertEquals("Juan", estudiante.getNombre());
        assertEquals("Pérez", estudiante.getApellido());
        assertEquals(12345, estudiante.getDocumento());
        assertEquals("Ingeniería de Sistemas", estudiante.getCarrera());
        assertEquals("1000087623", estudiante.getCodigo());
        assertEquals("est001", estudiante.getIdEstudiante());
        assertEquals(5, estudiante.getSemestre());
        assertNotNull(estudiante.getSemestres());
        assertNotNull(estudiante.getSolicitudes());
    }

    @Test
    @DisplayName("Debería permitir cambiar el ID personalizado del estudiante")
    void testSetId() {
        estudiante.setId("nuevoId123");
        assertEquals("nuevoId123", estudiante.getIdEstudiante());
    }

    @Test
    @DisplayName("Debería agregar exitosamente una materia cuando el grupo está activo y no hay conflictos")
    void testAgregarMateriaExitoso() {
        boolean resultado = estudiante.agregarMateria(grupo1);

        assertTrue(resultado);
        assertEquals(1, estudiante.getSemestres().get(0).getMaterias().size());

        Inscripcion inscripcion = estudiante.getSemestres().get(0).getMaterias().get(0);
        assertEquals(grupo1, inscripcion.getGrupo());
        assertEquals("activa", inscripcion.getEstado());
    }

    @Test
    @DisplayName("No debería agregar una materia cuando el grupo está inactivo")
    void testAgregarMateriaGrupoInactivo() {
        boolean resultado = estudiante.agregarMateria(grupoInactivo);

        assertFalse(resultado);
        assertEquals(0, estudiante.getSemestres().get(estudiante.getSemestres().size() - 1).getMaterias().size());
    }

    @Test
    @DisplayName("No debería detectar choque de horario cuando las materias tienen horarios diferentes")
    void testVerificarChoqueHorarioSinChoque() {
        estudiante.agregarMateria(grupo1);
        boolean hayChoque = estudiante.verificarChoqueHorario(grupo2);

        assertFalse(hayChoque);
    }


    @Test
    @DisplayName("No debería agregar una materia cuando hay choque de horario con materias existentes")
    void testAgregarMateriaConChoque() {
        estudiante.agregarMateria(grupo1);

        Grupo grupoConflicto = new Grupo(99, 2, 20, true, materia2, profesor);
        grupoConflicto.agregarSesion(new SesionClase("Lunes", "07:00", "10:00", "D-307"));
        boolean resultado = estudiante.agregarMateria(grupoConflicto);

        assertFalse(resultado);
        assertEquals(1, estudiante.getSemestres().get(0).getMaterias().size());
    }

    @Test
    @DisplayName("Debería permitir agregar múltiples materias cuando no hay choques de horario")
    void testAgregarMultiplesMaterias() {
        boolean resultado1 = estudiante.agregarMateria(grupo1);
        boolean resultado2 = estudiante.agregarMateria(grupo2);

        assertTrue(resultado1);
        assertTrue(resultado2);
        assertEquals(2, estudiante.getSemestres().get(0).getMaterias().size());
    }

    @Test
    @DisplayName("Debería eliminar completamente una materia de la lista del semestre")
    void testQuitarMateria() {
        estudiante.agregarMateria(grupo1);
        Inscripcion inscripcion = estudiante.getSemestres().get(0).getMaterias().get(0);
        estudiante.quitarMateria(inscripcion);
        assertEquals(0, estudiante.getSemestres().get(0).getMaterias().size());
    }

    @Test
    @DisplayName("Debería mantener la materia en la lista pero cambiar su estado a 'cancelada'")
    void testCancelarMateria() {
        estudiante.agregarMateria(grupo1);
        Inscripcion inscripcion = estudiante.getSemestres().get(0).getMaterias().get(0);
        estudiante.cancelarMateria(inscripcion);
        assertEquals(1, estudiante.getSemestres().get(0).getMaterias().size());
        assertEquals("cancelada", inscripcion.getEstado());
    }

    @Test
    @DisplayName("Debería crear una solicitud de cambio de grupo con estado 'pendiente' y agregarla a la lista")
    void testCrearSolicitudCambioGrupo() {
        estudiante.agregarMateria(grupo1);
        Inscripcion inscripcion = estudiante.getSemestres().get(0).getMaterias().get(0);
        Grupo grupoDestino = new Grupo(5, 5, 25, true, materia1, profesor);
        grupoDestino.agregarSesion(new SesionClase("Viernes", "14:30", "16:00", "C-20"));

        Solicitud solicitud = estudiante.crearSolicitud(
            "grupo",
            inscripcion,
            grupoDestino,
            "Cambio por horario laboral"
        );

        assertNotNull(solicitud);
        assertEquals("grupo", solicitud.getTipo());
        assertEquals("pendiente", solicitud.getEstado());
        assertEquals(1, estudiante.getSolicitudes().size());
        assertEquals(solicitud, estudiante.getSolicitudes().get(0));
    }

    @Test
    @DisplayName("Debería crear una solicitud de cambio de materia con estado 'pendiente'")
    void testCrearSolicitudCambioMateria() {
        // Primero agregar una materia
        estudiante.agregarMateria(grupo1);
        Inscripcion inscripcion = estudiante.getSemestres().get(0).getMaterias().get(0);

        // Crear solicitud de cambio de materia
        Solicitud solicitud = estudiante.crearSolicitud(
            "materia",
            inscripcion,
            grupo2,
            "Cambio de carrera"
        );

        assertNotNull(solicitud);
        assertEquals("materia", solicitud.getTipo());
        assertEquals("pendiente", solicitud.getEstado());
        assertEquals(1, estudiante.getSolicitudes().size());
    }

    @Test
    @DisplayName("No debería detectar choques de horario cuando el estudiante no tiene semestres registrados")
    void testVerificarChoqueHorarioSinSemestres() {
        // Crear estudiante sin semestres
        Estudiante estudianteSinSemestres = new Estudiante(
            "Ana", "López", 54321, "Medicina", "2021002", "est002", 3
        );

        boolean hayChoque = estudianteSinSemestres.verificarChoqueHorario(grupo1);

        assertFalse(hayChoque);
    }

    @Test
    @DisplayName("No debería agregar una materia cuando el estudiante no tiene semestres activos")
    void testAgregarMateriaSinSemestre() {
        // Crear estudiante sin semestres
        Estudiante estudianteSinSemestres = new Estudiante(
            "Ana", "López", 54321, "Medicina", "2021002", "est002", 3
        );

        boolean resultado = estudianteSinSemestres.agregarMateria(grupo1);

        assertFalse(resultado);
    }

    @Test
    @DisplayName("Debería ejecutar el método mostrarInformacion sin lanzar excepciones")
    void testMostrarInformacion() {
        assertDoesNotThrow(() -> estudiante.mostrarInformacion());
    }
}
