package com.example.fantasticosback.repository;

import com.example.fantasticosback.model.Document.DeanOffice;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeanOfficeRepository extends MongoRepository<DeanOffice, String> {
    DeanOffice findByFaculty(String faculty);
    DeanOffice findByDeanId(String deanId);
}