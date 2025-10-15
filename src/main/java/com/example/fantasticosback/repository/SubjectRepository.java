package com.example.fantasticosback.repository;

import com.example.fantasticosback.model.Document.Subject;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectRepository extends MongoRepository<Subject, String> {
    List<Subject> findByName(String name);
    List<Subject> findBySemester(int semester);
    List<Subject> findByCredits(int credits);
}
