package com.example.fantasticosback.repository;

import com.example.fantasticosback.model.Document.Group;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRepository extends MongoRepository<Group, String> {
    List<Group> findBySubjectId(String subjectId);
    List<Group> findAllByNumber(int number);

    @Query("{ 'teacher._id': ?0 }")
    List<Group> findByTeacherId(String teacherId);

    default boolean existsByNumber(int number) {
        return !findAllByNumber(number).isEmpty();
    }
}
