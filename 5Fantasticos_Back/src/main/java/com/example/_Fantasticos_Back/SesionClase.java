package com.example._Fantasticos_Back;

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
        return false;
    }
}
