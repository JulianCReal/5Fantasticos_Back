package com.example.fantasticosback.model.Observers;

import com.example.fantasticosback.dto.response.GroupCapacityAlertDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implementación concreta del Observer que gestiona las alertas de capacidad
 */
@Component
public class GroupCapacityAlertManager implements GroupCapacityObserver {

    private static final Logger logger = LoggerFactory.getLogger(GroupCapacityAlertManager.class);

    private final ConcurrentHashMap<String, GroupCapacityAlertDTO> activeAlerts = new ConcurrentHashMap<>();

    @Override
    public void onCapacityThresholdReached(String groupId, String subjectCode, String subjectName,
                                         int groupNumber, int currentCapacity, int maxCapacity,
                                         double occupancyPercentage) {

        String alertId = generateAlertId(groupId);
        String alertLevel = determineAlertLevel(occupancyPercentage);
        String message = generateAlertMessage(subjectCode, groupNumber, occupancyPercentage, alertLevel);

        GroupCapacityAlertDTO alert = new GroupCapacityAlertDTO(
            alertId,
            groupId,
            subjectCode,
            subjectName,
            groupNumber,
            currentCapacity,
            maxCapacity,
            occupancyPercentage,
            alertLevel,
            message,
            LocalDateTime.now(),
            false // No reconocida inicialmente
        );

        activeAlerts.put(alertId, alert);

        if ("CRITICAL".equals(alertLevel)) {
            logger.error("ALERTA CRÍTICA DE CAPACIDAD - GroupId: {}, Subject: {}, Group: {}, Occupancy: {}%, Message: {}",
                        groupId, subjectCode, groupNumber, occupancyPercentage, message);
        } else {
            logger.warn("ALERTA DE CAPACIDAD - GroupId: {}, Subject: {}, Group: {}, Occupancy: {}%, Message: {}",
                       groupId, subjectCode, groupNumber, occupancyPercentage, message);
        }
    }

    /**
     * Obtiene todas las alertas activas
     */
    public List<GroupCapacityAlertDTO> getActiveAlerts() {
        return new ArrayList<>(activeAlerts.values());
    }

    /**
     * Obtiene alertas no reconocidas
     */
    public List<GroupCapacityAlertDTO> getUnacknowledgedAlerts() {
        return activeAlerts.values().stream()
                .filter(alert -> !alert.isAcknowledged())
                .toList();
    }

    /**
     * Marca una alerta como reconocida
     */
    public boolean acknowledgeAlert(String alertId) {
        GroupCapacityAlertDTO alert = activeAlerts.get(alertId);
        if (alert != null) {
            alert.setAcknowledged(true);
            return true;
        }
        return false;
    }

    /**
     * Elimina una alerta
     */
    public boolean dismissAlert(String alertId) {
        return activeAlerts.remove(alertId) != null;
    }

    /**
     * Limpia alertas reconocidas más antiguas de X horas
     */
    public void cleanupOldAlerts(int hoursOld) {
        LocalDateTime cutoff = LocalDateTime.now().minusHours(hoursOld);
        activeAlerts.entrySet().removeIf(entry ->
            entry.getValue().isAcknowledged() &&
            entry.getValue().getTimestamp().isBefore(cutoff)
        );
    }

    private String generateAlertId(String groupId) {
        return "ALERT_" + groupId + "_" + UUID.randomUUID().toString().substring(0, 8);
    }

    private String determineAlertLevel(double occupancyPercentage) {
        if (occupancyPercentage >= 95.0) {
            return "CRITICAL";
        } else if (occupancyPercentage >= 90.0) {
            return "WARNING";
        }
        return "INFO";
    }

    private String generateAlertMessage(String subjectCode, int groupNumber,
                                      double occupancyPercentage, String alertLevel) {
        if ("CRITICAL".equals(alertLevel)) {
            return String.format("CRÍTICO: El grupo %d de %s está al %.1f%% de capacidad. Acción inmediata requerida.",
                               groupNumber, subjectCode, occupancyPercentage);
        } else {
            return String.format("ADVERTENCIA: El grupo %d de %s está al %.1f%% de capacidad. Considere abrir nuevo grupo.",
                               groupNumber, subjectCode, occupancyPercentage);
        }
    }
}
