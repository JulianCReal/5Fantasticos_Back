package com.example.fantasticosback.Persistence.Service;

import com.example.fantasticosback.Dtos.RequestDTO;
import com.example.fantasticosback.Exception.ResourceNotFoundException;
import com.example.fantasticosback.Exception.BusinessValidationException;
import com.example.fantasticosback.Model.Document.Request;
import com.example.fantasticosback.Persistence.Repository.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RequestService {

    @Autowired
    private RequestRepository requestRepository;

    public Request save(Request request) {
        return requestRepository.save(request);
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
        findById(request.getRequestId());
        return requestRepository.save(request);
    }

    public List<Request> findByStateName(String stateName) {
        // Validar que el nombre del estado no esté vacío
        if (stateName == null || stateName.trim().isEmpty()) {
            throw new BusinessValidationException("State name cannot be null or empty");
        }
        return requestRepository.findByStateName(stateName);
    }


    public RequestDTO toDTO(Request request) {
        return new RequestDTO(
                request.getRequestId(),
                request.getStudentId(),
                request.getSourceGroup(),
                request.getDestinationGroup(),
                request.getType(),
                request.getObservations(),
                request.getState().getStateName(),
                request.getRequestDate(),
                request.getPriority(),
                request.getEvaluationApproved()
        );
    }

    public Request fromDTO(RequestDTO dto) {
        Request request = new Request(
                dto.getId(),
                dto.getSourceGroup(),
                dto.getDestinationGroup(),
                dto.getType(),
                dto.getObservations(),
                dto.getRequestDate(),
                dto.getStudentId()
        );

        request.setEvaluationApproved(dto.getEvaluationApproved());
        request.setPriority(dto.getPriority());

        return request;
    }

    public List<RequestDTO> toDTOList(List<Request> requests) {
        return requests.stream().map(this::toDTO).collect(Collectors.toList());
    }
}
