package com.example.fantasticosback.Repository;

import com.example.fantasticosback.util.Estudiante;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface StudentRepository extends MongoRepository<Estudiante, String> {
    List<Estudiante> findByCarrera(String carrera);
    List<Estudiante> findBySemestre(int semestre);
}
