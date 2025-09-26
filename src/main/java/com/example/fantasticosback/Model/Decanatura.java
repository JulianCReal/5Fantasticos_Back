package com.example.fantasticosback.Model;

import com.example.fantasticosback.Model.Observers.ObserverSolicitud;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.logging.Logger;

@Document(collection = "Decanaturas")
public class Decanatura {

    @Id
    private String id;
    private String facultad;
    private static final Logger log = Logger.getLogger(Decanatura.class.getName());
    private ArrayList<Estudiante> estudiantes = new ArrayList<>();

    private ArrayList<ObserverSolicitud> observers = new ArrayList<>();

    public Decanatura(String id, String facultad) {
        this.id = id;
        this.facultad = facultad;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getFacultad() {
        return facultad;
    }

    public void addObserver(ObserverSolicitud observer){
        observers.add(observer);
    }
    private void alertObserver(Solicitud solicitud) {
        for(ObserverSolicitud observer: observers){
            observer.notificarSolicitud(solicitud);
        }
    }

    public void addEstudiante(Estudiante estudiante) {
        estudiantes.add(estudiante);
    }

    public void gestionarSolicitud(Estudiante estudiante, Solicitud solicitud) {
        try {
            solicitud.recoverState();

            if (solicitud.getEstado().getNombreEstado().equals("Pendiente")) {
                solicitud.procesar("Decanatura");
            }

            boolean aprobada = evaluar(estudiante, solicitud);
            solicitud.setEvaluacionAprobada(aprobada);

            solicitud.procesar("Decanatura");
            alertObserver(solicitud);

        } catch (Exception e) {
            log.severe("Error al gestionar solicitud: " + e.getMessage());
            throw new IllegalStateException("No se pudo procesar la solicitud.");
        }
    }

    private boolean evaluar(Estudiante estudiante, Solicitud solicitud) {
        switch (solicitud.getTipo()) {
            case "grupo":
                return evaluarCambio(estudiante, solicitud, "grupo");
            case "materia":
                return evaluarCambio(estudiante, solicitud, "materia");
            default:
                log.warning("Tipo de solicitud no reconocido: " + solicitud.getTipo());
                return false;
        }
    }

    private boolean evaluarCambio(Estudiante estudiante, Solicitud solicitud, String tipo) {
        Grupo origen = solicitud.getGrupoOrigen();
        Grupo destino = solicitud.getGrupoDestino();

        if (!destino.isEstado()) {
            log.warning("Solicitud rechazada: El grupo destino " + destino.getNumero() + " no está activo");
            return false;
        }
        if (destino.getCapacidad() <= 0) {
            log.warning("Solicitud rechazada: El grupo destino " + destino.getNumero() + " no tiene cupos disponibles");
            return false;
        }
        if (hayChoqueHorario(estudiante, destino, origen)) {
            log.warning("Solicitud rechazada: Hay choque de horario con el grupo destino " + destino.getNumero());
            return false;
        }

        log.info("Solicitud de cambio de " + tipo + " aprobada: de " + origen.getNumero() + " a " + destino.getNumero());
        return true;
    }

    private boolean hayChoqueHorario(Estudiante estudiante, Grupo grupoDestino, Grupo grupoExcluir) {
        if (estudiante.getSemestres().isEmpty()) {
            return false;
        }

        Semestre semestreActual = estudiante.getSemestres().get(estudiante.getSemestres().size() - 1);
        Inscripcion inscripcionTemporal = new Inscripcion(grupoDestino, 0, "temporal", 0.0);

        return semestreActual.getMaterias().stream().filter(inscripcion -> !inscripcion.getGrupo().equals(grupoExcluir)).anyMatch(inscripcionTemporal::validarChoque);
    }

    public  ArrayList<Solicitud> getSolicitudesPorFacultad() {
        ArrayList<Solicitud> solicitudesFacultad = new ArrayList<>();
        for (Estudiante estudiante : estudiantes) {
            solicitudesFacultad.addAll(estudiante.getSolicitudes());
        }
        return solicitudesFacultad;
    }



}

