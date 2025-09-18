package com.example._Fantasticos_Back.util;

import java.util.HashMap;
import java.util.logging.Logger;

public class Profesor extends Persona {

    private String departamento;
    private HashMap<String, Materia> materiasAsignadas;
    private static final Logger logger = Logger.getLogger(Profesor.class.getName());


    public Profesor(String nombre, String apellido, int documento, String departamento) {
        super(nombre, apellido, documento);
        this.documento = documento;
        this.departamento = departamento;
        this.materiasAsignadas = new HashMap<>();
    }

    public void asignarMateria(Materia materia) {
        String codigo = String.valueOf(materia.getIdMateria());
        if (materiasAsignadas.containsKey(codigo)) {
            logger.warning("La materia " + materia.getNombre() + " ya está asignada al profesor " + this.nombre + " " + this.apellido);
        } else {
            materiasAsignadas.put(codigo, materia);
            logger.info("Materia " + materia.getNombre() + " asignada al profesor " + this.nombre + " " + this.apellido);
        }
    }



    public HashMap<String, Materia> getMateriasAsignadas() {return materiasAsignadas;}


    @Override
    public void mostrarInformacion() {
        logger.info(() -> "Profesor: " + nombre + " " + apellido +
                ", Documento: " + documento +
                ", ID: " + documento);
    }

}
