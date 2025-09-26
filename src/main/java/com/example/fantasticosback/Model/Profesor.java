package com.example.fantasticosback.Model;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Document(collection = "Profesores")
public class Profesor extends Persona {

    @Id
    private String id;
    private String departamento;
    private HashMap<String, Materia> materiasAsignadas;
    private static final Logger log = Logger.getLogger(Profesor.class.getName());

    public Profesor() {
        super();
        this.materiasAsignadas = new HashMap<>();
    }

    public Profesor(String nombre, String apellido, int documento, String departamento) {
        super(nombre, apellido, documento);
        this.departamento = departamento;
        this.materiasAsignadas = new HashMap<>();
    }

    // Getters y Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public HashMap<String, Materia> getMateriasAsignadas() {
        return materiasAsignadas;
    }

    public void setMateriasAsignadas(HashMap<String, Materia> materiasAsignadas) {
        this.materiasAsignadas = materiasAsignadas;
    }

    public void asignarMateria(Materia materia) {
        String codigo = String.valueOf(materia.getIdMateria());
        if (materiasAsignadas.containsKey(codigo)) {
            log.warning("La materia " + materia.getNombre() + " ya está asignada al profesor " + this.nombre + " " + this.apellido);
        } else {
            materiasAsignadas.put(codigo, materia);
            log.info("Materia " + materia.getNombre() + " asignada al profesor " + this.nombre + " " + this.apellido);
        }
    }

    @Override
    public void mostrarInformacion() {
        log.info(() -> "Profesor: " + nombre + " " + apellido +
                ", Documento: " + documento +
                ", Departamento: " + departamento +
                ", Materias asignadas: " + materiasAsignadas.size());
    }

    public boolean esEstudianteMatriculado(Estudiante estudiante, List<Grupo> grupos) {
        for (Grupo grupo : grupos) {
            if (estudiante.estaInscritoEnGrupo(grupo)) {
                return true;
            }
        }
        return false;
    }

    public void verSemaforoEstudiante(Estudiante estudiante, List<Grupo> grupos) {
        if (esEstudianteMatriculado(estudiante, grupos)) {
            log.info("=== SEMÁFORO ACADÉMICO DEL ESTUDIANTE ===");
            log.info("Consultado por el profesor: " + this.nombre + " " + this.apellido);
        } else {
            log.warning("El estudiante " + estudiante.getNombre() + " " + estudiante.getApellido() +
                       " no está matriculado en ninguna de sus materias. No puede ver su semáforo académico.");
        }
    }

}
