package com.example.fantasticosback.Server;

import com.example.fantasticosback.Model.Solicitud;
import com.example.fantasticosback.Repository.DecanaturaRepository;
import com.example.fantasticosback.Repository.SolicitudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
