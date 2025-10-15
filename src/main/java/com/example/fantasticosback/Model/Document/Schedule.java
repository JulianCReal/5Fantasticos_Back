package com.example.fantasticosback.Model.Document;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;
import com.example.fantasticosback.enums.AcademicDay;
import com.example.fantasticosback.enums.TimeSlot;


@Document(collation = "Schedules")
@Data
public class Schedule {

    @Id
    private String id;
    private AcademicDay academicDay;
    private TimeSlot timeSlot;
    private String classroom;

    @DBRef
    private Group group;
    @DBRef
    private Teacher teacher;

    private boolean active;


    public Schedule() {}

    public Schedule(String id, AcademicDay academicDay, TimeSlot timeSlot, String classroom, Group group, Teacher teacher) {
        this.id = id;
        this.academicDay = academicDay;
        this.timeSlot = timeSlot;
        this.classroom = classroom;
        this.group = group;
        this.teacher = teacher;
        this.active = true;

        if (!timeSlot.isAvailableForDay(academicDay)) {
            throw new IllegalArgumentException(
                "TimeSlot " + timeSlot.getDisplayName() +
                " is not available for " + academicDay.getDisplayName()
            );
        }
    }

    public boolean overlaps(Schedule otherSchedule) {
        if (!this.academicDay.equals(otherSchedule.academicDay)) {
            return false; // No se solapan si son en días diferentes
        }

        return this.timeSlot.overlaps(otherSchedule.timeSlot);
    }


}
