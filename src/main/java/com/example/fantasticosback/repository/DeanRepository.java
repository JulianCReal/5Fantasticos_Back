package com.example.fantasticosback.repository;

import com.example.fantasticosback.model.Document.Dean;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeanRepository extends MongoRepository<Dean, String> {
    Optional<Dean> findByFaculty(String faculty);
}
