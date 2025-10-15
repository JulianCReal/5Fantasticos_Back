/**package com.example.fantasticosback.Controller;

import com.example.fantasticosback.Model.Entities.Enrollment;
import com.example.fantasticosback.Model.Entities.Group;
import com.example.fantasticosback.Persistence.Server.EnrollmentService;
import com.example.fantasticosback.Persistence.Server.GroupService;
import com.example.fantasticosback.Exception.BusinessValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.containsString;

@SpringBootTest
@AutoConfigureMockMvc
class EnrollmentControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @SpyBean
    private EnrollmentService enrollmentService;

    @Autowired
    private GroupService groupService;

    @BeforeEach
    void setUp() {
        // Crear un grupo de prueba
        Group testGroup = new Group(1, 1, 30, true, null);
        groupService.createGroup(testGroup);
    }

    @Test
    void testEnrollStudentInGroup() throws Exception {
        Enrollment mockEnrollment = new Enrollment(
            "ENR-1",
            null,
            null,
            "ACTIVE",
            0.0
        );

        when(enrollmentService.enrollStudentInGroup("E001", 1, "2024-1"))
            .thenReturn(mockEnrollment);

        mockMvc.perform(post("/api/enrollments/students/E001/groups/1")
                .param("semester", "2024-1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ACTIVE"))
                .andExpect(jsonPath("$.finalGrade").value(0.0));
    }

    @Test
    void testGetStudentEnrollments() throws Exception {
        Enrollment mockEnrollment = new Enrollment(
            "ENR-1",
            null,
            null,
            "ACTIVE",
            0.0
        );

        when(enrollmentService.getEnrollmentsByStudentId("E001"))
            .thenReturn(Collections.singletonList(mockEnrollment));

        mockMvc.perform(get("/api/enrollments/students/E001")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("ACTIVE"))
                .andExpect(jsonPath("$[0].finalGrade").value(0.0));
    }

    @Test
    void testCancelEnrollment() throws Exception {
        doNothing().when(enrollmentService).cancelEnrollment("E001", "1");

        mockMvc.perform(delete("/api/enrollments/students/E001/enrollments/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testEnrollStudentWithScheduleConflict() throws Exception {
        when(enrollmentService.enrollStudentInGroup("E001", 2, "2024-1"))
            .thenThrow(new BusinessValidationException("Existe conflicto de horario con otras materias inscritas"));

        mockMvc.perform(post("/api/enrollments/students/E001/groups/2")
                .param("semester", "2024-1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("conflicto de horario")));
    }

    @Test
    void testEnrollStudentInFullGroup() throws Exception {
        when(enrollmentService.enrollStudentInGroup("E001", 3, "2024-1"))
            .thenThrow(new BusinessValidationException("El grupo ha alcanzado su capacidad máxima"));

        mockMvc.perform(post("/api/enrollments/students/E001/groups/3")
                .param("semester", "2024-1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("capacidad máxima")));
    }
}
*/