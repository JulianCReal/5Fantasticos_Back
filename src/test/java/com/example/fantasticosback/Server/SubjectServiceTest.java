package com.example.fantasticosback.Server;

import com.example.fantasticosback.Model.Subject;
import com.example.fantasticosback.Repository.SubjectRepository;
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
class SubjectServiceTest {
    @Mock
    private SubjectRepository subjectRepository;
    @InjectMocks
    private SubjectService subjectService;

    private Subject getSubjectDummy() {
        return new Subject("1", "Mathematics", 3, 1);
    }

    @Test
    void testSave() {
        Subject subject = getSubjectDummy();
        when(subjectRepository.save(subject)).thenReturn(subject);
        assertEquals(subject, subjectService.save(subject));
    }

    @Test
    void testFindAll() {
        List<Subject> list = List.of(getSubjectDummy());
        when(subjectRepository.findAll()).thenReturn(list);
        assertEquals(list, subjectService.findAll());
    }

    @Test
    void testFindById() {
        Subject subject = getSubjectDummy();
        when(subjectRepository.findById("1")).thenReturn(Optional.of(subject));
        assertEquals(subject, subjectService.findById("1"));
        when(subjectRepository.findById("2")).thenReturn(Optional.empty());
        assertNull(subjectService.findById("2"));
    }

    @Test
    void testUpdate() {
        Subject subject = getSubjectDummy();
        when(subjectRepository.save(subject)).thenReturn(subject);
        assertEquals(subject, subjectService.update(subject));
    }

    @Test
    void testDelete() {
        subjectService.delete("1");
        verify(subjectRepository).deleteById("1");
    }

    @Test
    void testFindByName() {
        List<Subject> list = List.of(getSubjectDummy());
        when(subjectRepository.findByName("Mathematics")).thenReturn(list);
        assertEquals(list, subjectService.findByName("Mathematics"));
    }

    @Test
    void testFindBySemester() {
        List<Subject> list = List.of(getSubjectDummy());
        when(subjectRepository.findBySemester(1)).thenReturn(list);
        assertEquals(list, subjectService.findBySemester(1));
    }

    @Test
    void testFindByCredits() {
        List<Subject> list = List.of(getSubjectDummy());
        when(subjectRepository.findByCredits(3)).thenReturn(list);
        assertEquals(list, subjectService.findByCredits(3));
    }
}
