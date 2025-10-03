package com.example.fantasticosback.util;
import com.example.fantasticosback.Model.Career;
import com.example.fantasticosback.Model.Enrollment;
import com.example.fantasticosback.Model.Subject;
import com.example.fantasticosback.Model.Semester;

import java.util.ArrayList;

public class SemaforoAcademico {
    private int id;
    private int avancePorcentaje;
    private ArrayList<Enrollment> materias = new ArrayList<>();
    private double promedioAcumulado;
    private int creditosAprobados;
    private ArrayList<Semester> semesters = new ArrayList<>();
    private Career career;

    public SemaforoAcademico(int id, int avancePorcentaje,  Career career) {
        this.id = id;
        this.avancePorcentaje = avancePorcentaje;
        this.promedioAcumulado = 0.0;
        this.creditosAprobados = 0;
        this.career = career;
    }

    public void agregarSemestre(Semester semester) {
        this.semesters.add(semester);
    }


    public void actualizarAvance(ArrayList<Enrollment> materias) {
        this.materias = materias;
        this.creditosAprobados = 0; // Reset counter
        for (Enrollment enrollment : materias) {
            if (enrollment.getState().equals("aprobada")) {
                creditosAprobados += enrollment.getGrupo().getMateria().getCredits();
            }
        }
        calcularPromedioAcumulado();
        double avanceCalculado = (double) creditosAprobados / career.getTotalCredits() * 100;
        this.avancePorcentaje = (int) avanceCalculado;
    }

    private void calcularPromedioAcumulado() {
        double sumaNotasCreditos = 0.0;
        int creditosCursados = 0;

        for (Enrollment enrollment : materias) {
            int creditos = enrollment.getGrupo().getMateria().getCredits();
            double nota = enrollment.getFinalGrade();
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

    public ArrayList<Semester> getSemestres() {
        return semesters;
    }

    public ArrayList<Subject> getTodasLasMaterias() {
        if (career != null && career.getMaterias() != null) {
            return career.getMaterias();
        }
        return new ArrayList<>();
    }

    public ArrayList<Enrollment> getMaterias() {
        return materias;
    }

    public int getCreditosAprobados() {
        return creditosAprobados;
    }


}
