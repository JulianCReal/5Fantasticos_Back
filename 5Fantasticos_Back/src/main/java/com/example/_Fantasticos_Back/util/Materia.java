package com.example._Fantasticos_Back.util;

import java.util.*;
import java.util.logging.Logger;

public class Materia {
    private int idMateria;
    private String nombre;
    private int creditos;
    private int semestre;
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
    public String getNombre() {
        return nombre;
    }
    public int getCreditos() {
        return creditos;
    }
    public int getSemestre() {
        return semestre;
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
