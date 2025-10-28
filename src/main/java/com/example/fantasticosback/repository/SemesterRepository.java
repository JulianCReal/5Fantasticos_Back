package com.example.fantasticosback.repository;

import com.example.fantasticosback.model.Document.Semester;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface SemesterRepository extends MongoRepository<Semester, String> {
    List<Semester> findByStudentId(String studentId);
}
