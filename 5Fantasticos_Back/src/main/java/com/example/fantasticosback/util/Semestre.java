package com.example.fantasticosback.util;

import java.util.ArrayList;

public class Semestre {
    private int id;
    private int año;
    private int periodoAcademico;
    private boolean estado;
    private ArrayList<Inscripcion> materias = new ArrayList<>(); // Se usa ArrayList por ahora, está sujeto a cambios

    public Semestre(int id, int año, int periodoAcademico, boolean estado) {
        this.id = id;
        this.año = año;
        this.periodoAcademico = periodoAcademico;
        this.estado = estado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAño() {
        return año;
    }

    public void setAño(int año) {
        this.año = año;
    }

    public int getPeriodoAcademico() {
        return periodoAcademico;
    }

    public void setPeriodoAcademico(int periodoAcademico) {
        this.periodoAcademico = periodoAcademico;
    }

    public boolean getEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public ArrayList<Inscripcion> getMaterias() {
        return materias;
    }

    public void setMaterias(ArrayList<Inscripcion> materias) {
        this.materias = materias;
    }

    public void agregarMateria(Inscripcion materia) {
        this.materias.add(materia);
    }

    public void quitarMateria(Inscripcion materia) {
        materia.cancelar();
        this.materias.remove(materia);
    }
}
