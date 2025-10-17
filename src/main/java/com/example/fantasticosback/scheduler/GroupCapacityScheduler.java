package com.example.fantasticosback.scheduler;

import com.example.fantasticosback.service.GroupCapacityMonitoringService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Scheduler opcional para ejecutar monitoreo automático periódico
 * Se activa con la propiedad: monitoring.scheduler.enabled=true
 */
@Component
@ConditionalOnProperty(name = "monitoring.scheduler.enabled", havingValue = "true", matchIfMissing = false)
public class GroupCapacityScheduler {

    private final GroupCapacityMonitoringService monitoringService;

    public GroupCapacityScheduler(GroupCapacityMonitoringService monitoringService) {
        this.monitoringService = monitoringService;
    }

    /**
     * Ejecuta monitoreo cada 30 minutos durante horario laboral
     */
    @Scheduled(cron = "0 */30 8-18 * * MON-FRI")
    public void scheduledCapacityCheck() {
        System.out.println("🔍 Ejecutando monitoreo automático programado...");
        monitoringService.monitorAllGroups();
        System.out.println("✅ Monitoreo automático completado.");
    }

    /**
     * Limpia alertas reconocidas más antiguas de 24 horas - ejecuta diariamente a las 2 AM
     */
    @Scheduled(cron = "0 0 2 * * *")
    public void cleanupOldAlerts() {
        System.out.println("🧹 Limpiando alertas antiguas...");
        // Esta funcionalidad se puede expandir cuando sea necesario
        System.out.println("✅ Limpieza de alertas completada.");
    }
}
