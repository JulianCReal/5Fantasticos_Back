package com.example.fantasticosback.Controller;

import com.example.fantasticosback.Server.HorarioService;
import com.example.fantasticosback.enums.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/horarios")
public class HorarioController {

    private static final Logger log = Logger.getLogger(HorarioController.class.getName());

    @Autowired
    private HorarioService horarioService;


    @GetMapping("/estudiante/{estudianteId}")
    public ResponseEntity<?> obtenerHorarioEstudiante(
            @PathVariable String estudianteId,
            @RequestHeader("X-Usuario-Id") String usuarioId,
            @RequestHeader("X-Usuario-Rol") String userRoleStr) {
        
        try {

            UserRole userRole;
            try {
                userRole = UserRole.valueOf(userRoleStr.toUpperCase());
            } catch (IllegalArgumentException e) {
                log.warning("Rol de usuario inválido: " + userRoleStr);
                return ResponseEntity.badRequest()
                    .body("Rol de usuario inválido. Roles válidos: ESTUDIANTE, PROFESOR, DECANATURA");
            }

            log.info("Consulta de horario - Estudiante: " + estudianteId + 
                    ", Usuario: " + usuarioId + ", Rol: " + userRole);

            Map<String, Object> horario = horarioService.obtenerHorarioEstudiante(estudianteId, usuarioId, userRole);
            return ResponseEntity.ok(horario);

        } catch (IllegalArgumentException e) {
            log.warning("Argumento inválido: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Error: " + e.getMessage());
        } catch (IllegalAccessException e) {
            log.warning("Acceso denegado: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body("Acceso denegado: " + e.getMessage());
        } catch (Exception e) {
            log.severe("Error interno al consultar horario: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error interno del servidor");
        }
    }



    @GetMapping("/mi-horario/{estudianteId}")
    public ResponseEntity<?> obtenerMiHorario(@PathVariable String estudianteId) {
        try {
            log.info("Consulta de horario propio - Estudiante: " + estudianteId);
            
            Map<String, Object> horario = horarioService.obtenerHorarioEstudiante(
                estudianteId, estudianteId, UserRole.ESTUDIANTE);
            return ResponseEntity.ok(horario);

        } catch (IllegalArgumentException e) {
            log.warning("Estudiante no encontrado: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Error: " + e.getMessage());
        } catch (Exception e) {
            log.severe("Error interno al consultar horario: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error interno del servidor");
        }
    }
}