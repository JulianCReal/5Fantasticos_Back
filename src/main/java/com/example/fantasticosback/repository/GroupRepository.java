package com.example.fantasticosback.repository;

import com.example.fantasticosback.model.Document.Group;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRepository extends MongoRepository<Group, String> {
    List<Group> findByTeacherId(String teacherId);

    @Query("{'number': ?0}")
    List<Group> findAllByNumber(int number);

    List<Group> findBySubjectId(String subjectId);

    default boolean existsByNumber(int number) {
        return !findAllByNumber(number).isEmpty();
    }
}
