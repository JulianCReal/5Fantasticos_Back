package com.example.fantasticosback.util;
import com.example.fantasticosback.Model.Inscripcion;
import com.example.fantasticosback.Model.Semestre;

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
        this.creditosAprobados = 0; // Reset counter
        for (Inscripcion inscripcion : materias) {
            if (inscripcion.getEstado().equals("aprobada")) {
                creditosAprobados += inscripcion.getGrupo().getMateria().getCreditos();
            }
        }
        calcularPromedioAcumulado();
        double avanceCalculado = (double) creditosAprobados / totalCreditos * 100;
        this.avancePorcentaje = (int) avanceCalculado;
    }

    private void calcularPromedioAcumulado() {
        double sumaNotasCreditos = 0.0;
        int creditosCursados = 0;

        for (Inscripcion inscripcion : materias) {
            int creditos = inscripcion.getGrupo().getMateria().getCreditos();
            double nota = inscripcion.getNotaFinal();
            sumaNotasCreditos += nota * creditos;
            creditosCursados += creditos;
        }

        if (creditosCursados > 0) {
            double promedioCalculado = sumaNotasCreditos / creditosCursados;
            this.promedioAcumulado = Math.round(promedioCalculado * 10.0) / 10.0;
        } else {
            this.promedioAcumulado = 0.0;
        }
    }

    public int getId() {
        return id;
    }

    public int getAvancePorcentaje() {
        return avancePorcentaje;
    }

    public double getPromedioAcumulado() {
        return promedioAcumulado;
    }

    public int getTotalCreditos() {
        return totalCreditos;
    }

    public ArrayList<Semestre> getSemestres() {
        return semestres;
    }
}
