package com.example.fantasticosback.model.Document;

import com.example.fantasticosback.util.ClassRequirement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.*;
import java.util.logging.Logger;

@Data
@Document(collection = "Subjects")
@AllArgsConstructor
@NoArgsConstructor
public class Subject {

    @Id
    private String subjectId;
    private String name;
    private String code;
    private int credits;
    private int semester;
    private LinkedList<ClassRequirement> requirements;
    private static final Logger logger = Logger.getLogger(Subject.class.getName());
}