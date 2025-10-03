package com.example.fantasticosback.Controller;

import com.example.fantasticosback.Server.ScheduleService;
import com.example.fantasticosback.enums.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(ScheduleController.class)
class ScheduleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ScheduleService scheduleService;

    private Map<String, Object> horarioDTO;

    @BeforeEach
    void setUp() {
        // Crear datos de prueba
        List<Map<String, Object>> clases = Arrays.asList(
            new HashMap<String, Object>() {{
                put("materia", "Cálculo I");
                put("codigoMateria", "1001");
                put("creditos", 4);
                put("grupoNumero", 1);
                put("profesorNombre", "María González");
                put("dia", "Lunes");
                put("horaInicio", "08:00");
                put("horaFin", "10:00");
                put("aula", "Aula 101");
            }},
            new HashMap<String, Object>() {{
                put("materia", "Cálculo I");
                put("codigoMateria", "1001");
                put("creditos", 4);
                put("grupoNumero", 1);
                put("profesorNombre", "María González");
                put("dia", "Miércoles");
                put("horaInicio", "08:00");
                put("horaFin", "10:00");
                put("aula", "Aula 101");
            }}
        );

        horarioDTO = new HashMap<>();
        horarioDTO.put("estudianteId", "E001");
        horarioDTO.put("estudianteNombre", "Juan Pérez");
        horarioDTO.put("carrera", "Ingeniería");
        horarioDTO.put("semestre", 5);
        horarioDTO.put("clases", clases);
    }

    @Test
    void testObtenerHorarioEstudiante_EstudianteRole_Success() throws Exception {
        // Arrange
        when(scheduleService.obtenerHorarioEstudiante("E001", "E001", UserRole.STUDENT))
            .thenReturn(horarioDTO);

        // Act & Assert
        mockMvc.perform(get("/api/horarios/estudiante/E001")
                .header("X-Usuario-Id", "E001")
                .header("X-Usuario-Rol", "STUDENT")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estudianteId").value("E001"))
                .andExpect(jsonPath("$.estudianteNombre").value("Juan Pérez"))
                .andExpect(jsonPath("$.carrera").value("Ingeniería"))
                .andExpect(jsonPath("$.semestre").value(5))
                .andExpect(jsonPath("$.clases").isArray())
                .andExpect(jsonPath("$.clases.length()").value(2))
                .andExpect(jsonPath("$.clases[0].materia").value("Cálculo I"))
                .andExpect(jsonPath("$.clases[0].profesorNombre").value("María González"))
                .andExpect(jsonPath("$.clases[0].dia").value("Lunes"))
                .andExpect(jsonPath("$.clases[0].horaInicio").value("08:00"))
                .andExpect(jsonPath("$.clases[0].horaFin").value("10:00"))
                .andExpect(jsonPath("$.clases[0].aula").value("Aula 101"));
    }

    @Test
    void testObtenerHorarioEstudiante_ProfesorRole_Success() throws Exception {
        // Arrange
        when(scheduleService.obtenerHorarioEstudiante("E001", "P001", UserRole.PROFESSOR))
            .thenReturn(horarioDTO);

        // Act & Assert
        mockMvc.perform(get("/api/horarios/estudiante/E001")
                .header("X-Usuario-Id", "P001")
                .header("X-Usuario-Rol", "PROFESSOR")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estudianteId").value("E001"));
    }

    @Test
    void testObtenerHorarioEstudiante_DecanaturaRole_Success() throws Exception {
        // Arrange
        when(scheduleService.obtenerHorarioEstudiante("E001", "D001", UserRole.DEANOFFICE))
            .thenReturn(horarioDTO);

        // Act & Assert
        mockMvc.perform(get("/api/horarios/estudiante/E001")
                .header("X-Usuario-Id", "D001")
                .header("X-Usuario-Rol", "DEANOFFICE")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estudianteId").value("E001"));
    }

    @Test
    void testObtenerHorarioEstudiante_InvalidRole_BadRequest() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/horarios/estudiante/E001")
                .header("X-Usuario-Id", "E001")
                .header("X-Usuario-Rol", "INVALID_ROLE")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Rol de usuario inválido")));
    }

    @Test
    void testObtenerHorarioEstudiante_EstudianteNoEncontrado_NotFound() throws Exception {
        // Arrange
        when(scheduleService.obtenerHorarioEstudiante("E999", "E999", UserRole.STUDENT))
            .thenThrow(new IllegalArgumentException("Estudiante no encontrado con ID: E999"));

        // Act & Assert
        mockMvc.perform(get("/api/horarios/estudiante/E999")
                .header("X-Usuario-Id", "E999")
                .header("X-Usuario-Rol", "STUDENT")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Estudiante no encontrado")));
    }

    @Test
    void testObtenerHorarioEstudiante_AccesoDenegado_Forbidden() throws Exception {
        // Arrange
        when(scheduleService.obtenerHorarioEstudiante("E002", "E001", UserRole.STUDENT))
            .thenThrow(new IllegalAccessException("Los estudiantes solo pueden consultar su propio horario"));

        // Act & Assert
        mockMvc.perform(get("/api/horarios/estudiante/E002")
                .header("X-Usuario-Id", "E001")
                .header("X-Usuario-Rol", "STUDENT")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Acceso denegado")));
    }

    @Test
    void testObtenerHorarioEstudiante_ErrorInterno_InternalServerError() throws Exception {
        // Arrange
        when(scheduleService.obtenerHorarioEstudiante("E001", "E001", UserRole.STUDENT))
            .thenThrow(new RuntimeException("Error de base de datos"));

        // Act & Assert
        mockMvc.perform(get("/api/horarios/estudiante/E001")
                .header("X-Usuario-Id", "E001")
                .header("X-Usuario-Rol", "STUDENT")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Error interno del servidor")));
    }

    @Test
    void testObtenerMiHorario_Success() throws Exception {
        // Arrange
        when(scheduleService.obtenerHorarioEstudiante("E001", "E001", UserRole.STUDENT))
            .thenReturn(horarioDTO);

        // Act & Assert
        mockMvc.perform(get("/api/horarios/mi-horario/E001")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estudianteId").value("E001"))
                .andExpect(jsonPath("$.estudianteNombre").value("Juan Pérez"))
                .andExpect(jsonPath("$.clases.length()").value(2));
    }

    @Test
    void testObtenerMiHorario_EstudianteNoEncontrado_NotFound() throws Exception {
        // Arrange
        when(scheduleService.obtenerHorarioEstudiante("E999", "E999", UserRole.STUDENT))
            .thenThrow(new IllegalArgumentException("Estudiante no encontrado con ID: E999"));

        // Act & Assert
        mockMvc.perform(get("/api/horarios/mi-horario/E999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Estudiante no encontrado")));
    }

    @Test
    void testObtenerMiHorario_ErrorInterno_InternalServerError() throws Exception {
        // Arrange
        when(scheduleService.obtenerHorarioEstudiante("E001", "E001", UserRole.STUDENT))
            .thenThrow(new RuntimeException("Error de base de datos"));

        // Act & Assert
        mockMvc.perform(get("/api/horarios/mi-horario/E001")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Error interno del servidor")));
    }

    @Test
    void testObtenerHorarioEstudiante_HeadersFaltantes_BadRequest() throws Exception {
        // Act & Assert - Sin headers
        mockMvc.perform(get("/api/horarios/estudiante/E001")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testObtenerHorarioEstudiante_SoloHeaderUsuarioId_BadRequest() throws Exception {
        // Act & Assert - Solo header X-Usuario-Id
        mockMvc.perform(get("/api/horarios/estudiante/E001")
                .header("X-Usuario-Id", "E001")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testObtenerHorarioEstudiante_SoloHeaderRol_BadRequest() throws Exception {
        // Act & Assert - Solo header X-Usuario-Rol
        mockMvc.perform(get("/api/horarios/estudiante/E001")
                .header("X-Usuario-Rol", "STUDENT")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}