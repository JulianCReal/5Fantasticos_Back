package com.example.fantasticosback.model.Document;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;
import java.util.ArrayList;
import java.util.List;


@Document(collation = "Schedules")
@Data
public class Schedule {

    @Id
    private String id;

    @DBRef
    private Student student;
    private int year;
    private int academicPeriod;
    private int semesterNumber;
    @DBRef
    private List<Enrollment> enrollments = new ArrayList<>();
    private boolean active;

    public Schedule() {}

    public Schedule(String id, Student student, int semesterNumber, int year, int academicPeriod) {
        this.id = id;
        this.student = student;
        this.semesterNumber = semesterNumber;
        this.year = year;
        this.academicPeriod = academicPeriod;
        this.active = true;
    }

    public void addEnrollment(Enrollment enrollment) {
        this.enrollments.add(enrollment);
    }

    public void removeEnrollment(Enrollment enrollment) {
        enrollment.cancel();
        this.enrollments.remove(enrollment);
    }

    public boolean isEmpty() {
        return this.enrollments.isEmpty();
    }




}
