package com.example.fantasticosback.util;
import com.example.fantasticosback.Model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

public class SemaforoAcademicoTest {
    private SemaforoAcademico semaforo;
    private Semester semester1;

    @BeforeEach
    public void setUp() {
        Career career = new Career("Ingeniería de Sistemas", 160);
        semaforo = new SemaforoAcademico(1, 0, career);
        createData();
    }

    public void createData() {
        semester1 = new Semester(1, 2024, 2, false);
        Group group1 = new Group(1, 1, 30, true, SubjectCatalog.getMateria("AYSR"), new Professor("Dr. Carlos", "Martínez", 123456, "Ingeniería de Sistemas"));
        Enrollment enrollment1 = new Enrollment(group1, 1, "aprobada", 4.0);
        Group group2 = new Group(2, 1, 30, true, SubjectCatalog.getMateria("DOPO"), new Professor("Dra. Ana", "López", 654321, "Ingeniería de Sistemas"));
        Enrollment enrollment2 = new Enrollment(group2, 1, "aprobada", 3.5);
        Group group3 = new Group(3, 1, 30, true, SubjectCatalog.getMateria("CALD"), new Professor("Dr. Juan",  "Pérez", 112233, "Matemáticas"));
        Enrollment enrollment3 = new Enrollment(group3, 1, "reprobada", 2.7);
        semester1.agregarMateria(enrollment1);
        semester1.agregarMateria(enrollment2);
        semester1.agregarMateria(enrollment3);
    }

    @Test
    @DisplayName("Debería crear semáforo académico correctamente")
    void testCrearSemaforoAcademico() {
        assertNotNull(semaforo);
        assertEquals(1, semaforo.getId());
        assertEquals(0, semaforo.getAvancePorcentaje());
        assertEquals(0.0, semaforo.getPromedioAcumulado());
    }

    @Test
    @DisplayName("Debería agregar semestre al semáforo académico")
    void testAgregarSemestre() {
        semaforo.agregarSemestre(semester1);
        assertEquals(1, semaforo.getSemestres().size());
        assertEquals(semester1, semaforo.getSemestres().get(0));
    }

    @Test
    @DisplayName("Debería actualizar avance académico y calcular promedio acumulado")
    void testActualizarAvanceYCalcularPromedio() {
        semaforo.agregarSemestre(semester1);
        semaforo.actualizarAvance(semester1.getSubjects());
        assertEquals(5, semaforo.getAvancePorcentaje());
        assertEquals(3.5, semaforo.getPromedioAcumulado(), 0.001);

    }
}

