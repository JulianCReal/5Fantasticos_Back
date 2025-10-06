package com.example.fantasticosback.Controller;

import com.example.fantasticosback.Model.Subject;
import com.example.fantasticosback.Server.SubjectService;
import com.example.fantasticosback.Dtos.SubjectDTO;
import com.example.fantasticosback.Dtos.ResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SubjectControllerTest {
    @Mock
    private SubjectService subjectService;

    @InjectMocks
    private SubjectController subjectController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private SubjectDTO createSubjectDTODummy() {
        return new SubjectDTO("1", "Mathematics", 4, 1, new ArrayList<>());
    }

    private Subject createSubjectDummy() {
        Subject subject = new Subject();
        subject.setCredits(4);
        return subject;
    }

    @Test
    void testCreate() {
        SubjectDTO dto = createSubjectDTODummy();
        Subject subject = createSubjectDummy();
        when(subjectService.fromDTO(dto)).thenReturn(subject);
        when(subjectService.save(subject)).thenReturn(subject);
        when(subjectService.toDTO(subject)).thenReturn(dto);
        ResponseEntity<ResponseDTO<SubjectDTO>> response = subjectController.create(dto);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Success", response.getBody().getStatus());
        assertEquals("Subject created successfully", response.getBody().getMessage());
        assertEquals(dto, response.getBody().getData());
    }

    @Test
    void testList() {
        SubjectDTO dto = createSubjectDTODummy();
        List<SubjectDTO> dtos = Collections.singletonList(dto);
        when(subjectService.findAll()).thenReturn(Collections.emptyList());
        when(subjectService.toDTOList(any())).thenReturn(dtos);
        ResponseEntity<ResponseDTO<List<SubjectDTO>>> response = subjectController.list();
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Success", response.getBody().getStatus());
        assertEquals("List of subjects", response.getBody().getMessage());
        assertEquals(dtos, response.getBody().getData());
    }

    @Test
    void testGet_Exists() {
        String id = "1";
        Subject subject = createSubjectDummy();
        SubjectDTO dto = createSubjectDTODummy();
        when(subjectService.findById(id)).thenReturn(subject);
        when(subjectService.toDTO(subject)).thenReturn(dto);
        ResponseEntity<ResponseDTO<SubjectDTO>> response = subjectController.get(id);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Success", response.getBody().getStatus());
        assertEquals("Subject found", response.getBody().getMessage());
        assertEquals(dto, response.getBody().getData());
    }

    @Test
    void testGet_NotExists() {
        String id = "1";
        when(subjectService.findById(id)).thenReturn(null);
        ResponseEntity<ResponseDTO<SubjectDTO>> response = subjectController.get(id);
        assertEquals(404, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Error", response.getBody().getStatus());
        assertEquals("Subject not found", response.getBody().getMessage());
        assertNull(response.getBody().getData());
    }

    @Test
    void testUpdateByCredits() {
        int credits = 4;
        SubjectDTO dto = createSubjectDTODummy();
        Subject subject = createSubjectDummy();
        List<Subject> subjects = Arrays.asList(subject);
        when(subjectService.findByCredits(credits)).thenReturn(subjects);
        when(subjectService.save(any(Subject.class))).thenReturn(subject);
        when(subjectService.toDTOList(subjects)).thenReturn(Collections.singletonList(dto));
        ResponseEntity<ResponseDTO<List<SubjectDTO>>> response = subjectController.updateByCredits(credits, dto);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Success", response.getBody().getStatus());
        assertEquals("Subjects updated by credits", response.getBody().getMessage());
    }

    @Test
    void testUpdateByCredits_NotFound() {
        int credits = 4;
        SubjectDTO dto = createSubjectDTODummy();
        when(subjectService.findByCredits(credits)).thenReturn(Collections.emptyList());
        ResponseEntity<ResponseDTO<List<SubjectDTO>>> response = subjectController.updateByCredits(credits, dto);
        assertEquals(404, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Error", response.getBody().getStatus());
        assertEquals("No subjects found with the specified credits", response.getBody().getMessage());
    }

    @Test
    void testDelete_Exists() {
        String id = "1";
        Subject subject = createSubjectDummy();
        when(subjectService.findById(id)).thenReturn(subject);
        doNothing().when(subjectService).delete(id);
        ResponseEntity<ResponseDTO<Void>> response = subjectController.delete(id);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Success", response.getBody().getStatus());
        assertEquals("Subject deleted successfully", response.getBody().getMessage());
        assertNull(response.getBody().getData());
    }


}
