package com.example.fantasticosback.Repository;

import com.example.fantasticosback.util.Decanatura;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DecanaturaRepository extends MongoRepository<Decanatura, String> {
    List<Decanatura> findByFacultad(String facultad);
}
