package com.example.fantasticosback.model.Document;

import com.example.fantasticosback.dto.response.SubjectDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Document(collection = "Enrollments")
public class Enrollment {
    @Id
    private String id;
    private String studentId;
    private String groupId;
    private String subjectId;
    private Subject subject;
    private String status;
    private Career career;
    private double finalGrade;

}
