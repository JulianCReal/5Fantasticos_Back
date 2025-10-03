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

    public Request guardar(Request request) {return requestRepository.save(request);}

    public List<Request> encontrarTodos() {return requestRepository.findAll();}

    public Request obtenerPorId(String id) {return requestRepository.findById(id).orElse(null);}

    public void eliminar(String id) {
        requestRepository.deleteById(id);}

    public Request actualizar(Request request) {return requestRepository.save(request);}

    public List<Request> obtenerPorNombreEstado(String nombreEstado){
        return requestRepository.findByStatusName(nombreEstado);
    }
    public RequestDTO toDTO(Request request) {
        return new RequestDTO(
                request.getSolicitudId(),
                request.getStudentId(),
                request.getGrupoOrigen(),
                request.getGrupoDestino(),
                request.getType(),
                request.getRemarks(),
                request.getState().getStateName(),
                request.getRequestDate(),
                request.getPriority(),
                request.getEvaluacionAprobada()
        );
    }

    public Request fromDTO(RequestDTO dto) {
        Request request = new Request(
                dto.getId(),
                dto.getOriginGroup(),
                dto.getDestinationGroup(),
                dto.getType(),
                dto.getObservations(),
                dto.getRequestDate(),
                dto.getStudentId()
        );

        request.setEvaluacionAprobada(dto.getEvaluationApproved());
        request.setPriority(dto.getPriority());

        return request;
    }

    public List<RequestDTO> toDTOList(List<Request> solicitudes) {
        return solicitudes.stream().map(this::toDTO).collect(Collectors.toList());
    }
}
