package com.example.fantasticosback.Model;

import java.util.ArrayList;

public class Semestre {
    private int id;
    private int año;
    private int periodoAcademico;
    private boolean estado;
    private int promedioSemestre;
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

    /**
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
     **/

    public boolean isEstado() {
        return estado;
    }

    /**
    public void setEstado(boolean estado) {
        this.estado = estado;
    }
     **/

    public int getPromedioSemestre() {
        return promedioSemestre;
    }

    /**
    public void setPromedioSemestre(int promedioSemestre) {
        this.promedioSemestre = promedioSemestre;
    }
     **/

    public ArrayList<Inscripcion> getMaterias() {
        return materias;
    }

    /**
    public void setMaterias(ArrayList<Inscripcion> materias) {
        this.materias = materias;
    }
     **/

    public void agregarMateria(Inscripcion materia) {
        this.materias.add(materia);
    }

    public void quitarMateria(Inscripcion materia) {
        this.materias.remove(materia);
    }

    public void cancelarMateria(Inscripcion materia) {
        materia.cancelar();
    }

    public void calcularPromedioSemestre() {
        double sumaNotasCreditos = 0.0;
        int creditosTotales = 0;

        for (Inscripcion inscripcion : materias) {
            int creditos = inscripcion.getGrupo().getMateria().getCreditos();
            double nota = inscripcion.getNotaFinal();

            sumaNotasCreditos += nota * creditos;
            creditosTotales += creditos;

        }
            this.promedioSemestre = (int) (sumaNotasCreditos / creditosTotales);
    }
}
