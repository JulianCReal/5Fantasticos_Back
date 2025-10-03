package com.example.fantasticosback.Repository;

import com.example.fantasticosback.Model.Professor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfessorRepository extends MongoRepository<Professor, String> {
    List<Professor> findByDepartment(String department);
    List<Professor> findByFirstName(String firstName);
    List<Professor> findByLastName(String lastName);
}
