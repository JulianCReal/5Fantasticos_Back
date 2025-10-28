package com.example.fantasticosback.service;

import com.example.fantasticosback.exception.ResourceNotFoundException;
import com.example.fantasticosback.exception.BusinessValidationException;
import com.example.fantasticosback.model.Document.Request;
import com.example.fantasticosback.model.Observers.RequestCreationObserver;
import com.example.fantasticosback.model.RequestStates.PendingState;
import com.example.fantasticosback.model.Validators.RequestValidators.*;
import com.example.fantasticosback.repository.RequestRepository;
import com.example.fantasticosback.enums.RequestPriority;
import com.example.fantasticosback.enums.RequestType;
import com.example.fantasticosback.enums.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RequestService {
    private final RequestRepository requestRepository;
    private final DeanOfficeService deanOfficeService;
    private final ArrayList<RequestCreationObserver> observerForCreation;
    private final HashMap<RequestType, RequestValidator> validatorsForRequest;

    @Autowired
    private ChangeGroupValidator changeGroupValidator;
    @Autowired
    private JoinGroupValidator joinGroupValidator;
    @Autowired
    private LeaveGroupValidator leaveGroupValidator;
    @Autowired
    private SpecialValidator specialValidator;

    public RequestService(RequestRepository requestRepository, DeanOfficeService deanOfficeService, ArrayList<RequestCreationObserver> observerForCreation) {
        this.requestRepository = requestRepository;
        this.deanOfficeService = deanOfficeService;
        this.observerForCreation = observerForCreation;
        this.validatorsForRequest = addValidators();
    }

    public Request save(Request request) {
        Request saved = requestRepository.save(request);
        for(RequestCreationObserver observer : observerForCreation){
            observer.notifyCreation(request);
        }
        return saved;
    }

    public List<Request> findAll() {
        return requestRepository.findAll();
    }

    public Request findById(String id) {
        return requestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Request", "id", id));
    }

    public void delete(String id) {
        // Verificar que el request existe antes de eliminar
        Request existing = findById(id);

        // Usar el patrón State para determinar si se puede eliminar
        if (!existing.getState().canBeDeleted()) {
            throw new BusinessValidationException(
                "Cannot delete request in '" + existing.getState().getStateName() +
                "' status. Only requests in 'Pending' status can be deleted."
            );
        }

        requestRepository.deleteById(id);
    }

    public Request update(Request request) {
        // Verificar que el request existe
        findById(request.getId());
        return requestRepository.save(request);
    }

    public Request updateAditionalData(String requestId, Object aditionalData) {
        Request existing = findById(requestId);
        existing.setAditionalData(aditionalData);
        return requestRepository.save(existing);
    }

    public List<Request> findByUserId(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            throw new BusinessValidationException("User ID cannot be null or empty");
        }
        return requestRepository.findByUserId(userId);
    }
    public List<Request> findByStateName(String stateName) {
        if (stateName == null || stateName.trim().isEmpty()) {
            throw new BusinessValidationException("State name cannot be null or empty");
        }
        return requestRepository.findByStateName(stateName);
    }
    public List<Request> findByRequestPriority(String requestPriority) {
        if (requestPriority == null || requestPriority.trim().isEmpty()) {
            throw new BusinessValidationException("Request priority cannot be null or empty");
        }

        RequestPriority priority;
        try {
            priority = RequestPriority.valueOf(requestPriority.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BusinessValidationException("Invalid request priority: " + requestPriority);
        }
        return requestRepository.findByRequestPriority(priority);
    }
    public List<Request> findByCreatorRole(String creatorRole) {
        if (creatorRole == null || creatorRole.trim().isEmpty()) {
            throw new BusinessValidationException("Creator role cannot be null or empty");
        }
        Role role;
        try{
            role = Role.valueOf(creatorRole.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BusinessValidationException("Invalid creator role: " + creatorRole);
        }
        return requestRepository.findByCreatorRole(role);
    }
    public List<Request> findByDeanOffice(String deanOffice) {
        if (deanOffice == null || deanOffice.trim().isEmpty()) {
            throw new BusinessValidationException("Dean office cannot be null or empty");
        }
        return requestRepository.findByDeanOffice(deanOffice);
    }
    public List<Request> findRequestsByOneStudent(String studentId) {
        if (studentId == null || studentId.trim().isEmpty()) {
            throw new BusinessValidationException("Student ID cannot be null or empty");
        }
        List<Request> byRole = requestRepository.findByCreatorRole(Role.STUDENT);
        return byRole.stream().filter(r -> r.getUserId().equals(studentId)).collect(Collectors.toList());
    }

    public List<Request> findStudentsRequestByDeanOffice(String deanOffice, String userId) {
        if (deanOffice == null || deanOffice.trim().isEmpty()) {
            throw new BusinessValidationException("Dean office cannot be null or empty");
        }
        List<String> students = deanOfficeService.getStudentsByFaculty(deanOffice);
        if(students.contains(userId)){
            return requestRepository.findStudentsRequestByDeanOffice(deanOffice, userId);
        }else{
            throw new BusinessValidationException("The student with id: " + userId + " is not registered in the dean office: " + deanOffice);
        }
    }

    public List<Request> findByRequestDate(LocalDate requestDate) {
        LocalDateTime startOfDay = requestDate.atStartOfDay();
        LocalDateTime endOfDay = requestDate.atTime(LocalTime.MAX);
        return requestRepository.findAllByRequestDateBetween(startOfDay, endOfDay);
    }

    private HashMap<RequestType, RequestValidator> addValidators(){
        HashMap<RequestType, RequestValidator> handlersForRequest = new HashMap<>();
        handlersForRequest.put(RequestType.CHANGE_GROUP, changeGroupValidator);
        handlersForRequest.put(RequestType.JOIN_GROUP, joinGroupValidator);
        handlersForRequest.put(RequestType.LEAVE_GROUP, leaveGroupValidator);
        handlersForRequest.put(RequestType.SPECIAL, specialValidator);
        return handlersForRequest;
    }
    private boolean processRequest(Request request, Role role) {
        RequestType type = request.getType();
        String currentState = request.getState().getStateName();

        // Solo valida si el estado es Pending
        if ("Pending".equals(currentState)) {
            RequestValidator validator = validatorsForRequest.get(type);
            if (validator == null) {
                throw new BusinessValidationException("No validator found for request type: " + type);
            }
            boolean valid = validator.response(request);
            // Luego cambia de estado a In Progress como dictan tus reglas
            request.getState().changeState(request, role);
            // Actualiza el resto (guardar, setHistoryResponses…)
            requestRepository.save(request);
            return valid;
        }

        // Si el estado ya es In Progress, simplemente permite seguir la transición
        if ("In Progress".equals(currentState)) {
            // No se valida, solo cambia el estado según la acción
            request.getState().changeState(request, role);
            requestRepository.save(request);
            return true;
        }

        // Otros estados, como Approved o Rejected, tal vez no permiten más transición
        throw new BusinessValidationException("Request can't be processed in state: " + currentState);
    }
    public void answerRequest(Request request,String message,Boolean answer, Role role){
        request.setRequestResponseTime(LocalDateTime.now());
        System.out.println("responseMessage: " + message);
        System.out.println("status: " + answer);
        boolean approved = processRequest(request, role) && answer;
        request.setEvaluationApproved(approved);
        request.getState().changeState(request, role);
        request.setResponseMessage(message);
        request.setHistoryResponses("Request moved to " + request.getState().getStateName() + " by " + role + " on " + request.getRequestResponseTime());
        requestRepository.save(request);
    }

    public HashMap<String, String> getRequestHistoryResponses(String requestId) {
        Request request = findById(requestId);
        return request.getHistoryResponses();
    }
}
