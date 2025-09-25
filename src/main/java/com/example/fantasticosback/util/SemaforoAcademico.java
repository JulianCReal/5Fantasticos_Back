package com.example.fantasticosback.util;

import java.util.ArrayList;

public class SemaforoAcademico {
    private int id;
    private int avancePorcentaje;
    private ArrayList<Inscripcion> materias = new ArrayList<>();
    private double promedioAcumulado; // Cambiar a double para mayor precisión
    private int totalCreditos;
    private int creditosAprobados;

    public SemaforoAcademico(int id, int avancePorcentaje, int totalCreditos) {
        this.id = id;
        this.avancePorcentaje = avancePorcentaje;
        this.promedioAcumulado = 0.0; // Cambiar a 0.0
        this.totalCreditos = totalCreditos;
        this.creditosAprobados = 0;
    }

    // METODO INCOMPLETO
    public void actualizarAvance(ArrayList<Inscripcion> materias) {
        this.materias = materias;

        for (Inscripcion inscripcion : materias) {
            if (inscripcion.getEstado().equals("aprobada")) {
                creditosAprobados += inscripcion.getGrupo().getMateria().getCreditos();
            }
        }

        calcularPromedioAcumulado();
        this.avancePorcentaje = (int) ((double) creditosAprobados / totalCreditos * 100);
    }


    // METODO INCOMPLETO
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
