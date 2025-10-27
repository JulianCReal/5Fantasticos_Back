package com.example.fantasticosback.repository;

import com.example.fantasticosback.model.Document.Career;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CareerRepository extends MongoRepository<Career, String> {
}
