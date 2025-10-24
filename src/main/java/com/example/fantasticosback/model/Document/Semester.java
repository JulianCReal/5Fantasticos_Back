package com.example.fantasticosback.model.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Semester {
    private String id;
    private int year;
    private int academicPeriod;
    private boolean active;
    private int semesterAverage;
    private Schedule schedule;

}
