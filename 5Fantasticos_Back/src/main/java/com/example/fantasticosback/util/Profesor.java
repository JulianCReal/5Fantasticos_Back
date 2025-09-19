package com.example.fantasticosback.util;

import java.util.HashMap;
import java.util.logging.Logger;

public class Profesor extends Persona {

    private String departamento;
    private HashMap<String, Materia> materiasAsignadas;
    private static final Logger log = Logger.getLogger(Profesor.class.getName());

    public Profesor(String nombre, String apellido, int documento, String departamento) {
        super(nombre, apellido, documento);
        this.departamento = departamento;
        this.materiasAsignadas = new HashMap<>();
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
}
