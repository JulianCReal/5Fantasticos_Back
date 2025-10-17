package com.example.fantasticosback.controller;

import com.example.fantasticosback.dto.response.GroupCapacityAlertDTO;
import com.example.fantasticosback.dto.response.ResponseDTO;
import com.example.fantasticosback.service.GroupCapacityMonitoringService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "monitoring-controller", description = "Monitoreo Automático de Capacidad de Grupos")
@RestController
@RequestMapping("/api/monitoring")
@CrossOrigin(origins = "*")
public class GroupCapacityMonitoringController {

    private final GroupCapacityMonitoringService monitoringService;

    public GroupCapacityMonitoringController(GroupCapacityMonitoringService monitoringService) {
        this.monitoringService = monitoringService;
    }

    @Operation(summary = "Ejecutar monitoreo completo del sistema",
            description = "Revisa la capacidad de todos los grupos y genera alertas para aquellos que superen el 90% de ocupación")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Monitoreo ejecutado exitosamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/run-full-monitoring")
    public ResponseEntity<ResponseDTO<String>> runFullMonitoring() {
        monitoringService.monitorAllGroups();
        return ResponseEntity.ok(ResponseDTO.success(
            "Monitoreo completo ejecutado",
            "Se ha revisado la capacidad de todos los grupos del sistema"));
    }

    @Operation(summary = "Monitorear grupos con umbral personalizado",
            description = "Ejecuta monitoreo con un porcentaje de umbral específico (ej: 85% en lugar del 90% por defecto)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Monitoreo con umbral personalizado ejecutado"),
        @ApiResponse(responseCode = "400", description = "Umbral inválido (debe estar entre 50 y 100)")
    })
    @PostMapping("/run-monitoring")
    public ResponseEntity<ResponseDTO<String>> runMonitoringWithThreshold(
            @Parameter(description = "Porcentaje de umbral (50-100)") @RequestParam double threshold) {

        if (threshold < 50 || threshold > 100) {
            return ResponseEntity.badRequest().body(ResponseDTO.error(
                "El umbral debe estar entre 50% y 100%"));
        }

        monitoringService.monitorGroupsWithThreshold(threshold);
        return ResponseEntity.ok(ResponseDTO.success(
            "Monitoreo con umbral " + threshold + "% ejecutado",
            "Se ha revisado la capacidad usando el umbral personalizado"));
    }

    @Operation(summary = "Monitorear grupos de una materia específica",
            description = "Revisa la capacidad únicamente de los grupos de una materia particular")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Monitoreo de materia ejecutado"),
        @ApiResponse(responseCode = "404", description = "Materia no encontrada")
    })
    @PostMapping("/monitor-subject/{subjectCode}")
    public ResponseEntity<ResponseDTO<String>> monitorSubjectGroups(
            @Parameter(description = "Código de la materia (ej: CALD)") @PathVariable String subjectCode) {

        monitoringService.checkSubjectGroupsCapacity(subjectCode);
        return ResponseEntity.ok(ResponseDTO.success(
            "Monitoreo de " + subjectCode + " ejecutado",
            "Se ha revisado la capacidad de todos los grupos de la materia " + subjectCode));
    }

    @Operation(summary = "Monitorear grupo específico",
            description = "Revisa la capacidad de un grupo individual y genera alerta si es necesario")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Monitoreo de grupo ejecutado"),
        @ApiResponse(responseCode = "404", description = "Grupo no encontrado")
    })
    @PostMapping("/monitor-group/{groupId}")
    public ResponseEntity<ResponseDTO<String>> monitorSpecificGroup(
            @Parameter(description = "ID del grupo") @PathVariable String groupId) {

        monitoringService.checkGroupCapacity(groupId);
        return ResponseEntity.ok(ResponseDTO.success(
            "Monitoreo del grupo ejecutado",
            "Se ha revisado la capacidad del grupo especificado"));
    }

    @Operation(summary = "Obtener todas las alertas activas",
            description = "Consulta todas las alertas de capacidad generadas por el sistema de monitoreo")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Alertas obtenidas exitosamente")
    })
    @GetMapping("/alerts")
    public ResponseEntity<ResponseDTO<List<GroupCapacityAlertDTO>>> getAllAlerts() {
        List<GroupCapacityAlertDTO> alerts = monitoringService.getCapacityReport();
        return ResponseEntity.ok(ResponseDTO.success(alerts,
            "Alertas de capacidad obtenidas exitosamente"));
    }

    @Operation(summary = "Obtener alertas no reconocidas",
            description = "Consulta únicamente las alertas que no han sido vistas o reconocidas por el coordinador")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Alertas no reconocidas obtenidas exitosamente")
    })
    @GetMapping("/alerts/unacknowledged")
    public ResponseEntity<ResponseDTO<List<GroupCapacityAlertDTO>>> getUnacknowledgedAlerts() {
        List<GroupCapacityAlertDTO> alerts = monitoringService.getUnacknowledgedAlerts();
        return ResponseEntity.ok(ResponseDTO.success(alerts,
            "Alertas no reconocidas obtenidas exitosamente"));
    }

    @Operation(summary = "Reconocer una alerta",
            description = "Marca una alerta específica como vista/reconocida por el coordinador académico")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Alerta reconocida exitosamente"),
        @ApiResponse(responseCode = "404", description = "Alerta no encontrada")
    })
    @PutMapping("/alerts/{alertId}/acknowledge")
    public ResponseEntity<ResponseDTO<String>> acknowledgeAlert(
            @Parameter(description = "ID de la alerta") @PathVariable String alertId) {

        boolean acknowledged = monitoringService.acknowledgeAlert(alertId);
        if (acknowledged) {
            return ResponseEntity.ok(ResponseDTO.success(
                "Alerta reconocida",
                "La alerta ha sido marcada como vista"));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Descartar una alerta",
            description = "Elimina permanentemente una alerta del sistema de monitoreo")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Alerta descartada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Alerta no encontrada")
    })
    @DeleteMapping("/alerts/{alertId}")
    public ResponseEntity<ResponseDTO<String>> dismissAlert(
            @Parameter(description = "ID de la alerta") @PathVariable String alertId) {

        boolean dismissed = monitoringService.dismissAlert(alertId);
        if (dismissed) {
            return ResponseEntity.ok(ResponseDTO.success(
                "Alerta descartada",
                "La alerta ha sido eliminada del sistema"));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
