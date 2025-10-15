package com.example.fantasticosback.Repository;

import com.example.fantasticosback.Model.Document.Dean;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeanRepository extends MongoRepository<Dean, String> {
    Optional<Dean> findByFaculty(String faculty);
}
