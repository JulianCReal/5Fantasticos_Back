package com.example.fantasticosback.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClassSession {
    private String day;
    private String startTime;
    private String endTime;
    private String classroom;

    public boolean verifyConflict(ClassSession other) {
        if (!this.day.equalsIgnoreCase(other.day)) return false;
        int start1 = parseHour(this.startTime);
        int end1 = parseHour(this.endTime);
        int start2 = parseHour(other.startTime);
        int end2 = parseHour(other.endTime);
        // Hay conflicto si los intervalos se solapan
        return start1 < end2 && start2 < end1;
    }

    private int parseHour(String hour) {
        String[] parts = hour.split(":");
        return Integer.parseInt(parts[0]) * 60 + Integer.parseInt(parts[1]);
    }
}
