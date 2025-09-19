package com.example.fantasticosback.util;

import java.util.ArrayList;

public class Grupo {
    private int id;
    private int numero;
    private int capacidad;
    private boolean estado;
    private Materia materia;
    private Profesor profesor;
    private ArrayList<SesionClase> sesiones = new ArrayList<>();

    public Grupo(int id, int numero, int capacidad, boolean estado, Materia materia, Profesor profesor) {
        this.id = id;
        this.numero = numero;
        this.capacidad = capacidad;
        this.estado = estado;
        this.materia = materia;
        this.profesor = profesor;
        this.sesiones = new ArrayList<>();
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumero() {
        return numero;
    }


    public int getCapacidad() {
        return capacidad;
    }


    public boolean isEstado() {
        return estado;
    }


    public Materia getMateria() {
        return materia;
    }

    public void setMateria(Materia materia) {
        this.materia = materia;
    }


    public ArrayList<SesionClase> getSesiones() {
        return sesiones;
    }


    public void agregarSesion(SesionClase sesion) {
        this.sesiones.add(sesion);
    }
}
