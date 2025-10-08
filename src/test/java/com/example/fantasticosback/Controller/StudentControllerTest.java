package com.example.fantasticosback.Controller;

import com.example.fantasticosback.Dtos.ResponseDTO;
import com.example.fantasticosback.Dtos.StudentDTO;
import com.example.fantasticosback.Model.Entities.Career;
import com.example.fantasticosback.Model.Entities.Student;
import com.example.fantasticosback.Persistence.Controller.StudentController;
import com.example.fantasticosback.Persistence.Server.StudentService;
import com.example.fantasticosback.util.AcademicTrafficLight;
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
    void testCreate() {
        StudentDTO dto = new StudentDTO("E001", "Juan", "Engineering", 1);
        Career career = new Career("Engineering", 160);
        AcademicTrafficLight trafficLight = new AcademicTrafficLight(1, 0, career);
        Student student = new Student("Juan", "Perez", 123, "Engineering", "C001", "E001", 1, trafficLight);

        when(studentService.convertToDomain(dto)).thenReturn(student);
        when(studentService.save(student)).thenReturn(student);
        when(studentService.convertToStudentDTO(student)).thenReturn(dto);

        ResponseEntity<ResponseDTO<StudentDTO>> response = studentController.create(dto);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Success", response.getBody().getStatus());
        assertEquals("Student created successfully", response.getBody().getMessage());
        assertEquals(dto, response.getBody().getData());
    }

    @Test
    void testList() {
        Career career = new Career("Engineering", 160);
        AcademicTrafficLight trafficLight = new AcademicTrafficLight(1, 0, career);
        Student student1 = new Student("Juan", "Perez", 123, "Engineering", "C001", "E001", 1, trafficLight);
        Student student2 = new Student("Ana", "Lopez", 456, "Engineering", "C002", "E002", 2, trafficLight);

        List<Student> students = Arrays.asList(student1, student2);
        List<StudentDTO> dtos = Arrays.asList(
                new StudentDTO("E001", "Juan", "Engineering", 1),
                new StudentDTO("E002", "Ana", "Engineering", 2)
        );

        when(studentService.findAll()).thenReturn(students);
        when(studentService.convertList(students)).thenReturn(dtos);

        ResponseEntity<ResponseDTO<List<StudentDTO>>> response = studentController.list();

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Success", response.getBody().getStatus());
        assertEquals("List of students", response.getBody().getMessage());
        assertEquals(dtos, response.getBody().getData());
    }

    @Test
    void testGetSuccess() {
        String id = "E001";
        Career career = new Career("Engineering", 160);
        AcademicTrafficLight trafficLight = new AcademicTrafficLight(1, 0, career);
        Student student = new Student("Juan", "Perez", 123, "Engineering", "C001", "E001", 1, trafficLight);
        StudentDTO dto = new StudentDTO("E001", "Juan", "Engineering", 1);

        when(studentService.findById(id)).thenReturn(student);
        when(studentService.convertToStudentDTO(student)).thenReturn(dto);

        ResponseEntity<ResponseDTO<StudentDTO>> response = studentController.get(id);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Success", response.getBody().getStatus());
        assertEquals("Student found", response.getBody().getMessage());
        assertEquals(dto, response.getBody().getData());
    }

    @Test
    void testGetNotFound() {
        when(studentService.findById("E001")).thenReturn(null);

        ResponseEntity<ResponseDTO<StudentDTO>> response = studentController.get("E001");

        assertEquals(404, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Error", response.getBody().getStatus());
        assertEquals("Student not found", response.getBody().getMessage());
        assertNull(response.getBody().getData());
    }

    @Test
    void testUpdateSuccess() {
        String id = "E001";
        StudentDTO dto = new StudentDTO("E001", "Juan Updated", "Engineering", 2);
        Career career = new Career("Engineering", 160);
        AcademicTrafficLight trafficLight = new AcademicTrafficLight(1, 0, career);
        Student existingStudent = new Student("Juan", "Perez", 123, "Engineering", "C001", "E001", 1, trafficLight);
        Student updatedStudent = new Student("Juan Updated", "Perez", 123, "Engineering", "C001", "E001", 2, trafficLight);

        when(studentService.findById(id)).thenReturn(existingStudent);
        when(studentService.convertToDomain(dto)).thenReturn(updatedStudent);
        when(studentService.update(updatedStudent)).thenReturn(updatedStudent);
        when(studentService.convertToStudentDTO(updatedStudent)).thenReturn(dto);

        ResponseEntity<ResponseDTO<StudentDTO>> response = studentController.update(id, dto);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Success", response.getBody().getStatus());
        assertEquals("Student updated", response.getBody().getMessage());
        assertEquals(dto, response.getBody().getData());
    }

    @Test
    void testUpdateNotFound() {
        String id = "E001";
        StudentDTO dto = new StudentDTO("E001", "Juan", "Engineering", 1);

        when(studentService.findById(id)).thenReturn(null);

        ResponseEntity<ResponseDTO<StudentDTO>> response = studentController.update(id, dto);

        assertEquals(404, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Error", response.getBody().getStatus());
        assertEquals("Student not found", response.getBody().getMessage());
        assertNull(response.getBody().getData());
    }

    @Test
    void testDeleteSuccess() {
        String id = "E001";
        Career career = new Career("Engineering", 160);
        AcademicTrafficLight trafficLight = new AcademicTrafficLight(1, 0, career);
        Student student = new Student("Juan", "Perez", 123, "Engineering", "C001", "E001", 1, trafficLight);

        when(studentService.findById(id)).thenReturn(student);

        ResponseEntity<ResponseDTO<Void>> response = studentController.delete(id);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Success", response.getBody().getStatus());
        assertEquals("Student deleted successfully", response.getBody().getMessage());
        assertNull(response.getBody().getData());
        verify(studentService).delete(id);
    }

    @Test
    void testDeleteNotFound() {
        String id = "E001";

        when(studentService.findById(id)).thenReturn(null);

        ResponseEntity<ResponseDTO<Void>> response = studentController.delete(id);

        assertEquals(404, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Error", response.getBody().getStatus());
        assertEquals("Student not found", response.getBody().getMessage());
        assertNull(response.getBody().getData());
        verify(studentService, never()).delete(id);
    }

    @Test
    void testGetByCareer() {
        String career = "Engineering";
        Career careerObj = new Career("Engineering", 160);
        AcademicTrafficLight trafficLight = new AcademicTrafficLight(1, 0, careerObj);
        List<Student> students = Arrays.asList(
                new Student("Juan", "Perez", 123, "Engineering", "C001", "E001", 1, trafficLight),
                new Student("Ana", "Lopez", 456, "Engineering", "C002", "E002", 2, trafficLight)
        );
        List<StudentDTO> dtos = Arrays.asList(
                new StudentDTO("E001", "Juan", "Engineering", 1),
                new StudentDTO("E002", "Ana", "Engineering", 2)
        );

        when(studentService.findByCareer(career)).thenReturn(students);
        when(studentService.convertList(students)).thenReturn(dtos);

        ResponseEntity<ResponseDTO<List<StudentDTO>>> response = studentController.getByCareer(career);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Success", response.getBody().getStatus());
        assertEquals("Students by career", response.getBody().getMessage());
        assertEquals(dtos, response.getBody().getData());
    }

    @Test
    void testGetBySemester() {
        int semester = 1;
        Career career = new Career("Engineering", 160);
        AcademicTrafficLight trafficLight = new AcademicTrafficLight(1, 0, career);
        List<Student> students = Arrays.asList(
                new Student("Juan", "Perez", 123, "Engineering", "C001", "E001", 1, trafficLight)
        );
        List<StudentDTO> dtos = Arrays.asList(
                new StudentDTO("E001", "Juan", "Engineering", 1)
        );

        when(studentService.findBySemester(semester)).thenReturn(students);
        when(studentService.convertList(students)).thenReturn(dtos);

        ResponseEntity<ResponseDTO<List<StudentDTO>>> response = studentController.getBySemester(semester);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Success", response.getBody().getStatus());
        assertEquals("Students by semester", response.getBody().getMessage());
        assertEquals(dtos, response.getBody().getData());
    }
}
