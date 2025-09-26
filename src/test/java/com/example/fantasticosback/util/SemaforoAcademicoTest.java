package com.example.fantasticosback.util;
import com.example.fantasticosback.Model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

public class SemaforoAcademicoTest {
    private SemaforoAcademico semaforo;
    private Semestre semestre1;

    @BeforeEach
    public void setUp() {
        semaforo = new SemaforoAcademico(1, 0, 160);
        createData();
    }

    public void createData() {
        semestre1 = new Semestre(1, 2024, 2, false);
        Grupo grupo1 = new Grupo(1, 1, 30, true, CatalogoMaterias.getMateria("AYSR"), new Profesor("Dr. Carlos", "Martínez", 123456, "Ingeniería de Sistemas"));
        Inscripcion inscripcion1 = new Inscripcion(grupo1, 1, "aprobada", 4.0);
        Grupo grupo2 = new Grupo(2, 1, 30, true, CatalogoMaterias.getMateria("DOPO"), new Profesor("Dra. Ana", "López", 654321, "Ingeniería de Sistemas"));
        Inscripcion inscripcion2 = new Inscripcion(grupo2, 1, "aprobada", 3.5);
        Grupo grupo3 = new Grupo(3, 1, 30, true, CatalogoMaterias.getMateria("CALD"), new Profesor("Dr. Juan",  "Pérez", 112233, "Matemáticas"));
        Inscripcion inscripcion3 = new Inscripcion(grupo3, 1, "reprobada", 2.7);
        semestre1.agregarMateria(inscripcion1);
        semestre1.agregarMateria(inscripcion2);
        semestre1.agregarMateria(inscripcion3);
    }

    @Test
    @DisplayName("Debería crear semáforo académico correctamente")
    void testCrearSemaforoAcademico() {
        assertNotNull(semaforo);
        assertEquals(1, semaforo.getId());
        assertEquals(0, semaforo.getAvancePorcentaje());
        assertEquals(160, semaforo.getTotalCreditos());
        assertEquals(0.0, semaforo.getPromedioAcumulado());
    }

    @Test
    @DisplayName("Debería agregar semestre al semáforo académico")
    void testAgregarSemestre() {
        semaforo.agregarSemestre(semestre1);
        assertEquals(1, semaforo.getSemestres().size());
        assertEquals(semestre1, semaforo.getSemestres().get(0));
    }

    @Test
    @DisplayName("Debería actualizar avance académico y calcular promedio acumulado")
    void testActualizarAvanceYCalcularPromedio() {
        semaforo.agregarSemestre(semestre1);
        semaforo.actualizarAvance(semestre1.getMaterias());
        assertEquals(5, semaforo.getAvancePorcentaje());
        assertEquals(3.5, semaforo.getPromedioAcumulado(), 0.001);

    }
}

