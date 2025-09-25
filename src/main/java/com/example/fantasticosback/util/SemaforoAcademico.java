package com.example.fantasticosback.util;

import java.util.ArrayList;

public class SemaforoAcademico {
    private int id;
    private int avancePorcentaje;
    private ArrayList<Inscripcion> materias = new ArrayList<>();
    private double promedioAcumulado;
    private int totalCreditos;
    private int creditosAprobados;
    private ArrayList<Semestre> semestres = new ArrayList<>();

    public SemaforoAcademico(int id, int avancePorcentaje, int totalCreditos) {
        this.id = id;
        this.avancePorcentaje = avancePorcentaje;
        this.promedioAcumulado = 0.0;
        this.totalCreditos = totalCreditos;
        this.creditosAprobados = 0;
    }

    public void agregarSemestre(Semestre semestre) {
        this.semestres.add(semestre);
    }

    public void actualizarAvance(ArrayList<Inscripcion> materias) {
        this.materias = materias;

        for (Inscripcion inscripcion : materias) {
            if (inscripcion.getEstado().equals("aprobada")) {
                creditosAprobados += inscripcion.getGrupo().getMateria().getCreditos();
            }
        }

        calcularPromedioAcumulado();
        this.avancePorcentaje = creditosAprobados / totalCreditos * 100;
    }


    private void calcularPromedioAcumulado() {
        double sumaNotasCreditos = 0.0;

        for (Inscripcion inscripcion : materias) {
            if (inscripcion.getEstado().equals("aprobada")) {
                int creditos = inscripcion.getGrupo().getMateria().getCreditos();
                double nota = inscripcion.getNotaFinal();

                sumaNotasCreditos += nota * creditos;
                creditosAprobados += creditos;
            }
        }
            this.promedioAcumulado = sumaNotasCreditos / creditosAprobados;
    }


    public int getId() {
        return id;
    }



}
