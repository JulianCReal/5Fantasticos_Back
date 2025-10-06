package com.example.fantasticosback.Controller;

import com.example.fantasticosback.Model.Subject;
import com.example.fantasticosback.Server.SubjectService;
import com.example.fantasticosback.Dtos.SubjectDTO;
import com.example.fantasticosback.Dtos.CreateGroupDTO;
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
        return new SubjectDTO("101", "Cálculo Diferencial", 3, 1);
    }

    private Subject createSubjectDummy() {
        Subject subject = new Subject("101", "Cálculo Diferencial", 3, 1);
        return subject;
    }

    private CreateGroupDTO createGroupDTODummy() {
        return new CreateGroupDTO(1, 30, true, "teacher123");
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
        assertEquals("List of predefined subjects", response.getBody().getMessage());
        assertEquals(dtos, response.getBody().getData());
    }

    @Test
    void testGet_Exists() {
        String id = "101";
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
        String id = "999";
        when(subjectService.findById(id)).thenReturn(null);

        ResponseEntity<ResponseDTO<SubjectDTO>> response = subjectController.get(id);

        assertEquals(404, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Error", response.getBody().getStatus());
        assertEquals("Subject not found", response.getBody().getMessage());
        assertNull(response.getBody().getData());
    }

    @Test
    void testGetBySemester() {
        int semester = 1;
        SubjectDTO dto = createSubjectDTODummy();
        List<SubjectDTO> dtos = Collections.singletonList(dto);
        when(subjectService.findBySemester(semester)).thenReturn(Collections.emptyList());
        when(subjectService.toDTOList(any())).thenReturn(dtos);

        ResponseEntity<ResponseDTO<List<SubjectDTO>>> response = subjectController.getBySemester(semester);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Success", response.getBody().getStatus());
        assertEquals("Subjects by semester", response.getBody().getMessage());
        assertEquals(dtos, response.getBody().getData());
    }

    @Test
    void testGetByName() {
        String name = "Cálculo";
        SubjectDTO dto = createSubjectDTODummy();
        List<SubjectDTO> dtos = Collections.singletonList(dto);
        when(subjectService.findByName(name)).thenReturn(Collections.emptyList());
        when(subjectService.toDTOList(any())).thenReturn(dtos);

        ResponseEntity<ResponseDTO<List<SubjectDTO>>> response = subjectController.getByName(name);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Success", response.getBody().getStatus());
        assertEquals("Subjects by name", response.getBody().getMessage());
        assertEquals(dtos, response.getBody().getData());
    }

    @Test
    void testAddGroupToSubject_Success() {
        String subjectCode = "CALD";
        CreateGroupDTO groupDto = createGroupDTODummy();
        when(subjectService.addGroupToSubjectByCode(subjectCode, groupDto)).thenReturn(true);

        ResponseEntity<ResponseDTO<String>> response = subjectController.addGroupToSubject(subjectCode, groupDto);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Success", response.getBody().getStatus());
        assertEquals("Group added to subject " + subjectCode, response.getBody().getMessage());
        assertEquals("Group added successfully", response.getBody().getData());
    }

    @Test
    void testAddGroupToSubject_SubjectNotFound() {
        String subjectCode = "INVALID";
        CreateGroupDTO groupDto = createGroupDTODummy();
        when(subjectService.addGroupToSubjectByCode(subjectCode, groupDto))
            .thenThrow(new IllegalArgumentException("Subject not found in catalog with code: " + subjectCode));

        ResponseEntity<ResponseDTO<String>> response = subjectController.addGroupToSubject(subjectCode, groupDto);

        assertEquals(404, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Error", response.getBody().getStatus());
        assertTrue(response.getBody().getMessage().contains("Subject not found"));
    }

    @Test
    void testAddGroupToSubject_Failed() {
        String subjectCode = "CALD";
        CreateGroupDTO groupDto = createGroupDTODummy();
        when(subjectService.addGroupToSubjectByCode(subjectCode, groupDto)).thenReturn(false);

        ResponseEntity<ResponseDTO<String>> response = subjectController.addGroupToSubject(subjectCode, groupDto);

        assertEquals(400, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Error", response.getBody().getStatus());
        assertEquals("Failed to add group to subject", response.getBody().getMessage());
    }

    @Test
    void testGetSubjectGroups_Success() {
        String subjectCode = "CALD";
        List<Object> groups = Arrays.asList(
            java.util.Map.of("groupId", 1, "groupNumber", 1, "capacity", 30)
        );
        when(subjectService.getGroupsBySubjectCode(subjectCode)).thenReturn(groups);

        ResponseEntity<ResponseDTO<List<Object>>> response = subjectController.getSubjectGroups(subjectCode);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Success", response.getBody().getStatus());
        assertEquals("Groups retrieved for " + subjectCode, response.getBody().getMessage());
        assertEquals(groups, response.getBody().getData());
    }

    @Test
    void testGetSubjectGroups_SubjectNotFound() {
        String subjectCode = "INVALID";
        when(subjectService.getGroupsBySubjectCode(subjectCode))
            .thenThrow(new IllegalArgumentException("Subject not found in catalog with code: " + subjectCode));

        ResponseEntity<ResponseDTO<List<Object>>> response = subjectController.getSubjectGroups(subjectCode);

        assertEquals(404, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("Error", response.getBody().getStatus());
        assertTrue(response.getBody().getMessage().contains("Subject not found"));
    }
}
