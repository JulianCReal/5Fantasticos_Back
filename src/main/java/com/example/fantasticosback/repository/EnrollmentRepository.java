package com.example.fantasticosback.repository;

import com.example.fantasticosback.model.Document.Enrollment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EnrollmentRepository extends MongoRepository<Enrollment, String> {

    List<Enrollment> findByStudentId(String studentId);

    List<Enrollment> findByGroupId(String groupId);

    boolean existsByStudentIdAndGroupId(String studentId, String groupId);
}
