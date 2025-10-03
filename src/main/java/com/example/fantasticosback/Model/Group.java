package com.example.fantasticosback.Model;

import com.example.fantasticosback.util.ClassSession;

import java.util.ArrayList;

public class Group {
    private int id;
    private int number;
    private int capacity;
    private boolean estado;
    private Subject subject;
    private Professor professor;
    private ArrayList<Student> studentsGrup;
    private ArrayList<ClassSession> sesiones = new ArrayList<>();

    public Group(int id, int number, int capacity, boolean estado, Subject subject, Professor professor) {
        this.id = id;
        this.number = number;
        this.capacity = capacity;
        this.estado = estado;
        this.subject = subject;
        this.professor = professor;
        this.studentsGrup = new ArrayList<>();
        this.sesiones = new ArrayList<>();
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumber() {
        return number;
    }


    public int getCapacity() {
        return capacity;
    }


    public boolean isEstado() {
        return estado;
    }


    public Subject getMateria() {
        return subject;
    }

    public void setMateria(Subject subject) {
        this.subject = subject;
    }

    public Professor getProfesor() {
        return professor;
    }

    public void setProfesor(Professor professor) {
        this.professor = professor;
    }

    public ArrayList<Student> getStudentsGrup() {
        return studentsGrup;
    }

    public void setStudentsGrup(ArrayList<Student> studentsGrup) {
        this.studentsGrup = studentsGrup;
    }

    public ArrayList<ClassSession> getSesiones() {
        return sesiones;
    }

    public void setSesiones(ArrayList<ClassSession> sesiones) {
        this.sesiones = sesiones;
    }

    public void agregarSesion(ClassSession sesion) {
        this.sesiones.add(sesion);
    }
}
