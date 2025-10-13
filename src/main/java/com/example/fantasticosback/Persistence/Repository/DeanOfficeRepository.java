package com.example.fantasticosback.Persistence.Repository;

import com.example.fantasticosback.Model.Document.DeanOffice;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeanOfficeRepository extends MongoRepository<DeanOffice, String> {
    DeanOffice findByFaculty(String faculty);
}
