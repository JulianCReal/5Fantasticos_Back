package com.example.fantasticosback.model.Document;

import com.example.fantasticosback.util.ClassSession;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;


@Data
@Document(collection = "Groups")
@AllArgsConstructor
public class Group {
    @Id
    private String id;

    private String subjectId;
    private int number;
    private int capacity;
    private boolean active;
    private Teacher teacher;
    private ArrayList<Student> groupStudents;
    private ArrayList<ClassSession> sessions;

}
