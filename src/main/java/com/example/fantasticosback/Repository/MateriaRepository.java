package com.example.fantasticosback.Repository;

import com.example.fantasticosback.Model.Materia;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MateriaRepository extends MongoRepository<Materia, String> {
    List<Materia> findByNombre(String nombre);
    List<Materia> findBySemestre(int semestre);
    List<Materia> findByCreditos(int creditos);
}
