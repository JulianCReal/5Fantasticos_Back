package com.example.fantasticosback.Repository;

import com.example.fantasticosback.Model.Request;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestRepository extends MongoRepository<Request, String> {
    List<Request> findByStatusName(String statusName);
}
