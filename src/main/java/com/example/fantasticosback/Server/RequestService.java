package com.example.fantasticosback.Server;

import com.example.fantasticosback.Dtos.RequestDTO;
import com.example.fantasticosback.Model.Request;
import com.example.fantasticosback.Repository.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RequestService {

    @Autowired
    private RequestRepository requestRepository;

    public Request save(Request request) {return requestRepository.save(request);}

    public List<Request> findAll() {return requestRepository.findAll();}

    public Request findById(String id) {return requestRepository.findById(id).orElse(null);}

    public void delete(String id) {requestRepository.deleteById(id);}

    public Request update(Request request) {return requestRepository.save(request);}

    public List<Request> findByStateName(String stateName){
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
