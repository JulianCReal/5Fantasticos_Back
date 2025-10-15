package com.example.fantasticosback.Repository;

import com.example.fantasticosback.Model.Document.Request;
import com.example.fantasticosback.util.Enums.RequestPriority;
import com.example.fantasticosback.util.Enums.Role;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RequestRepository extends MongoRepository<Request, String> {
    List<Request> findByUserId(String userId);
    List<Request> findByStateName(String stateName);
    List<Request> findByRequestPriority(RequestPriority requestPriority);
    List<Request> findByCreatorRole(Role creatorRole);
    List<Request> findByDeanOffice(String deanOffice);
    List<Request> findStudentsRequestByDeanOffice(String deanOffice, String userId);

    @Query("{ 'requestDate' : { $gte: ?0, $lte: ?1 } }")
    List<Request> findAllByRequestDateBetween(LocalDateTime startDateTime, LocalDateTime endDateTime);
}
