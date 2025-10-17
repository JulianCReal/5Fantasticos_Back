package com.example.fantasticosback.controller;

/*
import com.example.fantasticosback.service.ScheduleService;
import com.example.fantasticosback.enums.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ScheduleControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ScheduleService scheduleService;

    @InjectMocks
    private ScheduleController scheduleController;

    private Map<String, Object> scheduleDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(scheduleController).build();

        // Create test data
        List<Map<String, Object>> classes = Arrays.asList(
            new HashMap<>() {{
                put("subject", "Calculus I");
                put("subjectCode", "1001");
                put("credits", 4);
                put("groupNumber", 1);
                put("teacherName", "María González");
                put("day", "Monday");
                put("startTime", "08:00");
                put("endTime", "10:00");
                put("classroom", "Room 101");
            }},
            new HashMap<>() {{
                put("subject", "Physics I");
                put("subjectCode", "1002");
                put("credits", 3);
                put("groupNumber", 2);
                put("teacherName", "Carlos Ruiz");
                put("day", "Tuesday");
                put("startTime", "10:00");
                put("endTime", "12:00");
                put("classroom", "Room 102");
            }}
        );

        scheduleDTO = new HashMap<>();
        scheduleDTO.put("studentId", "E001");
        scheduleDTO.put("studentName", "Juan Pérez");
        scheduleDTO.put("career", "Engineering");
        scheduleDTO.put("semester", 1);
        scheduleDTO.put("classes", classes);
    }

    @Test
    void testGetStudentSchedule_AsStudent_Success() throws Exception {
        // Arrange
        when(scheduleService.getStudentSchedule("E001", "E001", UserRole.STUDENT))
                .thenReturn(scheduleDTO);

        // Act & Assert
        mockMvc.perform(get("/api/schedules/student/{studentId}", "E001")
                        .header("X-User-Id", "E001")
                        .header("X-User-Role", "STUDENT")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.studentId").value("E001"))
                .andExpect(jsonPath("$.studentName").value("Juan Pérez"))
                .andExpect(jsonPath("$.career").value("Engineering"))
                .andExpect(jsonPath("$.semester").value(1))
                .andExpect(jsonPath("$.classes").isArray())
                .andExpect(jsonPath("$.classes.length()").value(2))
                .andExpect(jsonPath("$.classes[0].subject").value("Calculus I"))
                .andExpect(jsonPath("$.classes[0].teacherName").value("María González"))
                .andExpect(jsonPath("$.classes[0].day").value("Monday"))
                .andExpect(jsonPath("$.classes[0].startTime").value("08:00"))
                .andExpect(jsonPath("$.classes[0].endTime").value("10:00"))
                .andExpect(jsonPath("$.classes[0].classroom").value("Room 101"));
    }

    @Test
    void testGetStudentSchedule_AsTeacher_Success() throws Exception {
        // Arrange
        when(scheduleService.getStudentSchedule("E001", "T001", UserRole.TEACHER))
                .thenReturn(scheduleDTO);

        // Act & Assert
        mockMvc.perform(get("/api/schedules/student/{studentId}", "E001")
                        .header("X-User-Id", "T001")
                        .header("X-User-Role", "TEACHER")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.studentId").value("E001"));
    }

    @Test
    void testGetStudentSchedule_AsDeanOffice_Success() throws Exception {
        // Arrange
        when(scheduleService.getStudentSchedule("E001", "D001", UserRole.DEAN_OFFICE))
                .thenReturn(scheduleDTO);

        // Act & Assert
        mockMvc.perform(get("/api/schedules/student/{studentId}", "E001")
                        .header("X-User-Id", "D001")
                        .header("X-User-Role", "DEAN_OFFICE")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.studentId").value("E001"));
    }

    @Test
    void testGetStudentSchedule_InvalidRole() throws Exception {
        // No need to mock the service since the controller handles invalid roles
        // before calling the service

        // Act & Assert
        mockMvc.perform(get("/api/schedules/student/{studentId}", "E001")
                        .header("X-User-Id", "E001")
                        .header("X-User-Role", "INVALID_ROLE")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Invalid user role")));
    }

    @Test
    void testGetStudentSchedule_StudentNotFound() throws Exception {
        // Arrange
        when(scheduleService.getStudentSchedule("E999", "E999", UserRole.STUDENT))
                .thenThrow(new IllegalArgumentException("Student not found with ID: E999"));

        // Act & Assert
        mockMvc.perform(get("/api/schedules/student/{studentId}", "E999")
                        .header("X-User-Id", "E999")
                        .header("X-User-Role", "STUDENT")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Student not found")));
    }

    @Test
    void testGetStudentSchedule_AccessDenied() throws Exception {
        // Arrange
        when(scheduleService.getStudentSchedule("E001", "E002", UserRole.STUDENT))
                .thenThrow(new IllegalAccessException("Students can only query their own schedule"));

        // Act & Assert
        mockMvc.perform(get("/api/schedules/student/{studentId}", "E001")
                        .header("X-User-Id", "E002")
                        .header("X-User-Role", "STUDENT")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Access denied")));
    }

    @Test
    void testGetMySchedule_Success() throws Exception {
        // Arrange
        when(scheduleService.getStudentSchedule("E001", "E001", UserRole.STUDENT))
                .thenReturn(scheduleDTO);

        // Act & Assert
        mockMvc.perform(get("/api/schedules/my-schedule/{studentId}", "E001")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.studentId").value("E001"))
                .andExpect(jsonPath("$.studentName").value("Juan Pérez"))
                .andExpect(jsonPath("$.classes.length()").value(2));
    }

    @Test
    void testGetMySchedule_StudentNotFound() throws Exception {
        // Arrange
        when(scheduleService.getStudentSchedule("E999", "E999", UserRole.STUDENT))
                .thenThrow(new IllegalArgumentException("Student not found with ID: E999"));

        // Act & Assert
        mockMvc.perform(get("/api/schedules/my-schedule/{studentId}", "E999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Student not found")));
    }

    @Test
    void testGetStudentSchedule_MissingHeaders_BadRequest() throws Exception {
        // Act & Assert - Without headers
        mockMvc.perform(get("/api/schedules/student/{studentId}", "E001")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetStudentSchedule_OnlyUserIdHeader_BadRequest() throws Exception {
        // Act & Assert - Only X-User-Id header
        mockMvc.perform(get("/api/schedules/student/{studentId}", "E001")
                        .header("X-User-Id", "E001")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetStudentSchedule_OnlyRoleHeader_BadRequest() throws Exception {
        // Act & Assert - Only X-User-Role header
        mockMvc.perform(get("/api/schedules/student/{studentId}", "E001")
                        .header("X-User-Role", "STUDENT")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
*/