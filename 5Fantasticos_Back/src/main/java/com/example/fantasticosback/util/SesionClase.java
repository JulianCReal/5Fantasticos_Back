package com.example.fantasticosback.util;

public class SesionClase {
    private String dia;
    private String horaInicio;
    private String horaFin;
    private String salon;

    public SesionClase(String dia, String horaInicio, String horaFin, String salon) {
        this.dia = dia;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.salon = salon;
    }

    public boolean verificarChoque(SesionClase otraSesion) {
        if (!this.dia.equals(otraSesion.dia)) {
            return false;
        }
        else if  (this.horaInicio.equals(otraSesion.horaInicio) || this.horaFin.equals(otraSesion.horaFin)) {
            return true;
        }
        return false;
    }
}
