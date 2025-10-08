package com.example.fantasticosback.Persistence.Repository;

import com.example.fantasticosback.Model.Entities.Request;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestRepository extends MongoRepository<Request, String> {
    List<Request> findByStateName(String stateName);
}
