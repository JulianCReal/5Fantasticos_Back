
package com.example.fantasticosback.service;
import com.example.fantasticosback.model.Document.ChangePeriod;
import com.example.fantasticosback.repository.ChangePeriodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class ChangePeriodService {


    private final ChangePeriodRepository repository;

    public ChangePeriod create(ChangePeriod period) {
        return repository.save(period);
    }

    public Optional<ChangePeriod> getActivePeriod() {
        LocalDate today = LocalDate.now();
        return repository.findFirstByStartDateBeforeAndEndDateAfter(today, today);
    }

    public boolean isChangeAllowed(String changeType) {
        Optional<ChangePeriod> periodOpt = getActivePeriod();
        if (periodOpt.isEmpty()) return false;

        ChangePeriod period = periodOpt.get();
        switch (changeType) {
            case "subject": return period.isAllowSubjectChanges();
            case "group": return period.isAllowGroupChanges();
            default: return false;
        }
    }
}
