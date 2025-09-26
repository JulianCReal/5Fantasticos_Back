package com.example.fantasticosback.Server;

import com.example.fantasticosback.Model.*;
import com.example.fantasticosback.Repository.DecanaturaRepository;
import com.example.fantasticosback.Repository.StudentRepository;
import com.example.fantasticosback.Repository.TeacherRepository;
import com.example.fantasticosback.enums.UserRole;
import com.example.fantasticosback.util.SemaforoAcademico;
import com.example.fantasticosback.util.SesionClase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HorarioServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private TeacherRepository teacherRepository;

    @Mock
    private DecanaturaRepository decanaturaRepository;

    @InjectMocks
    private HorarioService horarioService;

    private Estudiante estudiante;
    private Profesor profesor;
    private Decanatura decanatura;
    private Materia materia;
    private Grupo grupo;
    private Inscripcion inscripcion;
    private Semestre semestre;

    @BeforeEach
    void setUp() {
        // Crear estudiante de prueba
        Carrera carrera = new Carrera("Ingeniería de Sistemas", 160);
        SemaforoAcademico semaforo = new SemaforoAcademico(1, 0, carrera);
        estudiante = new Estudiante("Juan", "Pérez", 123456789, "Ingeniería", "C001", "E001", 5, semaforo);
        
        // Crear profesor de prueba
        profesor = new Profesor("María", "González", 987654321, "Matemáticas");
        profesor.setId("P001");

        // Crear decanatura de prueba
        decanatura = new Decanatura("D001", "Ingeniería");

        // Crear materia de prueba
        materia = new Materia(1001, "Cálculo I", 4, 1);

        // Crear grupo de prueba
        grupo = new Grupo(1, 1, 30, true, materia, profesor);
        
        // Agregar sesiones al grupo
        SesionClase sesion1 = new SesionClase("Lunes", "08:00", "10:00", "Aula 101");
        SesionClase sesion2 = new SesionClase("Miércoles", "08:00", "10:00", "Aula 101");
        grupo.agregarSesion(sesion1);
        grupo.agregarSesion(sesion2);

        // Crear inscripción de prueba
        inscripcion = new Inscripcion(grupo, 1, "activa", 0.0);

        // Crear semestre de prueba
        semestre = new Semestre(1, 2024, 1, true);
        semestre.agregarMateria(inscripcion);

        // Agregar semestre al estudiante
        estudiante.agregarSemestre(semestre);
    }

    @Test
    void testObtenerHorarioEstudiante_EstudianteConsultandoSuPropioHorario_Success() throws Exception {
        // Arrange
        when(studentRepository.findById("E001")).thenReturn(Optional.of(estudiante));

        // Act
        Map<String, Object> resultado = horarioService.obtenerHorarioEstudiante("E001", "E001", UserRole.ESTUDIANTE);

        // Assert
        assertNotNull(resultado);
        assertEquals("E001", resultado.get("estudianteId"));
        assertEquals("Juan Pérez", resultado.get("estudianteNombre"));
        assertEquals("Ingeniería", resultado.get("carrera"));
        assertEquals(5, resultado.get("semestre"));
        assertTrue(resultado.get("clases") instanceof ArrayList);
        ArrayList<?> clases = (ArrayList<?>) resultado.get("clases");
        assertEquals(2, clases.size()); // Dos sesiones de la materia
        
        verify(studentRepository).findById("E001");
    }

    @Test
    void testObtenerHorarioEstudiante_EstudianteConsultandoOtroHorario_ThrowsIllegalAccessException() {
        // Arrange
        when(studentRepository.findById("E002")).thenReturn(Optional.of(estudiante));

        // Act & Assert
        IllegalAccessException exception = assertThrows(IllegalAccessException.class, () -> {
            horarioService.obtenerHorarioEstudiante("E002", "E001", UserRole.ESTUDIANTE);
        });

        assertEquals("Los estudiantes solo pueden consultar su propio horario", exception.getMessage());
        verify(studentRepository).findById("E002");
    }

    @Test
    void testObtenerHorarioEstudiante_ProfesorConsultandoEstudianteAsignado_Success() throws Exception {
        // Arrange
        when(studentRepository.findById("E001")).thenReturn(Optional.of(estudiante));
        when(teacherRepository.findById("P001")).thenReturn(Optional.of(profesor));

        // Act
        Map<String, Object> resultado = horarioService.obtenerHorarioEstudiante("E001", "P001", UserRole.PROFESOR);

        // Assert
        assertNotNull(resultado);
        assertEquals("E001", resultado.get("estudianteId"));
        assertTrue(resultado.get("clases") instanceof ArrayList);
        ArrayList<?> clases = (ArrayList<?>) resultado.get("clases");
        assertEquals(2, clases.size());
        
        verify(studentRepository).findById("E001");
        verify(teacherRepository).findById("P001");
    }

    @Test
    void testObtenerHorarioEstudiante_ProfesorConsultandoEstudianteNoAsignado_ThrowsIllegalAccessException() {
        // Arrange
        Profesor otroProfesor = new Profesor("Carlos", "Martínez", 555555555, "Física");
        otroProfesor.setId("P002");
        
        when(studentRepository.findById("E001")).thenReturn(Optional.of(estudiante));
        when(teacherRepository.findById("P002")).thenReturn(Optional.of(otroProfesor));

        // Act & Assert
        IllegalAccessException exception = assertThrows(IllegalAccessException.class, () -> {
            horarioService.obtenerHorarioEstudiante("E001", "P002", UserRole.PROFESOR);
        });

        assertEquals("Los profesores solo pueden consultar el horario de sus estudiantes asignados", exception.getMessage());
        verify(studentRepository).findById("E001");
        verify(teacherRepository).findById("P002");
    }

    @Test
    void testObtenerHorarioEstudiante_DecanaturaConsultandoEstudianteDeSuFacultad_Success() throws Exception {
        // Arrange
        when(studentRepository.findById("E001")).thenReturn(Optional.of(estudiante));
        when(decanaturaRepository.findById("D001")).thenReturn(Optional.of(decanatura));

        // Act
        Map<String, Object> resultado = horarioService.obtenerHorarioEstudiante("E001", "D001", UserRole.DECANATURA);

        // Assert
        assertNotNull(resultado);
        assertEquals("E001", resultado.get("estudianteId"));
        assertTrue(resultado.get("clases") instanceof ArrayList);
        ArrayList<?> clases = (ArrayList<?>) resultado.get("clases");
        assertEquals(2, clases.size());
        
        verify(studentRepository).findById("E001");
        verify(decanaturaRepository).findById("D001");
    }

    @Test
    void testObtenerHorarioEstudiante_DecanaturaConsultandoEstudianteDeOtraFacultad_ThrowsIllegalAccessException() {
        // Arrange
        Decanatura otraDecanatura = new Decanatura("D002", "Medicina");
        
        when(studentRepository.findById("E001")).thenReturn(Optional.of(estudiante));
        when(decanaturaRepository.findById("D002")).thenReturn(Optional.of(otraDecanatura));

        // Act & Assert
        IllegalAccessException exception = assertThrows(IllegalAccessException.class, () -> {
            horarioService.obtenerHorarioEstudiante("E001", "D002", UserRole.DECANATURA);
        });

        assertEquals("La decanatura solo puede consultar estudiantes de su facultad", exception.getMessage());
        verify(studentRepository).findById("E001");
        verify(decanaturaRepository).findById("D002");
    }

    @Test
    void testObtenerHorarioEstudiante_EstudianteNoExiste_ThrowsIllegalArgumentException() {
        // Arrange
        when(studentRepository.findById("E999")).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            horarioService.obtenerHorarioEstudiante("E999", "E999", UserRole.ESTUDIANTE);
        });

        assertEquals("Estudiante no encontrado con ID: E999", exception.getMessage());
        verify(studentRepository).findById("E999");
    }

    @Test
    void testObtenerHorarioEstudiante_ProfesorNoExiste_ThrowsIllegalArgumentException() {
        // Arrange
        when(studentRepository.findById("E001")).thenReturn(Optional.of(estudiante));
        when(teacherRepository.findById("P999")).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            horarioService.obtenerHorarioEstudiante("E001", "P999", UserRole.PROFESOR);
        });

        assertEquals("Profesor no encontrado con ID: P999", exception.getMessage());
        verify(studentRepository).findById("E001");
        verify(teacherRepository).findById("P999");
    }

    @Test
    void testObtenerHorarioEstudiante_DecanaturaNoExiste_ThrowsIllegalArgumentException() {
        // Arrange
        when(studentRepository.findById("E001")).thenReturn(Optional.of(estudiante));
        when(decanaturaRepository.findById("D999")).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            horarioService.obtenerHorarioEstudiante("E001", "D999", UserRole.DECANATURA);
        });

        assertEquals("Decanatura no encontrada con ID: D999", exception.getMessage());
        verify(studentRepository).findById("E001");
        verify(decanaturaRepository).findById("D999");
    }

    @Test
    void testObtenerHorarioEstudiante_EstudianteSinSemestres_RetornaHorarioVacio() throws Exception {
        // Arrange
        Carrera carrera = new Carrera("Ingeniería de Sistemas", 160);
        SemaforoAcademico semaforo = new SemaforoAcademico(1, 0, carrera);
        Estudiante estudianteSinSemestres = new Estudiante("Ana", "López", 111111111, "Ingeniería", "C002", "E002", 1, semaforo);
        when(studentRepository.findById("E002")).thenReturn(Optional.of(estudianteSinSemestres));

        // Act
        Map<String, Object> resultado = horarioService.obtenerHorarioEstudiante("E002", "E002", UserRole.ESTUDIANTE);

        // Assert
        assertNotNull(resultado);
        assertEquals("E002", resultado.get("estudianteId"));
        assertEquals("Ana López", resultado.get("estudianteNombre"));
        assertTrue(resultado.get("clases") instanceof ArrayList);
        ArrayList<?> clases = (ArrayList<?>) resultado.get("clases");
        assertTrue(clases.isEmpty());
        
        verify(studentRepository).findById("E002");
    }

    @Test
    void testObtenerHorarioEstudiante_InscripcionCancelada_NoIncluirEnHorario() throws Exception {
        // Arrange
        inscripcion.cancelar(); // Cancelar la inscripción
        when(studentRepository.findById("E001")).thenReturn(Optional.of(estudiante));

        // Act
        Map<String, Object> resultado = horarioService.obtenerHorarioEstudiante("E001", "E001", UserRole.ESTUDIANTE);

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.get("clases") instanceof ArrayList);
        ArrayList<?> clases = (ArrayList<?>) resultado.get("clases");
        assertTrue(clases.isEmpty()); // No debe incluir clases canceladas
        
        verify(studentRepository).findById("E001");
    }
}