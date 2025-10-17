package com.example.fantasticosback.repository;

import com.example.fantasticosback.model.Document.ChangePeriod;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface ChangePeriodRepository extends MongoRepository<ChangePeriod, String> {

    Optional<ChangePeriod> findFirstByStartDateBeforeAndEndDateAfter(LocalDate startDate, LocalDate endDate);

}

