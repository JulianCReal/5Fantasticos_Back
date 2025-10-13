package com.example.fantasticosback.Model.Document;

import com.example.fantasticosback.Model.Observers.RequestObserver;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Document(collection = "DeanOffices")
public class DeanOffice {

    @Id
    private String id;
    private String faculty;
    private List<String> students = new ArrayList<>();
    private List<String> professors = new ArrayList<>();
    private List<String> subjects = new ArrayList<>();
    private String deanName;

    @Transient
    private ArrayList<RequestObserver> observers = new ArrayList<>();

    public DeanOffice(String id, String faculty) {
        this.id = id;
        this.faculty = faculty;
    }
    public void addStudentId(String studentId) {
        students.add(studentId);
    }
    public void addProfessorId(String id) {
        professors.add(id);
    }
    public void addSubjectId(String id) {
        subjects.add(id);
    }


}
