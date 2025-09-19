package com.example.fantasticosback.util;

public class Decanatura {
    private int id;
    private String facultad;

    public Decanatura(int id, String facultad) {
        this.id = id;
        this.facultad = facultad;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFacultad() {
        return facultad;
    }

    public void setFacultad(String facultad) {
        this.facultad = facultad;
    }

    public void consultarSemaforo(Estudiante e) {
        //e.verSemaforo();
    }

    public void verHorario(Estudiante e) {
        //e.obtenerHorario();
    }

    public void consultarGrupos(Materia m) {
        //m.obtenerGrupos();
    }

    public void verSolicitudes(Estudiante e) {
        //e.obtenerSolicitudes();
    }
}
