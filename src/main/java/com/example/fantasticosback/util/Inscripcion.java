package com.example.fantasticosback.util;

public class Inscripcion {
    private Grupo grupo;
    private int id;
    private String estado;
    private double notaFinal;

    public Inscripcion(Grupo grupo, int id, String estado, double notaFinal) {
        this.grupo = grupo;
        this.id = id;
        this.estado = estado;
        this.notaFinal = notaFinal;
    }

    // Getters y Setters
    public Grupo getGrupo() {
        return grupo;
    }

    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public double getNotaFinal() {
        return notaFinal;
    }

    public void setNotaFinal(double notaFinal) {
        this.notaFinal = notaFinal;
    }

    public void cancelar() {
        this.estado = "cancelada";
    }

    public void evaluar() {
        if (notaFinal >= 30) {
            this.estado = "aprobada";
        } else {
            this.estado = "reprobada";
        }
    }

    public void cambiarGrupo(Grupo nuevoGrupo) {
        this.grupo = nuevoGrupo;
    }

    public boolean validarChoque(Inscripcion otra) {
        for (SesionClase sesion1 : this.grupo.getSesiones()) {
            for (SesionClase sesion2 : otra.grupo.getSesiones()) {
                if (sesion1.verificarChoque(sesion2)) {
                    return true;
                }
            }
        }
        return false;
    }
}
