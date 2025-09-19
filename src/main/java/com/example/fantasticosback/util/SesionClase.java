package com.example.fantasticosback.util;

public class SesionClase {
    private String dia;
    private String horaInicio;
    private String horaFin;
    private String salon;

    public SesionClase() {
    }

    public SesionClase(String dia, String horaInicio, String horaFin, String salon) {
        this.dia = dia;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.salon = salon;
    }

    // Getters y Setters
    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(String horaFin) {
        this.horaFin = horaFin;
    }

    public String getSalon() {
        return salon;
    }

    public void setSalon(String salon) {
        this.salon = salon;
    }

    public boolean verificarChoque(SesionClase otraSesion) {
        System.out.println("=== DEBUG CHOQUE ===");
        System.out.println("Esta sesión: " + this.dia + " " + this.horaInicio + "-" + this.horaFin);
        System.out.println("Otra sesión: " + otraSesion.dia + " " + otraSesion.horaInicio + "-" + otraSesion.horaFin);

        if (!this.dia.equals(otraSesion.dia)) {
            System.out.println("No hay choque: días diferentes");
            return false;
        } else if (this.horaInicio.equals(otraSesion.horaInicio) || this.horaFin.equals(otraSesion.horaFin)) {
            System.out.println("HAY CHOQUE: horas iguales");
            return true;
        }
        System.out.println("No hay choque: horas diferentes");
        return false;
    }
}
