package com.example.fantasticosback.Persistence.Repository;

import com.example.fantasticosback.Model.Entities.Group;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRepository extends MongoRepository<Group, Integer> {
    List<Group> findByTeacherId(String teacherId);

    @Query("{'number': ?0}")
    List<Group> findAllByNumber(int number);

    default boolean existsByNumber(int number) {
        return !findAllByNumber(number).isEmpty();
    }
}
