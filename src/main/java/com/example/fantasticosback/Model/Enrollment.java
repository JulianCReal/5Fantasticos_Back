package com.example.fantasticosback.Model;

import com.example.fantasticosback.util.ClassSession;

public class Enrollment {
    private Group group;
    private int id;
    private String state;
    private double finalGrade;


    public Enrollment(Group group, int id, String state, double finalGrade) {
        this.group = group;
        this.id = id;
        this.state = state;
        this.finalGrade = finalGrade;
    }

    // Getters y Setters
    public Group getGrupo() {
        return group;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    public double getFinalGrade() {
        return finalGrade;
    }


    public void cancelar() {
        this.state = "cancelada";
    }

    public void evaluar() {
        if (finalGrade >= 30) {
            this.state = "aprobada";
        } else {
            this.state = "reprobada";
        }
    }


    public void cambiarGrupo(Group nuevoGroup) {
        this.group = nuevoGroup;
    }

    public boolean validarChoque(Enrollment otra) {
        for (ClassSession sesion1 : this.group.getSesiones()) {
            for (ClassSession sesion2 : otra.group.getSesiones()) {
                if (sesion1.verificarChoque(sesion2)) {
                    return true;
                }
            }
        }
        return false;
    }
}
