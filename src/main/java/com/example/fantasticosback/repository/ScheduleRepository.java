package com.example.fantasticosback.repository;

import com.example.fantasticosback.model.Document.Schedule;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleRepository extends MongoRepository<Schedule, String> {
    List<Schedule> findByStudentId(String studentId);
}
