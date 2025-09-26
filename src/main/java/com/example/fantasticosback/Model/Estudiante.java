package com.example.fantasticosback.Model;

import com.example.fantasticosback.Model.Observers.ObserverSolicitud;
import com.example.fantasticosback.util.SemaforoAcademico;
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
    private SemaforoAcademico semaforoAcademico;

    private ArrayList<ObserverSolicitud> observers = new ArrayList<>();

    public Estudiante(String nombre, String apellido, int documento, String carrera, String codigo, String idEstudiante, int semestre, SemaforoAcademico semaforoAcademico) {
        super(nombre, apellido, documento);
        this.carrera = carrera;
        this.codigo = codigo;
        this.idEstudiante = idEstudiante;
        this.semestre = semestre;
        this.semestres = new ArrayList<>();
        this.solicitudes = new ArrayList<>();
        this.semaforoAcademico = semaforoAcademico;
    }

    // Getters y Setters
    public String getIdEstudiante() {
        return idEstudiante;
    }



    public String getCarrera() {
        return carrera;
    }


    public String getCodigo() {
        return codigo;
    }


    public int getSemestre() {
        return semestre;
    }


    public ArrayList<Semestre> getSemestres() {
        return semestres;
    }


    public ArrayList<Solicitud> getSolicitudes() {
        return solicitudes;
    }

    public SemaforoAcademico getSemaforoAcademico() {
        return semaforoAcademico;
    }

    public SemaforoAcademico verSemaforo() {
        return  semaforoAcademico;
    }

    public void setId(String _id) {
        this.idEstudiante = _id;
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

    public void addObserver(ObserverSolicitud observer){
        observers.add(observer);
    }
    private void alertObserver(Solicitud solicitud){
        for(ObserverSolicitud observer: observers){
            observer.notificarSolicitud(solicitud);
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
                fechaActual,
                this.idEstudiante
        );

        if ("grupo".equals(tipo)) {
            log.info("Solicitud de cambio de grupo creada: ID " + solicitudId +
                    " de grupo " + materiaActual.getGrupo().getNumero() + " a grupo " + grupoDestino.getNumero());
        } else if ("materia".equals(tipo)) {
            log.info("Solicitud de cambio de materia creada: ID " + solicitudId +
                    " de materia " + materiaActual.getGrupo().getMateria().getNombre() +
                    " a materia " + grupoDestino.getMateria().getNombre());
        }
        alertObserver(solicitud);
        solicitudes.add(solicitud);
        return solicitud;
    }

    public SemaforoAcademico verSemaforoAcademico() {
        return semaforoAcademico;
    }


    public boolean estaInscritoEnGrupo(Grupo grupo) {
        Semestre semestre = semestres.get(semestres.size() - 1);
        for (Inscripcion inscripcion : semestre.getMaterias()) {
            if (inscripcion.getGrupo().getId() == grupo.getId()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void mostrarInformacion() {
        log.info(() -> "Estudiante: " + nombre + " " + apellido + ", " +
                "Documento: " + documento + ", " +
                "Carrera: " + carrera + ", " +
                "Semestres: " + semestres.size());
    }
}
