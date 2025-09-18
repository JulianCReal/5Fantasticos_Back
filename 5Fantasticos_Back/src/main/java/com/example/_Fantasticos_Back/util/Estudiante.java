package com.example._Fantasticos_Back.util;

import java.util.*;
import java.util.logging.Logger;

public class Estudiante extends Persona {
    private String carrera;
    private ArrayList<Semestre> semestres = new ArrayList<>();
    private static final Logger log = Logger.getLogger(Estudiante.class.getName());

    public Estudiante(String nombre, String apellido, int documento, String carrera) {
        super(nombre, apellido, documento);
        this.carrera = carrera;
    }

    public String getCarrera() {
        return carrera;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }

    public ArrayList<Semestre> getSemestres() {
        return semestres;
    }

    public void setSemestres(ArrayList<Semestre> semestres) {
        this.semestres = semestres;
    }

    public boolean verificarChoqueHorario(Grupo grupoDeseado) {
        if (semestres.isEmpty()) {
            return false;
        }

        Semestre semestreActual = semestres.get(semestres.size() - 1);
        Inscripcion inscripcionTemporal = new Inscripcion(grupoDeseado, 0, "cursando", 0.0);

        for (Inscripcion inscripcionExistente : semestreActual.getMaterias()) {
            if (inscripcionTemporal.validarChoque(inscripcionExistente)) {
                log.warning(" El grupo " + grupoDeseado.getNumero() +
                           " choca con " + inscripcionExistente.getGrupo().getMateria().getNombre());
                return true;
            }
        }
        return false;
    }

    public boolean agregarMateria(Grupo grupoElegido) {
        if (!grupoElegido.isEstado()) {
            log.warning("El grupo " + grupoElegido.getNumero() + " no está activo.");
            return false;
        }

        if (verificarChoqueHorario(grupoElegido)) {
            log.warning("No se puede inscribir: hay choque de horario.");
            return false;
        }

        int inscripcionId = (int) (Math.random() * 100000);
        Inscripcion nuevaInscripcion = new Inscripcion(grupoElegido, inscripcionId, "activa", 0.0);

        if (!semestres.isEmpty()) {
            semestres.get(semestres.size() - 1).agregarMateria(nuevaInscripcion);
            log.info(" Inscripción exitosa al grupo " + grupoElegido.getNumero() +
                    " de " + grupoElegido.getMateria().getNombre());
            return true;
        }

        log.warning("No hay semestre activo para agregar la materia.");
        return false;
    }

    @Override
    public void mostrarInformacion(){
        log.info(() -> "Estudiante: " + nombre + " " + apellido + ", " +
                "Documento: " + documento + ", " +
                "Carrera: " + carrera + ", " +
                "Semestres: " + semestres.size());
    }
}
