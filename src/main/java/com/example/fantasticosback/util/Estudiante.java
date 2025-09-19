package com.example.fantasticosback.util;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.*;
import java.util.logging.Logger;

@Document(collection = "Estudiantes")
public class Estudiante extends Persona {

    @Id
    private String idEstudiante;
    private String carrera;
    private String codigo;
    private int semestre;
    private ArrayList<Semestre> semestres = new ArrayList<>();
    private static final Logger log = Logger.getLogger(Estudiante.class.getName());
    private ArrayList<Solicitud> solicitudes = new ArrayList<>();

    public Estudiante(String nombre, String apellido, int documento, String carrera, String codigo, String idEstudiante, int semestre) {
        super(nombre, apellido, documento);
        this.carrera = carrera;
        this.codigo = codigo;
        this.idEstudiante = idEstudiante;
        this.semestre = semestre;
    }
    public void setId(String _id) {
        this.idEstudiante = _id;
    }

    public String getCarrera() {
        return carrera;
    }


    public ArrayList<Semestre> getSemestres() {
        return semestres;
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

    public void quitarMateria(Inscripcion inscripcion) {
        if (!semestres.isEmpty()) {
            Semestre semestreActual = semestres.get(semestres.size() - 1);
            semestreActual.quitarMateria(inscripcion);
            log.info("Materia " + inscripcion.getGrupo().getMateria().getNombre() + " retirada exitosamente.");
        } else {
            log.warning("No hay semestre activo para quitar la materia.");
        }
    }

    public void cancelarMateria(Inscripcion inscripcion) {
        if (!semestres.isEmpty()) {
            Semestre semestreActual = semestres.get(semestres.size() - 1);
            semestreActual.cancelarMateria(inscripcion);
            log.info("Materia " + inscripcion.getGrupo().getMateria().getNombre() + " cancelada exitosamente.");
        } else {
            log.warning("No hay semestre activo para cancelar la materia.");
        }
    }

    public Solicitud crearSolicitud(String tipo, Inscripcion materiaActual, Grupo grupoDestino, String observaciones) {
        int solicitudId = (int) (Math.random() * 100000);
        Date fechaActual = new Date();

        Solicitud solicitud = new Solicitud(
                solicitudId,
                materiaActual.getGrupo(),
                grupoDestino,
                tipo,
                observaciones,
                "pendiente",
                fechaActual
        );

        if ("grupo".equals(tipo)) {
            log.info("Solicitud de cambio de grupo creada: ID " + solicitudId +
                    " de grupo " + materiaActual.getGrupo().getNumero() + " a grupo " + grupoDestino.getNumero());
        } else if ("materia".equals(tipo)) {
            log.info("Solicitud de cambio de materia creada: ID " + solicitudId +
                    " de materia " + materiaActual.getGrupo().getMateria().getNombre() +
                    " a materia " + grupoDestino.getMateria().getNombre());
        }

        solicitudes.add(solicitud);
        return solicitud;
    }

    public ArrayList<Solicitud> getSolicitudes() {
        return solicitudes;
    }

    @Override
    public void mostrarInformacion(){
        log.info(() -> "Estudiante: " + nombre + " " + apellido + ", " +
                "Documento: " + documento + ", " +
                "Carrera: " + carrera + ", " +
                "Semestres: " + semestres.size());
    }
}
