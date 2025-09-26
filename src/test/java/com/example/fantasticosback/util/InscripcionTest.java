package com.example.fantasticosback.util;

import com.example.fantasticosback.Model.Grupo;
import com.example.fantasticosback.Model.Inscripcion;
import com.example.fantasticosback.Model.Materia;
import com.example.fantasticosback.Model.Profesor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class InscripcionTest {

    private Inscripcion inscripcion;
    private Grupo grupo;
    private Grupo otroGrupo;
    private Materia materia;
    private Profesor profesor;
    private SesionClase sesion1;
    private SesionClase sesion2;
    private SesionClase sesion3;
    private SesionClase sesion4;

    @BeforeEach
    void setUp() {
        materia = new Materia(1, "Cálculo I", 4, 1);
        profesor = new Profesor("Juan", "Pérez", 12345678, "Matemáticas");

        grupo = new Grupo(1, 101, 30, true, materia, profesor);
        sesion1 = new SesionClase("Lunes", "08:00", "10:00", "A101");
        sesion2 = new SesionClase("Miércoles", "10:00", "12:00", "A102");
        grupo.agregarSesion(sesion1);
        grupo.agregarSesion(sesion2);

        otroGrupo = new Grupo(2, 102, 25, true, materia, profesor);
        sesion3 = new SesionClase("Martes", "14:00", "16:00", "B201");
        sesion4 = new SesionClase("Jueves", "08:00", "10:00", "B202");
        otroGrupo.agregarSesion(sesion3);
        otroGrupo.agregarSesion(sesion4);

        inscripcion = new Inscripcion(grupo, 1, "inscrita", 0.0);
    }

    @Test
    void testConstructor() {
        assertEquals(grupo, inscripcion.getGrupo());
        assertEquals(1, inscripcion.getId());
        assertEquals("inscrita", inscripcion.getEstado());
        assertEquals(0.0, inscripcion.getNotaFinal(), 0.001);
    }

    @Test
    void testConstructorConParametrosVariados() {
        Inscripcion inscripcion2 = new Inscripcion(otroGrupo, 999, "aprobada", 45.5);

        assertEquals(otroGrupo, inscripcion2.getGrupo());
        assertEquals(999, inscripcion2.getId());
        assertEquals("aprobada", inscripcion2.getEstado());
        assertEquals(45.5, inscripcion2.getNotaFinal(), 0.001);
    }

    @Test
    void testConstructorConGrupoNulo() {
        Inscripcion inscripcionNula = new Inscripcion(null, 2, "pendiente", 25.0);

        assertNull(inscripcionNula.getGrupo());
        assertEquals(2, inscripcionNula.getId());
        assertEquals("pendiente", inscripcionNula.getEstado());
        assertEquals(25.0, inscripcionNula.getNotaFinal(), 0.001);
    }

    @Test
    void testGetters() {
        assertEquals(grupo, inscripcion.getGrupo());
        assertEquals(1, inscripcion.getId());
        assertEquals("inscrita", inscripcion.getEstado());
        assertEquals(0.0, inscripcion.getNotaFinal(), 0.001);
    }

    @Test
    void testSetId() {
        // Test del setter de ID
        inscripcion.setId(999);
        assertEquals(999, inscripcion.getId());

        inscripcion.setId(0);
        assertEquals(0, inscripcion.getId());

        inscripcion.setId(-5);
        assertEquals(-5, inscripcion.getId());
    }

    @Test
    void testCancelar() {
        // Verificar que cancelar cambia el estado a "cancelada"
        inscripcion.cancelar();
        assertEquals("cancelada", inscripcion.getEstado());

        // Verificar que cancelar desde otro estado también funciona
        Inscripcion inscripcionAprobada = new Inscripcion(grupo, 2, "aprobada", 40.0);
        inscripcionAprobada.cancelar();
        assertEquals("cancelada", inscripcionAprobada.getEstado());
    }

    @Test
    void testEvaluarAprobada() {
        Inscripcion inscripcionAprobada = new Inscripcion(grupo, 2, "inscrita", 30.0);
        inscripcionAprobada.evaluar();
        assertEquals("aprobada", inscripcionAprobada.getEstado());

        Inscripcion inscripcionNotaAlta = new Inscripcion(grupo, 3, "inscrita", 45.5);
        inscripcionNotaAlta.evaluar();
        assertEquals("aprobada", inscripcionNotaAlta.getEstado());

        Inscripcion inscripcionLimite = new Inscripcion(grupo, 4, "inscrita", 30.0);
        inscripcionLimite.evaluar();
        assertEquals("aprobada", inscripcionLimite.getEstado());
    }

    @Test
    void testEvaluarReprobada() {
        Inscripcion inscripcionReprobada = new Inscripcion(grupo, 2, "inscrita", 29.9);
        inscripcionReprobada.evaluar();
        assertEquals("reprobada", inscripcionReprobada.getEstado());

        Inscripcion inscripcionNotaBaja = new Inscripcion(grupo, 3, "inscrita", 0.0);
        inscripcionNotaBaja.evaluar();
        assertEquals("reprobada", inscripcionNotaBaja.getEstado());

        Inscripcion inscripcionNotaNegativa = new Inscripcion(grupo, 4, "inscrita", -5.0);
        inscripcionNotaNegativa.evaluar();
        assertEquals("reprobada", inscripcionNotaNegativa.getEstado());
    }

    @Test
    void testCambiarGrupo() {
        assertEquals(grupo, inscripcion.getGrupo());

        inscripcion.cambiarGrupo(otroGrupo);
        assertEquals(otroGrupo, inscripcion.getGrupo());

        inscripcion.cambiarGrupo(null);
        assertNull(inscripcion.getGrupo());
    }

    @Test
    void testValidarChoqueNoHayChoque() {
        // Crear inscripción con horarios que no chocan
        Inscripcion otraInscripcion = new Inscripcion(otroGrupo, 2, "inscrita", 0.0);

        // Los horarios son diferentes: Lunes 08:00-10:00, Miércoles 10:00-12:00 vs Martes 14:00-16:00, Jueves 08:00-10:00
        assertFalse(inscripcion.validarChoque(otraInscripcion));
        assertFalse(otraInscripcion.validarChoque(inscripcion));
    }

    @Test
    void testValidarChoqueHayChoque() {
        // Crear grupo con horario que choca
        Grupo grupoChoque = new Grupo(3, 103, 20, true, materia, profesor);
        SesionClase sesionChoque = new SesionClase("Lunes", "08:00", "09:00", "C301");
        grupoChoque.agregarSesion(sesionChoque);

        Inscripcion inscripcionChoque = new Inscripcion(grupoChoque, 3, "inscrita", 0.0);

        // Debe haber choque porque ambas tienen sesión el Lunes a las 08:00
        assertTrue(inscripcion.validarChoque(inscripcionChoque));
        assertTrue(inscripcionChoque.validarChoque(inscripcion));
    }

    @Test
    void testValidarChoqueHorarioFinalIgual() {
        Grupo grupoChoqueFinal = new Grupo(4, 104, 15, true, materia, profesor);
        SesionClase sesionChoqueFinal = new SesionClase("Lunes", "09:00", "10:00", "D401");
        grupoChoqueFinal.agregarSesion(sesionChoqueFinal);

        Inscripcion inscripcionChoqueFinal = new Inscripcion(grupoChoqueFinal, 4, "inscrita", 0.0);

        assertTrue(inscripcion.validarChoque(inscripcionChoqueFinal));
        assertTrue(inscripcionChoqueFinal.validarChoque(inscripcion));
    }

    @Test
    void testValidarChoqueGrupoSinSesiones() {
        // Crear grupo sin sesiones
        Grupo grupoVacio = new Grupo(5, 105, 10, true, materia, profesor);
        Inscripcion inscripcionVacia = new Inscripcion(grupoVacio, 5, "inscrita", 0.0);

        // No debe haber choque si uno de los grupos no tiene sesiones
        assertFalse(inscripcion.validarChoque(inscripcionVacia));
        assertFalse(inscripcionVacia.validarChoque(inscripcion));
    }

    @Test
    void testValidarChoqueAmbosGruposSinSesiones() {
        Grupo grupoVacio1 = new Grupo(6, 106, 10, true, materia, profesor);
        Grupo grupoVacio2 = new Grupo(7, 107, 10, true, materia, profesor);

        Inscripcion inscripcionVacia1 = new Inscripcion(grupoVacio1, 6, "inscrita", 0.0);
        Inscripcion inscripcionVacia2 = new Inscripcion(grupoVacio2, 7, "inscrita", 0.0);

        assertFalse(inscripcionVacia1.validarChoque(inscripcionVacia2));
        assertFalse(inscripcionVacia2.validarChoque(inscripcionVacia1));
    }

}
