package com.example.fantasticosback.Repository;

import com.example.fantasticosback.Model.Student;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface StudentRepository extends MongoRepository<Student, String> {
    List<Student> findByDegreeProgram(String degreeProgram);
    List<Student> findBySemester(int semester);
}
