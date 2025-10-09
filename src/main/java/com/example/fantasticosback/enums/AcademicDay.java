package com.example.fantasticosback.enums;



public enum AcademicDay {

    LUNES("Lunes"),
    MARTES("Martes"),
    MIERCOLES("Miércoles"),
    JUEVES("Jueves"),
    VIERNES("Viernes"),
    SABADO("Sábado");

    private final String displayName;

    AcademicDay(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {return displayName;}
    public boolean isWeekend() {return this == SABADO;}
}

