package com.example.fantasticosback.Persistence.Repository;

import com.example.fantasticosback.Model.Entities.Student;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends MongoRepository<Student, String> {
    List<Student> findByCareer(String career);
    List<Student> findBySemester(int semester);
}
