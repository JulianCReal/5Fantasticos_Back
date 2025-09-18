package com.example._Fantasticos_Back.util;

import javax.swing.*;
import java.util.*;
import java.util.logging.Logger;

public class Estudiante extends Persona {

    private String carrera;
    private int semestre;
    private static final Logger logger = Logger.getLogger(Estudiante.class.getName());
    private HashMap<String, Materia> horario = new HashMap<>();

    public Estudiante(String nombre, String apellido, int documento, String carrera, int semestre) {
        super(nombre, apellido, documento);
        this.documento = documento;
        this.carrera = carrera;
        this.semestre = semestre;
    }



    public void agregarMateriaAlHorario(String codigoMateria) {
        if (horario.containsKey(codigoMateria)) {
            logger.warning("La materia " + codigoMateria + " ya está en el horario del estudiante " + this.nombre + " " + this.apellido);
            JOptionPane.showMessageDialog(null, "La materia ya está en el horario.");
        } else {
            Materia nuevaMateria = CatalogoMaterias.getMateria(codigoMateria);
            if (nuevaMateria == null) {
                logger.warning("La materia " + codigoMateria + " no existe en el catálogo.");
                JOptionPane.showMessageDialog(null, "La materia no existe en el catálogo.");
                return;
            }
            horario.put(codigoMateria, nuevaMateria);
            logger.info("Materia " + codigoMateria + " agregada al horario del estudiante " + this.nombre + " " + this.apellido);
            JOptionPane.showMessageDialog(null, "Materia agregada al horario.");
        }
    }

    public HashMap<String, Materia> getHorario() {return horario;}

    @Override
    public void mostrarInformacion(){
        logger.info(() -> "Estudiante: " + nombre + " " + apellido + ", " +
                "Documento: " + documento + ", " +
                "Carrera: " + carrera + ", " +
                "Semestre: " + semestre);
    }
}
