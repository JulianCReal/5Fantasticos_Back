package com.example.fantasticosback.controller;

import com.example.fantasticosback.dto.response.TeacherDTO;
import com.example.fantasticosback.exception.BusinessValidationException;
import com.example.fantasticosback.exception.ResourceNotFoundException;
import com.example.fantasticosback.mapper.TeacherMapper;
import com.example.fantasticosback.model.Document.Teacher;
import com.example.fantasticosback.repository.TeacherRepository;
import com.example.fantasticosback.service.TeacherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TeacherServiceTest {

    @Mock
    private TeacherRepository teacherRepository;

    @Mock
    private TeacherMapper teacherMapper;

    @InjectMocks
    private TeacherService teacherService;

    private Teacher teacher;
    private TeacherDTO teacherDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        teacher = new Teacher();
        teacher.setId("1");
        teacher.setName("Juan");
        teacher.setLastName("Pérez");
        teacher.setDocument("12345");
        teacher.setDepartment("Math");

        teacherDTO = new TeacherDTO();
        teacherDTO.setId("1");
        teacherDTO.setName("Juan");
        teacherDTO.setLastName("Pérez");
        teacherDTO.setDocument("12345");
        teacherDTO.setDepartment("Math");
    }

    @Test
    void save_ShouldSaveTeacher_WhenValid() {
        when(teacherMapper.fromDTO(any(TeacherDTO.class))).thenReturn(teacher);
        when(teacherRepository.findByDocument("12345")).thenReturn(null);
        when(teacherRepository.save(any(Teacher.class))).thenReturn(teacher);
        when(teacherMapper.fromDocument(any(Teacher.class))).thenReturn(teacherDTO);

        TeacherDTO result = teacherService.save(teacherDTO);

        assertNotNull(result);
        assertEquals("Juan", result.getName());
        verify(teacherRepository, times(1)).save(any(Teacher.class));
    }

    @Test
    void save_ShouldThrow_WhenDuplicateDocument() {
        when(teacherMapper.fromDTO(any(TeacherDTO.class))).thenReturn(teacher);
        when(teacherRepository.findByDocument("12345")).thenReturn(teacher);

        assertThrows(BusinessValidationException.class, () -> teacherService.save(teacherDTO));
    }

    @Test
    void findById_ShouldReturnTeacher_WhenExists() {
        when(teacherRepository.findById("1")).thenReturn(Optional.of(teacher));
        when(teacherMapper.fromDocument(teacher)).thenReturn(teacherDTO);

        TeacherDTO result = teacherService.findById("1");

        assertEquals("Juan", result.getName());
        verify(teacherRepository).findById("1");
    }

    @Test
    void findById_ShouldThrow_WhenNotFound() {
        when(teacherRepository.findById("1")).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> teacherService.findById("1"));
    }

    @Test
    void findByDepartment_ShouldReturnList_WhenExists() {
        when(teacherRepository.findByDepartment("Math")).thenReturn(List.of(teacher));
        when(teacherMapper.fromDocument(teacher)).thenReturn(teacherDTO);

        List<TeacherDTO> result = teacherService.findByDepartment("Math");

        assertEquals(1, result.size());
        verify(teacherRepository).findByDepartment("Math");
    }

    @Test
    void findByDepartment_ShouldThrow_WhenEmpty() {
        when(teacherRepository.findByDepartment("History")).thenReturn(List.of());
        assertThrows(ResourceNotFoundException.class, () -> teacherService.findByDepartment("History"));
    }

    @Test
    void delete_ShouldDeleteTeacher_WhenExists() {
        when(teacherRepository.findById("1")).thenReturn(Optional.of(teacher));
        doNothing().when(teacherRepository).deleteById("1");

        teacherService.delete("1");

        verify(teacherRepository).deleteById("1");
    }

    @Test
    void delete_ShouldThrow_WhenNotFound() {
        when(teacherRepository.findById("1")).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> teacherService.delete("1"));
    }
}
