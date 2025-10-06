package com.example.fantasticosback.Repository;

import com.example.fantasticosback.Model.Subject;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectRepository extends MongoRepository<Subject, String> {
    List<Subject> findByName(String name);
    List<Subject> findBySemester(int semester);
    List<Subject> findByCredits(int credits);
}
