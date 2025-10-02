package com.example.fantasticosback.Controller;

import com.example.fantasticosback.Dtos.ResponseDTO;
import com.example.fantasticosback.Dtos.StudentDTO;
import com.example.fantasticosback.Model.Carrera;
import com.example.fantasticosback.Model.Estudiante;
import com.example.fantasticosback.Server.StudentService;
import com.example.fantasticosback.util.SemaforoAcademico;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentControllerTest {

    @Mock
    private StudentService studentService;

    @InjectMocks
    private StudentController studentController;

    @Test
    void testCrear() {
        StudentDTO dto = new StudentDTO("E001", "Juan", "Ing", 1);
        Carrera carrera = new Carrera("Ing", 160);
        SemaforoAcademico semaforo = new SemaforoAcademico(1, 0, carrera);
        Estudiante estudiante = new Estudiante("Juan", "Perez", 123, "Ing", "C001", "E001", 1, semaforo);

        when(studentService.convertirADominio(dto)).thenReturn(estudiante);
        when(studentService.guardar(estudiante)).thenReturn(estudiante);
        when(studentService.convertirAEstudianteDTO(estudiante)).thenReturn(dto);

        ResponseEntity<ResponseDTO<StudentDTO>> response = studentController.crear(dto);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Success", response.getBody().getStatus());
        assertEquals("Estudiante creado correctamente", response.getBody().getMessage());
        assertEquals(dto, response.getBody().getData());
    }

    @Test
    void testListar() {
        StudentDTO dto1 = new StudentDTO("E001", "Juan", "Ing", 1);
        StudentDTO dto2 = new StudentDTO("E002", "Ana", "Med", 2);
        List<StudentDTO> listaDTOs = Arrays.asList(dto1, dto2);

        when(studentService.obtenerTodos()).thenReturn(Arrays.asList()); // original no importa
        when(studentService.convertirLista(any())).thenReturn(listaDTOs);

        ResponseEntity<ResponseDTO<List<StudentDTO>>> response = studentController.listar();

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Success", response.getBody().getStatus());
        assertEquals("Lista de estudiantes", response.getBody().getMessage());
        assertEquals(listaDTOs, response.getBody().getData());
    }

    @Test
    void testObtener_Existe() {
        String id = "E001";
        StudentDTO dto = new StudentDTO(id, "Juan", "Ing", 1);
        Carrera carrera = new Carrera("Ing", 160);
        SemaforoAcademico semaforo = new SemaforoAcademico(1, 0, carrera);
        Estudiante estudiante = new Estudiante("Juan", "Perez", 123, "Ing", "C001", id, 1, semaforo);

        when(studentService.obtenerPorId(id)).thenReturn(estudiante);
        when(studentService.convertirAEstudianteDTO(estudiante)).thenReturn(dto);

        ResponseEntity<ResponseDTO<StudentDTO>> response = studentController.obtener(id);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Success", response.getBody().getStatus());
        assertEquals("Estudiante encontrado", response.getBody().getMessage());
        assertEquals(dto, response.getBody().getData());
    }

    @Test
    void testObtener_NoExiste() {
        when(studentService.obtenerPorId("E001")).thenReturn(null);

        ResponseEntity<ResponseDTO<StudentDTO>> response = studentController.obtener("E001");

        assertEquals(404, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Error", response.getBody().getStatus());
        assertEquals("Estudiante no encontrado", response.getBody().getMessage());
        assertNull(response.getBody().getData());
    }

    @Test
    void testActualizar_Existe() {
        String id = "E001";
        StudentDTO dto = new StudentDTO(id, "Juan", "Ing", 1);
        Carrera carrera = new Carrera("Ing", 160);
        SemaforoAcademico semaforo = new SemaforoAcademico(1, 0, carrera);
        Estudiante estudiante = new Estudiante("Juan", "Perez", 123, "Ing", "C001", id, 1, semaforo);

        when(studentService.obtenerPorId(id)).thenReturn(estudiante);
        when(studentService.convertirADominio(dto)).thenReturn(estudiante);
        when(studentService.actualizar(estudiante)).thenReturn(estudiante);
        when(studentService.convertirAEstudianteDTO(estudiante)).thenReturn(dto);

        ResponseEntity<ResponseDTO<StudentDTO>> response = studentController.actualizar(id, dto);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Success", response.getBody().getStatus());
        assertEquals("Estudiante actualizado", response.getBody().getMessage());
        assertEquals(dto, response.getBody().getData());
    }

    @Test
    void testActualizar_NoExiste() {
        String id = "E001";
        StudentDTO dto = new StudentDTO(id, "Juan", "Ing", 1);

        when(studentService.obtenerPorId(id)).thenReturn(null);

        ResponseEntity<ResponseDTO<StudentDTO>> response = studentController.actualizar(id, dto);

        assertEquals(404, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Error", response.getBody().getStatus());
        assertEquals("Estudiante no encontrado", response.getBody().getMessage());
        assertNull(response.getBody().getData());
    }

    @Test
    void testEliminar_Existe() {
        String id = "E001";
        Carrera carrera = new Carrera("Ing", 160);
        SemaforoAcademico semaforo = new SemaforoAcademico(1, 0, carrera);
        Estudiante estudiante = new Estudiante("Juan", "Perez", 123, "Ing", "C001", id, 1, semaforo);

        when(studentService.obtenerPorId(id)).thenReturn(estudiante);
        doNothing().when(studentService).eliminar(id);

        ResponseEntity<ResponseDTO<Void>> response = studentController.eliminar(id);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Success", response.getBody().getStatus());
        assertEquals("Estudiante eliminado correctamente", response.getBody().getMessage());
        assertNull(response.getBody().getData());
    }

    @Test
    void testEliminar_NoExiste() {
        String id = "E001";
        when(studentService.obtenerPorId(id)).thenReturn(null);

        ResponseEntity<ResponseDTO<Void>> response = studentController.eliminar(id);

        assertEquals(404, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Error", response.getBody().getStatus());
        assertEquals("Estudiante no encontrado", response.getBody().getMessage());
        assertNull(response.getBody().getData());
    }

    @Test
    void testObtenerPorCarrera() {
        String carrera = "Ing";
        StudentDTO dto1 = new StudentDTO("E001", "Juan", "Ing", 1);
        List<StudentDTO> listaDTOs = Arrays.asList(dto1);

        when(studentService.obtenerPorCarrera(carrera)).thenReturn(Arrays.asList()); // original no importa
        when(studentService.convertirLista(any())).thenReturn(listaDTOs);

        ResponseEntity<ResponseDTO<List<StudentDTO>>> response = studentController.obtenerPorCarrera(carrera);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Success", response.getBody().getStatus());
        assertEquals("Estudiantes por carrera", response.getBody().getMessage());
        assertEquals(listaDTOs, response.getBody().getData());
    }

    @Test
    void testObtenerPorSemestre() {
        int semestre = 1;
        StudentDTO dto1 = new StudentDTO("E001", "Juan", "Ing", 1);
        List<StudentDTO> listaDTOs = Arrays.asList(dto1);

        when(studentService.obtenerPorSemestre(semestre)).thenReturn(Arrays.asList()); // original no importa
        when(studentService.convertirLista(any())).thenReturn(listaDTOs);

        ResponseEntity<ResponseDTO<List<StudentDTO>>> response = studentController.obtenerPorSemestre(semestre);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Success", response.getBody().getStatus());
        assertEquals("Estudiantes por semestre", response.getBody().getMessage());
        assertEquals(listaDTOs, response.getBody().getData());
    }
}
