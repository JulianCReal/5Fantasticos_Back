package com.example.fantasticosback.model.Document;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "ChangePeriods")
@Data
public class ChangePeriod {

    @Id
    private String id;

    private int year;
    private int academicPeriod;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean active;

    // Permitir cambios por tipo
    private boolean allowSubjectChanges;
    private boolean allowGroupChanges;

    // Getters explícitos para evitar dependencia exclusiva de Lombok en el analizador
    public boolean isAllowSubjectChanges() {
        return allowSubjectChanges;
    }

    public boolean isAllowGroupChanges() {
        return allowGroupChanges;
    }
}
