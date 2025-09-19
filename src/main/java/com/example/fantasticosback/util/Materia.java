package com.example.fantasticosback.util;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.*;
import java.util.logging.Logger;

@Document(collection = "Materias")
public class Materia {

    @Id
    private int idMateria;
    private String nombre;
    private int creditos;
    private int semestre;
    private ArrayList<Grupo> gruposDisponibles = new ArrayList<>();
    private static final Logger logger = Logger.getLogger(Materia.class.getName());
    private static Map<String, Materia> catalogoMaterias = new HashMap<>();

    public Materia() {
        this.gruposDisponibles = new ArrayList<>();
    }

    public Materia(int idMateria, String nombre, int creditos, int semestre) {
        this.idMateria = idMateria;
        this.nombre = nombre;
        this.creditos = creditos;
        this.semestre = semestre;
        this.gruposDisponibles = new ArrayList<>();
    }

    // Getters y Setters
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

    public ArrayList<Grupo> getGruposDisponibles() {
        return gruposDisponibles;
    }

    public void setGruposDisponibles(ArrayList<Grupo> gruposDisponibles) {
        this.gruposDisponibles = gruposDisponibles;
    }

    public void agregarGrupo(Grupo grupo) {
        this.gruposDisponibles.add(grupo);
    }

    public List<Grupo> getGrupos() {
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
