package com.example.fantasticosback.Persistence.Repository;

import com.example.fantasticosback.Model.Entities.DeanOffice;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeanOfficeRepository extends MongoRepository<DeanOffice, String> {
    List<DeanOffice> findByFaculty(String faculty);
}
