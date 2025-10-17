package com.example.fantasticosback.enums;

import java.time.LocalTime;

public enum TimeSlot {

    SLOT_1("7:00 - 8:30 AM", LocalTime.of(7, 0), LocalTime.of(8, 30)),
    SLOT_2("8:30 - 10:00 AM", LocalTime.of(8, 30), LocalTime.of(10, 0)),
    SLOT_3("10:00 - 11:30 AM", LocalTime.of(10, 0), LocalTime.of(11, 30)),
    SLOT_4("11:30 - 1:00 PM", LocalTime.of(11, 30), LocalTime.of(13, 0)),
    SLOT_5("1:00 - 2:30 PM", LocalTime.of(13, 0), LocalTime.of(14, 30)),
    SLOT_6("2:30 - 4:00 PM", LocalTime.of(14, 30), LocalTime.of(16, 0)),
    SLOT_7("4:00 - 5:30 PM", LocalTime.of(16, 0), LocalTime.of(17, 30)),
    SLOT_8("5:30 - 7:00 PM", LocalTime.of(17, 30), LocalTime.of(19, 0));

    private final String displayName;
    private final LocalTime startTime;
    private final LocalTime endTime;

    TimeSlot(String displayName, LocalTime startTime, LocalTime endTime) {
        this.displayName = displayName;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getDisplayName() {return displayName;}
    public LocalTime getStartTime() {return startTime;}
    public LocalTime getEndTime() {return endTime;}

    // Verificar si dos clases colapsan
    public boolean overlaps(TimeSlot other) {
        return this.startTime.isBefore(other.endTime) && other.startTime.isBefore(this.endTime);
    }

    public boolean isAvailableForDay(AcademicDay day) {
        if (day == AcademicDay.SABADO) {
            return this.ordinal() <= SLOT_4.ordinal();
        }
        return true;
    }
}
