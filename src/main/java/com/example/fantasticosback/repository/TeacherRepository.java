package com.example.fantasticosback.repository;

import com.example.fantasticosback.model.Document.Teacher;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeacherRepository extends MongoRepository<Teacher, String> {
    List<Teacher> findByDepartment(String department);
    List<Teacher> findByName(String name);
    List<Teacher> findByLastName(String lastName);
    Teacher findByDocument(String document);
}