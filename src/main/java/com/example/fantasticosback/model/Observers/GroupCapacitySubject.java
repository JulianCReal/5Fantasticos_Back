package com.example.fantasticosback.model.Observers;

/**
 * Interface para el sujeto observable que notifica cambios en la capacidad de grupos
 */
public interface GroupCapacitySubject {


    void addObserver(GroupCapacityObserver observer);

    void removeObserver(GroupCapacityObserver observer);


    void notifyObservers(String groupId, String subjectCode, String subjectName,
                        int groupNumber, int currentCapacity, int maxCapacity,
                        double occupancyPercentage);

    void checkGroupCapacity(String groupId);

    void checkSubjectGroupsCapacity(String subjectCode);
}
