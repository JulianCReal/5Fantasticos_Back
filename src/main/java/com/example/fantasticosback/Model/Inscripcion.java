package com.example.fantasticosback.Model;

import com.example.fantasticosback.util.SesionClase;

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEstado() {
        return estado;
    }

    public double getNotaFinal() {
        return notaFinal;
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
