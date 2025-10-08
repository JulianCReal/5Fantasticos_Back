package com.example.fantasticosback.Server;

import com.example.fantasticosback.Model.Entities.Career;
import com.example.fantasticosback.Model.Entities.Student;
import com.example.fantasticosback.Persistence.Repository.StudentRepository;
import com.example.fantasticosback.Persistence.Server.StudentService;
import com.example.fantasticosback.util.AcademicTrafficLight;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {
    @Mock
    private StudentRepository studentRepository;
    @InjectMocks
    private StudentService studentService;

    private Student getStudentDummy() {
        Career career = new Career("Systems Engineering", 160);
        AcademicTrafficLight trafficLight = new AcademicTrafficLight(1, 0, career);
        return new Student("John", "Perez", 123, "Engineering", "C001", "E001", 1, trafficLight);
    }

    @Test
    void testSave() {
        Student student = getStudentDummy();
        when(studentRepository.save(student)).thenReturn(student);
        assertEquals(student, studentService.save(student));
    }

    @Test
    void testFindAll() {
        List<Student> list = List.of(getStudentDummy());
        when(studentRepository.findAll()).thenReturn(list);
        assertEquals(list, studentService.findAll());
    }

    @Test
    void testFindById() {
        Student student = getStudentDummy();
        when(studentRepository.findById("1")).thenReturn(Optional.of(student));
        assertEquals(student, studentService.findById("1"));
        when(studentRepository.findById("2")).thenReturn(Optional.empty());
        assertNull(studentService.findById("2"));
    }

    @Test
    void testUpdate() {
        Student student = getStudentDummy();
        when(studentRepository.save(student)).thenReturn(student);
        assertEquals(student, studentService.update(student));
    }

    @Test
    void testDelete() {
        studentService.delete("1");
        verify(studentRepository).deleteById("1");
    }

    @Test
    void testFindByCareer() {
        List<Student> list = List.of(getStudentDummy());
        when(studentRepository.findByCareer("Engineering")).thenReturn(list);
        assertEquals(list, studentService.findByCareer("Engineering"));
    }

    @Test
    void testFindBySemester() {
        List<Student> list = List.of(getStudentDummy());
        when(studentRepository.findBySemester(1)).thenReturn(list);
        assertEquals(list, studentService.findBySemester(1));
    }
}
