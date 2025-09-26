package com.example.fantasticosback.Model;

import com.example.fantasticosback.util.CatalogoMaterias;

import java.util.ArrayList;

public class Carrera {
    private String nombre;
    private ArrayList<Materia> materias;
    private int totalCreditos;

    public Carrera(String nombre, int totalCreditos) {
        this.nombre = nombre;
        this.materias = new ArrayList<>();
        this.totalCreditos = totalCreditos;

    }

    private void agregarMaterias() {
    }

    public ArrayList<Materia> getMaterias() {
        return materias;
    }

    public int getTotalCreditos() {
        return totalCreditos;
    }

}