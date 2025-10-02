package com.example.fantasticosback.Server;

import com.example.fantasticosback.Dtos.RequestDTO;
import com.example.fantasticosback.Model.Solicitud;
import com.example.fantasticosback.Repository.DecanaturaRepository;
import com.example.fantasticosback.Repository.SolicitudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SolicitudService {

    @Autowired
    private SolicitudRepository solicitudRepository;

    public Solicitud guardar(Solicitud solicitud) {return solicitudRepository.save(solicitud);}

    public List<Solicitud> encontrarTodos() {return solicitudRepository.findAll();}

    public Solicitud obtenerPorId(String id) {return solicitudRepository.findById(id).orElse(null);}

    public void eliminar(String id) {solicitudRepository.deleteById(id);}

    public Solicitud actualizar(Solicitud solicitud) {return solicitudRepository.save(solicitud);}

    public List<Solicitud> obtenerPorNombreEstado(String nombreEstado){
        return solicitudRepository.findByNombreEstado(nombreEstado);
    }
    public RequestDTO toDTO(Solicitud solicitud) {
        return new RequestDTO(
                solicitud.getSolicitudId(),
                solicitud.getIdEstudiante(),
                solicitud.getGrupoOrigen(),
                solicitud.getGrupoDestino(),
                solicitud.getTipo(),
                solicitud.getObservaciones(),
                solicitud.getEstado().getNombreEstado(),
                solicitud.getFechaSolicitud(),
                solicitud.getPrioridad(),
                solicitud.getEvaluacionAprobada()
        );
    }

    public Solicitud fromDTO(RequestDTO dto) {
        Solicitud solicitud = new Solicitud(
                dto.getId(),
                dto.getGrupoOrigen(),
                dto.getGrupoDestino(),
                dto.getTipo(),
                dto.getObservaciones(),
                dto.getFechaSolicitud(),
                dto.getStudentId()
        );

        solicitud.setEvaluacionAprobada(dto.getEvaluacionAprobada());
        solicitud.setPrioridad(dto.getPrioridad());

        return solicitud;
    }

    public List<RequestDTO> toDTOList(List<Solicitud> solicitudes) {
        return solicitudes.stream().map(this::toDTO).collect(Collectors.toList());
    }
}
