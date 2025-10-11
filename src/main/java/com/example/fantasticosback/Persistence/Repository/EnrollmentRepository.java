package com.example.fantasticosback.Persistence.Repository;

import com.example.fantasticosback.Model.Entities.Enrollment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EnrollmentRepository extends MongoRepository<Enrollment, String> {

    @Query("{'student._id': ?0}")
    List<Enrollment> findByStudentId(String studentId);

    @Query("{'group.id': ?0}")
    List<Enrollment> findByGroupId(int groupId);

    @Query(value = "{'student._id': ?0, 'group.id': ?1}", exists = true)
    boolean existsByStudentIdAndGroupId(String studentId, int groupId);
}
