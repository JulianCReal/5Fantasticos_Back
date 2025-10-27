package com.example.fantasticosback.repository;

import com.example.fantasticosback.util.AcademicTrafficLight;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;
import java.util.List;

public interface AcademicTrafficLightRepository extends MongoRepository<AcademicTrafficLight, String> {
    List<AcademicTrafficLight> findByCareer_Name(String careerName);
    Optional<AcademicTrafficLight> findByStudentId(String studentId);
}
