package com.example.fantasticosback.repository;

import com.example.fantasticosback.model.Document.Subject;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubjectRepository extends MongoRepository<Subject, String> {
    List<Subject> findByName(String name);
    List<Subject> findBySemester(int semester);
    List<Subject> findByCredits(int credits);
    Optional<Subject> findByCode(String subjectId);
}
