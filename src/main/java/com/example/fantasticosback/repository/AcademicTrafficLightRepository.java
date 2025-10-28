package com.example.fantasticosback.repository;

import com.example.fantasticosback.util.AcademicTrafficLight;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AcademicTrafficLightRepository extends MongoRepository<AcademicTrafficLight, String> {
    Optional<AcademicTrafficLight> findByStudentId(String studentId);
    boolean existsByStudentId(String studentId);
    void deleteByStudentId(String studentId);
}