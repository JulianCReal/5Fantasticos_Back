package com.example.fantasticosback.service;

import com.example.fantasticosback.dto.response.GroupCapacityAlertDTO;
import com.example.fantasticosback.model.Document.Group;
import com.example.fantasticosback.model.Document.Subject;
import com.example.fantasticosback.model.Observers.GroupCapacityObserver;
import com.example.fantasticosback.model.Observers.GroupCapacitySubject;
import com.example.fantasticosback.model.Observers.GroupCapacityAlertManager;
import com.example.fantasticosback.repository.GroupRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class GroupCapacityMonitoringService implements GroupCapacitySubject {

    private final List<GroupCapacityObserver> observers = new ArrayList<>();
    private final GroupRepository groupRepository;
    private final SubjectService subjectService;
    private final GroupCapacityAlertManager alertManager;

    // Umbral por defecto del 90%
    private static final double DEFAULT_THRESHOLD = 90.0;

    public GroupCapacityMonitoringService(GroupRepository groupRepository,
                                        SubjectService subjectService,
                                        GroupCapacityAlertManager alertManager) {
        this.groupRepository = groupRepository;
        this.subjectService = subjectService;
        this.alertManager = alertManager;

        // Auto-registrar el AlertManager como observer
        addObserver(alertManager);
    }

    @Override
    public void addObserver(GroupCapacityObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(GroupCapacityObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(String groupId, String subjectCode, String subjectName,
                              int groupNumber, int currentCapacity, int maxCapacity,
                              double occupancyPercentage) {
        for (GroupCapacityObserver observer : observers) {
            observer.onCapacityThresholdReached(groupId, subjectCode, subjectName,
                                              groupNumber, currentCapacity, maxCapacity,
                                              occupancyPercentage);
        }
    }

    @Override
    public void checkGroupCapacity(String groupId) {
        Group group = groupRepository.findById(groupId).orElse(null);
        if (group == null) return;

        Subject subject = subjectService.findById(group.getSubjectId());
        if (subject == null) return;

        evaluateGroupCapacity(group, subject);
    }

    @Override
    public void checkSubjectGroupsCapacity(String subjectCode) {
        Subject subject = subjectService.findAll().stream()
                .filter(s -> s.getCode().equalsIgnoreCase(subjectCode))
                .findFirst()
                .orElse(null);

        if (subject == null) return;

        List<Group> groups = groupRepository.findBySubjectId(subject.getSubjectId());

        for (Group group : groups) {
            evaluateGroupCapacity(group, subject);
        }
    }


    public void monitorAllGroups() {
        List<Group> allGroups = groupRepository.findAll();

        for (Group group : allGroups) {
            Subject subject = subjectService.findById(group.getSubjectId());
            if (subject != null) {
                evaluateGroupCapacity(group, subject);
            }
        }
    }

    public void monitorGroupsWithThreshold(double threshold) {
        List<Group> allGroups = groupRepository.findAll();

        for (Group group : allGroups) {
            Subject subject = subjectService.findById(group.getSubjectId());
            if (subject != null) {
                evaluateGroupCapacityWithThreshold(group, subject, threshold);
            }
        }
    }

    public List<GroupCapacityAlertDTO> getCapacityReport() {
        return alertManager.getActiveAlerts();
    }


    public List<GroupCapacityAlertDTO> getUnacknowledgedAlerts() {
        return alertManager.getUnacknowledgedAlerts();
    }


    public boolean acknowledgeAlert(String alertId) {
        return alertManager.acknowledgeAlert(alertId);
    }


    public boolean dismissAlert(String alertId) {
        return alertManager.dismissAlert(alertId);
    }

    private void evaluateGroupCapacity(Group group, Subject subject) {
        evaluateGroupCapacityWithThreshold(group, subject, DEFAULT_THRESHOLD);
    }

    private void evaluateGroupCapacityWithThreshold(Group group, Subject subject, double threshold) {
        if (group.getCapacity() <= 0) return;

        int enrolledStudents = group.getGroupStudents() != null ? group.getGroupStudents().size() : 0;
        double occupancyPercentage = (double) enrolledStudents / group.getCapacity() * 100;

        // Notificar si se alcanza el umbral
        if (occupancyPercentage >= threshold) {
            notifyObservers(
                group.getId(),
                subject.getCode(),
                subject.getName(),
                group.getNumber(),
                enrolledStudents,
                group.getCapacity(),
                occupancyPercentage
            );
        }
    }
}
