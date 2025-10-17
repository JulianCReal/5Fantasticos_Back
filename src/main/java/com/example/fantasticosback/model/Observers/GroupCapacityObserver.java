package com.example.fantasticosback.model.Observers;

/**
 * Interface para observadores que monitorean la capacidad de los grupos
 */
public interface GroupCapacityObserver {

    /**
     * Método llamado cuando un grupo alcanza el límite de capacidad especificado
     * @param groupId ID del grupo
     * @param subjectCode Código de la materia
     * @param subjectName Nombre de la materia
     * @param groupNumber Número del grupo
     * @param currentCapacity Capacidad actual
     * @param maxCapacity Capacidad máxima
     * @param occupancyPercentage Porcentaje de ocupación
     */
    void onCapacityThresholdReached(String groupId, String subjectCode, String subjectName,
                                  int groupNumber, int currentCapacity, int maxCapacity,
                                  double occupancyPercentage);
}
