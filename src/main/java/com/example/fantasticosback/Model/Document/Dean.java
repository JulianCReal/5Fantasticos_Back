package com.example.fantasticosback.Model.Document;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Document(collection = "Deans")
public class Dean {

    @Id
    private String id;
    private String name;
    private String professorId;
    private String deanOfficeId;
    private LocalDate startDate;
    private LocalDate endDate;

   
    private List<String> requestIds = new ArrayList<>();

    public void addRequest(String requestId) {
        this.requestIds.add(requestId);
    }
}
