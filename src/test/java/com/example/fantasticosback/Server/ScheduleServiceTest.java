package com.example.fantasticosback.Server;

import com.example.fantasticosback.Model.*;
import com.example.fantasticosback.Repository.DeanOfficeRepository;
import com.example.fantasticosback.Repository.StudentRepository;
import com.example.fantasticosback.Repository.ProfessorRepository;
import com.example.fantasticosback.enums.UserRole;
import com.example.fantasticosback.util.SemaforoAcademico;
import com.example.fantasticosback.util.ClassSession;
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
class ScheduleServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private ProfessorRepository professorRepository;

    @Mock
    private DeanOfficeRepository deanOfficeRepository;

    @InjectMocks
    private ScheduleService scheduleService;

    private Student student;
    private Professor professor;
    private DeanOffice deanOffice;
    private Subject subject;
    private Group group;
    private Enrollment enrollment;
    private Semester semester;

    @BeforeEach
    void setUp() {
        // Crear estudiante de prueba
        Career career = new Career("Ingeniería de Sistemas", 160);
        SemaforoAcademico semaforo = new SemaforoAcademico(1, 0, career);
        student = new Student("Juan", "Pérez", 123456789, "Ingeniería", "C001", "E001", 5, semaforo);
        
        // Crear profesor de prueba
        professor = new Professor("María", "González", 987654321, "Matemáticas");
        professor.setId("P001");

        // Crear decanatura de prueba
        deanOffice = new DeanOffice("D001", "Ingeniería");

        // Crear materia de prueba
        subject = new Subject(1001, "Cálculo I", 4, 1);

        // Crear grupo de prueba
        group = new Group(1, 1, 30, true, subject, professor);
        
        // Agregar sesiones al grupo
        ClassSession sesion1 = new ClassSession("Lunes", "08:00", "10:00", "Aula 101");
        ClassSession sesion2 = new ClassSession("Miércoles", "08:00", "10:00", "Aula 101");
        group.agregarSesion(sesion1);
        group.agregarSesion(sesion2);

        // Crear inscripción de prueba
        enrollment = new Enrollment(group, 1, "activa", 0.0);

        // Crear semestre de prueba
        semester = new Semester(1, 2024, 1, true);
        semester.agregarMateria(enrollment);

        // Agregar semestre al estudiante
        student.agregarSemestre(semester);
    }

    @Test
    void testObtenerHorarioEstudiante_EstudianteConsultandoSuPropioHorario_Success() throws Exception {
        // Arrange
        when(studentRepository.findById("E001")).thenReturn(Optional.of(student));

        // Act
        Map<String, Object> resultado = scheduleService.obtenerHorarioEstudiante("E001", "E001", UserRole.STUDENT);

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
        when(studentRepository.findById("E002")).thenReturn(Optional.of(student));

        // Act & Assert
        IllegalAccessException exception = assertThrows(IllegalAccessException.class, () -> {
            scheduleService.obtenerHorarioEstudiante("E002", "E001", UserRole.STUDENT);
        });

        assertEquals("Los estudiantes solo pueden consultar su propio horario", exception.getMessage());
        verify(studentRepository).findById("E002");
    }

    @Test
    void testObtenerHorarioEstudiante_ProfesorConsultandoEstudianteAsignado_Success() throws Exception {
        // Arrange
        when(studentRepository.findById("E001")).thenReturn(Optional.of(student));
        when(professorRepository.findById("P001")).thenReturn(Optional.of(professor));

        // Act
        Map<String, Object> resultado = scheduleService.obtenerHorarioEstudiante("E001", "P001", UserRole.PROFESSOR);

        // Assert
        assertNotNull(resultado);
        assertEquals("E001", resultado.get("estudianteId"));
        assertTrue(resultado.get("clases") instanceof ArrayList);
        ArrayList<?> clases = (ArrayList<?>) resultado.get("clases");
        assertEquals(2, clases.size());
        
        verify(studentRepository).findById("E001");
        verify(professorRepository).findById("P001");
    }

    @Test
    void testObtenerHorarioEstudiante_ProfesorConsultandoEstudianteNoAsignado_ThrowsIllegalAccessException() {
        // Arrange
        Professor otroProfessor = new Professor("Carlos", "Martínez", 555555555, "Física");
        otroProfessor.setId("P002");
        
        when(studentRepository.findById("E001")).thenReturn(Optional.of(student));
        when(professorRepository.findById("P002")).thenReturn(Optional.of(otroProfessor));

        // Act & Assert
        IllegalAccessException exception = assertThrows(IllegalAccessException.class, () -> {
            scheduleService.obtenerHorarioEstudiante("E001", "P002", UserRole.PROFESSOR);
        });

        assertEquals("Los profesores solo pueden consultar el horario de sus estudiantes asignados", exception.getMessage());
        verify(studentRepository).findById("E001");
        verify(professorRepository).findById("P002");
    }

    @Test
    void testObtenerHorarioEstudiante_DecanaturaConsultandoEstudianteDeSuFacultad_Success() throws Exception {
        // Arrange
        when(studentRepository.findById("E001")).thenReturn(Optional.of(student));
        when(deanOfficeRepository.findById("D001")).thenReturn(Optional.of(deanOffice));

        // Act
        Map<String, Object> resultado = scheduleService.obtenerHorarioEstudiante("E001", "D001", UserRole.DEANOFFICE);

        // Assert
        assertNotNull(resultado);
        assertEquals("E001", resultado.get("estudianteId"));
        assertTrue(resultado.get("clases") instanceof ArrayList);
        ArrayList<?> clases = (ArrayList<?>) resultado.get("clases");
        assertEquals(2, clases.size());
        
        verify(studentRepository).findById("E001");
        verify(deanOfficeRepository).findById("D001");
    }

    @Test
    void testObtenerHorarioEstudiante_DecanaturaConsultandoEstudianteDeOtraFacultad_ThrowsIllegalAccessException() {
        // Arrange
        DeanOffice otraDeanOffice = new DeanOffice("D002", "Medicina");
        
        when(studentRepository.findById("E001")).thenReturn(Optional.of(student));
        when(deanOfficeRepository.findById("D002")).thenReturn(Optional.of(otraDeanOffice));

        // Act & Assert
        IllegalAccessException exception = assertThrows(IllegalAccessException.class, () -> {
            scheduleService.obtenerHorarioEstudiante("E001", "D002", UserRole.DEANOFFICE);
        });

        assertEquals("La decanatura solo puede consultar estudiantes de su facultad", exception.getMessage());
        verify(studentRepository).findById("E001");
        verify(deanOfficeRepository).findById("D002");
    }

    @Test
    void testObtenerHorarioEstudiante_EstudianteNoExiste_ThrowsIllegalArgumentException() {
        // Arrange
        when(studentRepository.findById("E999")).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            scheduleService.obtenerHorarioEstudiante("E999", "E999", UserRole.STUDENT);
        });

        assertEquals("Estudiante no encontrado con ID: E999", exception.getMessage());
        verify(studentRepository).findById("E999");
    }

    @Test
    void testObtenerHorarioEstudiante_ProfesorNoExiste_ThrowsIllegalArgumentException() {
        // Arrange
        when(studentRepository.findById("E001")).thenReturn(Optional.of(student));
        when(professorRepository.findById("P999")).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            scheduleService.obtenerHorarioEstudiante("E001", "P999", UserRole.PROFESSOR);
        });

        assertEquals("Profesor no encontrado con ID: P999", exception.getMessage());
        verify(studentRepository).findById("E001");
        verify(professorRepository).findById("P999");
    }

    @Test
    void testObtenerHorarioEstudiante_DecanaturaNoExiste_ThrowsIllegalArgumentException() {
        // Arrange
        when(studentRepository.findById("E001")).thenReturn(Optional.of(student));
        when(deanOfficeRepository.findById("D999")).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            scheduleService.obtenerHorarioEstudiante("E001", "D999", UserRole.DEANOFFICE);
        });

        assertEquals("Decanatura no encontrada con ID: D999", exception.getMessage());
        verify(studentRepository).findById("E001");
        verify(deanOfficeRepository).findById("D999");
    }

    @Test
    void testObtenerHorarioEstudiante_EstudianteSinSemestres_RetornaHorarioVacio() throws Exception {
        // Arrange
        Career career = new Career("Ingeniería de Sistemas", 160);
        SemaforoAcademico semaforo = new SemaforoAcademico(1, 0, career);
        Student studentSinSemestres = new Student("Ana", "López", 111111111, "Ingeniería", "C002", "E002", 1, semaforo);
        when(studentRepository.findById("E002")).thenReturn(Optional.of(studentSinSemestres));

        // Act
        Map<String, Object> resultado = scheduleService.obtenerHorarioEstudiante("E002", "E002", UserRole.STUDENT);

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
        enrollment.cancelar(); // Cancelar la inscripción
        when(studentRepository.findById("E001")).thenReturn(Optional.of(student));

        // Act
        Map<String, Object> resultado = scheduleService.obtenerHorarioEstudiante("E001", "E001", UserRole.STUDENT);

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.get("clases") instanceof ArrayList);
        ArrayList<?> clases = (ArrayList<?>) resultado.get("clases");
        assertTrue(clases.isEmpty()); // No debe incluir clases canceladas
        
        verify(studentRepository).findById("E001");
    }
}