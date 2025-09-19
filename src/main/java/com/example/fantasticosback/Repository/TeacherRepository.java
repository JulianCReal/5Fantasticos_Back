package com.example.fantasticosback.Repository;

import com.example.fantasticosback.util.Profesor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeacherRepository extends MongoRepository<Profesor, String> {
    List<Profesor> findByDepartamento(String departamento);
    List<Profesor> findByNombre(String nombre);
    List<Profesor> findByApellido(String apellido);
}
