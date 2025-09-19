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
    }

    public Grupo(int id, int numero, int capacidad, Materia materia) {
        this(id, numero, capacidad, true, materia, null);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public int getCapacidad() {
        return capacidad;
    }


    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public Materia getMateria() {
        return materia;
    }

    public void setMateria(Materia materia) {
        this.materia = materia;
    }

    public Profesor getProfesor() {
        return profesor;
    }

    public void setProfesor(Profesor profesor) {
        this.profesor = profesor;
    }

    public ArrayList<SesionClase> getSesiones() {
        return sesiones;
    }

    public void setSesiones(ArrayList<SesionClase> sesiones) {
        this.sesiones = sesiones;
    }

    public void modificarCapacidad(int nuevaCapacidad) {
        this.capacidad = nuevaCapacidad;
    }

    public void agregarSesion(SesionClase sesion) {
        this.sesiones.add(sesion);
    }

    public void asignarProfesor(Profesor profesor) {
        this.profesor = profesor;
    }
}
