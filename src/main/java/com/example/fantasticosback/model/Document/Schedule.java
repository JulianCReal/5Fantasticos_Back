package com.example.fantasticosback.model.Document;

import com.example.fantasticosback.util.ClassSession;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "schedules")
public class Schedule {

    @Id
    private String id;
    private String studentId;
    private List<String> enrollmentIds;
    private Semester semester;
}
