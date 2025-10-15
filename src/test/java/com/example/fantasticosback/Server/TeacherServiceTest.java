package com.example.fantasticosback.Server;

import com.example.fantasticosback.Model.Document.Teacher;
import com.example.fantasticosback.Repository.TeacherRepository;
import com.example.fantasticosback.Service.TeacherService;
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
class TeacherServiceTest {
    @Mock
    private TeacherRepository teacherRepository;
    @InjectMocks
    private TeacherService teacherService;

    private Teacher getTeacherDummy() {
        Teacher teacher = new Teacher("John", "Perez", 123, "Mathematics");
        teacher.setId("1");
        return teacher;
    }

    @Test
    void testSave() {
        Teacher teacher = getTeacherDummy();
        when(teacherRepository.save(teacher)).thenReturn(teacher);
        assertEquals(teacher, teacherService.save(teacher));
    }

    @Test
    void testFindAll() {
        List<Teacher> list = List.of(getTeacherDummy());
        when(teacherRepository.findAll()).thenReturn(list);
        assertEquals(list, teacherService.findAll());
    }

    @Test
    void testFindById() {
        Teacher teacher = getTeacherDummy();
        when(teacherRepository.findById("1")).thenReturn(Optional.of(teacher));
        assertEquals(teacher, teacherService.findById("1"));
        when(teacherRepository.findById("2")).thenReturn(Optional.empty());
        assertNull(teacherService.findById("2"));
    }

    @Test
    void testUpdate() {
        Teacher teacher = getTeacherDummy();
        when(teacherRepository.save(teacher)).thenReturn(teacher);
        assertEquals(teacher, teacherService.update(teacher));
    }

    @Test
    void testDelete() {
        teacherService.delete("1");
        verify(teacherRepository).deleteById("1");
    }

    @Test
    void testFindByDepartment() {
        List<Teacher> list = List.of(getTeacherDummy());
        when(teacherRepository.findByDepartment("Department")).thenReturn(list);
        assertEquals(list, teacherService.findByDepartment("Department"));
    }

    @Test
    void testFindByName() {
        List<Teacher> list = List.of(getTeacherDummy());
        when(teacherRepository.findByName("John")).thenReturn(list);
        assertEquals(list, teacherService.findByName("John"));
    }

    @Test
    void testFindByLastName() {
        List<Teacher> list = List.of(getTeacherDummy());
        when(teacherRepository.findByLastName("Perez")).thenReturn(list);
        assertEquals(list, teacherService.findByLastName("Perez"));
    }
}
