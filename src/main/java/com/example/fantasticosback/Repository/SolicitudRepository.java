package com.example.fantasticosback.Repository;

import com.example.fantasticosback.Model.Solicitud;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SolicitudRepository extends MongoRepository<Solicitud, String> {
    List<Solicitud> findByNombreEstado(String nombreEstado);
}
