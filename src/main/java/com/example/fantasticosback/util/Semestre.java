package com.example.fantasticosback.util;

import java.util.ArrayList;

public class Semestre {
    private int id;
    private int año;
    private int periodoAcademico;
    private boolean estado;
    private ArrayList<Inscripcion> materias = new ArrayList<>();

    public Semestre() {
        this.materias = new ArrayList<>();
    }

    public Semestre(int id, int año, int periodoAcademico, boolean estado) {
        this.id = id;
        this.año = año;
        this.periodoAcademico = periodoAcademico;
        this.estado = estado;
        this.materias = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<Inscripcion> getMaterias() {
        return materias;
    }

    public void agregarMateria(Inscripcion materia) {
        this.materias.add(materia);
    }

    public void quitarMateria(Inscripcion materia) {
        this.materias.remove(materia);
    }

    public void cancelarMateria(Inscripcion materia) {
        materia.cancelar();
    }
}
