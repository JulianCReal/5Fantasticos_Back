package com.example.fantasticosback.repository;

import com.example.fantasticosback.util.AcademicTrafficLight;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;


@Repository
public interface AcademicTrafficLightRepository extends MongoRepository<AcademicTrafficLight, String> {
    List<AcademicTrafficLight> findByCareer_Name(String careerName);
    Optional<AcademicTrafficLight> findByStudentId(String studentId);
}
