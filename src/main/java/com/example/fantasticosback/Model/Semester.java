package com.example.fantasticosback.Model;

import java.util.ArrayList;

public class Semester {
    private int id;
    private int year;
    private int academicPeriod;
    private boolean state;
    private int averageSemesterGrade;
    private ArrayList<Enrollment> subjects = new ArrayList<>();

    public Semester() {
        this.subjects = new ArrayList<>();
    }

    public Semester(int id, int year, int academicPeriod, boolean state) {
        this.id = id;
        this.year = year;
        this.academicPeriod = academicPeriod;
        this.state = state;
        this.subjects = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
    public int getAño() {
        return year;
    }

    public void setAño(int year) {
        this.year = year;
    }

    public int getPeriodoAcademico() {
        return academicPeriod;
    }

    public void setPeriodoAcademico(int academicPeriod) {
        this.academicPeriod = academicPeriod;
    }
     **/

    public boolean isState() {
        return state;
    }

    /**
    public void setState(boolean state) {
        this.state = state;
    }
     **/

    public int getAverageSemesterGrade() {
        return averageSemesterGrade;
    }

    /**
    public void setPromedioSemestre(int averageSemesterGrade) {
        this.averageSemesterGrade = averageSemesterGrade;
    }
     **/

    public ArrayList<Enrollment> getSubjects() {
        return subjects;
    }

    /**
    public void setMaterias(ArrayList<Inscripcion> subjects) {
        this.subjects = subjects;
    }
     **/

    public void agregarMateria(Enrollment materia) {
        this.subjects.add(materia);
    }

    public void quitarMateria(Enrollment materia) {
        this.subjects.remove(materia);
    }

    public void cancelarMateria(Enrollment materia) {
        materia.cancelar();
    }

    public void calcularPromedioSemestre() {
        double sumaNotasCreditos = 0.0;
        int creditosTotales = 0;

        for (Enrollment enrollment : subjects) {
            int creditos = enrollment.getGrupo().getMateria().getCredits();
            double nota = enrollment.getFinalGrade();

            sumaNotasCreditos += nota * creditos;
            creditosTotales += creditos;

        }
            this.averageSemesterGrade = (int) (sumaNotasCreditos / creditosTotales);
    }
}
