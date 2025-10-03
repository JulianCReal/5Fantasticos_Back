package com.example.fantasticosback.util;

import com.example.fantasticosback.Model.Group;
import com.example.fantasticosback.Model.Enrollment;
import com.example.fantasticosback.Model.Subject;
import com.example.fantasticosback.Model.Professor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EnrollmentTest {

    private Enrollment enrollment;
    private Group group;
    private Group otroGroup;
    private Subject subject;
    private Professor professor;
    private ClassSession sesion1;
    private ClassSession sesion2;
    private ClassSession sesion3;
    private ClassSession sesion4;

    @BeforeEach
    void setUp() {
        subject = new Subject(1, "Cálculo I", 4, 1);
        professor = new Professor("Juan", "Pérez", 12345678, "Matemáticas");

        group = new Group(1, 101, 30, true, subject, professor);
        sesion1 = new ClassSession("Lunes", "08:00", "10:00", "A101");
        sesion2 = new ClassSession("Miércoles", "10:00", "12:00", "A102");
        group.agregarSesion(sesion1);
        group.agregarSesion(sesion2);

        otroGroup = new Group(2, 102, 25, true, subject, professor);
        sesion3 = new ClassSession("Martes", "14:00", "16:00", "B201");
        sesion4 = new ClassSession("Jueves", "08:00", "10:00", "B202");
        otroGroup.agregarSesion(sesion3);
        otroGroup.agregarSesion(sesion4);

        enrollment = new Enrollment(group, 1, "inscrita", 0.0);
    }

    @Test
    void testConstructor() {
        assertEquals(group, enrollment.getGrupo());
        assertEquals(1, enrollment.getId());
        assertEquals("inscrita", enrollment.getState());
        assertEquals(0.0, enrollment.getFinalGrade(), 0.001);
    }

    @Test
    void testConstructorConParametrosVariados() {
        Enrollment enrollment2 = new Enrollment(otroGroup, 999, "aprobada", 45.5);

        assertEquals(otroGroup, enrollment2.getGrupo());
        assertEquals(999, enrollment2.getId());
        assertEquals("aprobada", enrollment2.getState());
        assertEquals(45.5, enrollment2.getFinalGrade(), 0.001);
    }

    @Test
    void testConstructorConGrupoNulo() {
        Enrollment enrollmentNula = new Enrollment(null, 2, "pendiente", 25.0);

        assertNull(enrollmentNula.getGrupo());
        assertEquals(2, enrollmentNula.getId());
        assertEquals("pendiente", enrollmentNula.getState());
        assertEquals(25.0, enrollmentNula.getFinalGrade(), 0.001);
    }

    @Test
    void testGetters() {
        assertEquals(group, enrollment.getGrupo());
        assertEquals(1, enrollment.getId());
        assertEquals("inscrita", enrollment.getState());
        assertEquals(0.0, enrollment.getFinalGrade(), 0.001);
    }

    @Test
    void testSetId() {
        // Test del setter de ID
        enrollment.setId(999);
        assertEquals(999, enrollment.getId());

        enrollment.setId(0);
        assertEquals(0, enrollment.getId());

        enrollment.setId(-5);
        assertEquals(-5, enrollment.getId());
    }

    @Test
    void testCancelar() {
        // Verificar que cancelar cambia el estado a "cancelada"
        enrollment.cancelar();
        assertEquals("cancelada", enrollment.getState());

        // Verificar que cancelar desde otro estado también funciona
        Enrollment enrollmentAprobada = new Enrollment(group, 2, "aprobada", 40.0);
        enrollmentAprobada.cancelar();
        assertEquals("cancelada", enrollmentAprobada.getState());
    }

    @Test
    void testEvaluarAprobada() {
        Enrollment enrollmentAprobada = new Enrollment(group, 2, "inscrita", 30.0);
        enrollmentAprobada.evaluar();
        assertEquals("aprobada", enrollmentAprobada.getState());

        Enrollment enrollmentNotaAlta = new Enrollment(group, 3, "inscrita", 45.5);
        enrollmentNotaAlta.evaluar();
        assertEquals("aprobada", enrollmentNotaAlta.getState());

        Enrollment enrollmentLimite = new Enrollment(group, 4, "inscrita", 30.0);
        enrollmentLimite.evaluar();
        assertEquals("aprobada", enrollmentLimite.getState());
    }

    @Test
    void testEvaluarReprobada() {
        Enrollment enrollmentReprobada = new Enrollment(group, 2, "inscrita", 29.9);
        enrollmentReprobada.evaluar();
        assertEquals("reprobada", enrollmentReprobada.getState());

        Enrollment enrollmentNotaBaja = new Enrollment(group, 3, "inscrita", 0.0);
        enrollmentNotaBaja.evaluar();
        assertEquals("reprobada", enrollmentNotaBaja.getState());

        Enrollment enrollmentNotaNegativa = new Enrollment(group, 4, "inscrita", -5.0);
        enrollmentNotaNegativa.evaluar();
        assertEquals("reprobada", enrollmentNotaNegativa.getState());
    }

    @Test
    void testCambiarGrupo() {
        assertEquals(group, enrollment.getGrupo());

        enrollment.cambiarGrupo(otroGroup);
        assertEquals(otroGroup, enrollment.getGrupo());

        enrollment.cambiarGrupo(null);
        assertNull(enrollment.getGrupo());
    }

    @Test
    void testValidarChoqueNoHayChoque() {
        // Crear inscripción con horarios que no chocan
        Enrollment otraEnrollment = new Enrollment(otroGroup, 2, "inscrita", 0.0);

        // Los horarios son diferentes: Lunes 08:00-10:00, Miércoles 10:00-12:00 vs Martes 14:00-16:00, Jueves 08:00-10:00
        assertFalse(enrollment.validarChoque(otraEnrollment));
        assertFalse(otraEnrollment.validarChoque(enrollment));
    }

    @Test
    void testValidarChoqueHayChoque() {
        // Crear grupo con horario que choca
        Group groupChoque = new Group(3, 103, 20, true, subject, professor);
        ClassSession sesionChoque = new ClassSession("Lunes", "08:00", "09:00", "C301");
        groupChoque.agregarSesion(sesionChoque);

        Enrollment enrollmentChoque = new Enrollment(groupChoque, 3, "inscrita", 0.0);

        // Debe haber choque porque ambas tienen sesión el Lunes a las 08:00
        assertTrue(enrollment.validarChoque(enrollmentChoque));
        assertTrue(enrollmentChoque.validarChoque(enrollment));
    }

    @Test
    void testValidarChoqueHorarioFinalIgual() {
        Group groupChoqueFinal = new Group(4, 104, 15, true, subject, professor);
        ClassSession sesionChoqueFinal = new ClassSession("Lunes", "09:00", "10:00", "D401");
        groupChoqueFinal.agregarSesion(sesionChoqueFinal);

        Enrollment enrollmentChoqueFinal = new Enrollment(groupChoqueFinal, 4, "inscrita", 0.0);

        assertTrue(enrollment.validarChoque(enrollmentChoqueFinal));
        assertTrue(enrollmentChoqueFinal.validarChoque(enrollment));
    }

    @Test
    void testValidarChoqueGrupoSinSesiones() {
        // Crear grupo sin sesiones
        Group groupVacio = new Group(5, 105, 10, true, subject, professor);
        Enrollment enrollmentVacia = new Enrollment(groupVacio, 5, "inscrita", 0.0);

        // No debe haber choque si uno de los grupos no tiene sesiones
        assertFalse(enrollment.validarChoque(enrollmentVacia));
        assertFalse(enrollmentVacia.validarChoque(enrollment));
    }

    @Test
    void testValidarChoqueAmbosGruposSinSesiones() {
        Group groupVacio1 = new Group(6, 106, 10, true, subject, professor);
        Group groupVacio2 = new Group(7, 107, 10, true, subject, professor);

        Enrollment enrollmentVacia1 = new Enrollment(groupVacio1, 6, "inscrita", 0.0);
        Enrollment enrollmentVacia2 = new Enrollment(groupVacio2, 7, "inscrita", 0.0);

        assertFalse(enrollmentVacia1.validarChoque(enrollmentVacia2));
        assertFalse(enrollmentVacia2.validarChoque(enrollmentVacia1));
    }

}
