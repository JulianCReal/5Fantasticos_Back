package com.example.fantasticosback.util;

import java.util.*;
import java.util.logging.Logger;

public class Materia {
    private int idMateria;
    private String nombre;
    private int creditos;
    private int semestre;
    private List<String> horario;
    private ArrayList<Grupo> gruposDisponibles = new ArrayList<>();
    private static final Logger logger = Logger.getLogger(Materia.class.getName());
    private static Map<String, Materia> catalogoMaterias = new HashMap<>();

    public Materia(int idMateria, String nombre, int creditos, int semestre) {
        this.idMateria = idMateria;
        this.nombre = nombre;
        this.creditos = creditos;
        this.semestre = semestre;
    }

    public int getIdMateria() {
        return idMateria;
    }

    public void setIdMateria(int idMateria) {
        this.idMateria = idMateria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCreditos() {
        return creditos;
    }

    public void setCreditos(int creditos) {
        this.creditos = creditos;
    }

    public int getSemestre() {
        return semestre;
    }

    public void setSemestre(int semestre) {
        this.semestre = semestre;
    }

    public List<String> getHorario() {
        return horario;
    }

    public void setHorario(List<String> horario) {
        this.horario = horario;
    }

    public ArrayList<Grupo> getGruposDisponibles() {
        return gruposDisponibles;
    }

    public void setGruposDisponibles(ArrayList<Grupo> gruposDisponibles) {
        this.gruposDisponibles = gruposDisponibles;
    }

    public void agregarGrupo(Grupo grupo) {
        this.gruposDisponibles.add(grupo);
    }

    public List<Grupo> getGruposConCupos() {
        return gruposDisponibles.stream()
                .filter(grupo -> grupo.isEstado())
                .toList();
    }

    public void mostrarInformacion() {
        logger.info(() -> "Materia: " + nombre +
                " (ID: " + idMateria +
                ", Créditos: " + creditos +
                ", Semestre: " + semestre +
                ", Grupos disponibles: " + gruposDisponibles.size() + ")");
    }
}
