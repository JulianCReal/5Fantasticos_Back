package com.example._Fantasticos_Back.util;

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

    public int getPeriodoAcademico() {
        return periodoAcademico;
    }

    public void agregarMateria(Inscripcion materia) {
        this.materias.add(materia);
    }

    public void quitarMateria(Inscripcion materia) {
        materia.cancelar();
        this.materias.remove(materia);
    }

    public boolean getEstado() {
        return  estado;
    }

}
