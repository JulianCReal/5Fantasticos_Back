package com.example.fantasticosback.util;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.logging.Logger;

@Document(collection = "Decanaturas")
public class Decanatura {

    @Id
    private String id;
    private String facultad;
    private static final Logger log = Logger.getLogger(Decanatura.class.getName());


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

    /**
    public void verSolicitudes(Estudiante e) {
        e.obtenerSolicitudes();
    }
     **/

    public boolean evaluarSolicitud(Estudiante estudiante, Solicitud solicitud) {

        try {
            if ("grupo".equals(solicitud.getTipo())) {
                return evaluarCambioGrupo(estudiante, solicitud);
            } else if ("materia".equals(solicitud.getTipo())) {
                return evaluarCambioMateria(estudiante, solicitud);
            } else {
                log.warning("Tipo de solicitud no reconocido: " + solicitud.getTipo());
                solicitud.cambiarEstado("rechazada");
                return false;
            }
        } catch (Exception e) {
            log.severe("Error al evaluar solicitud: " + e.getMessage());
            solicitud.cambiarEstado("error");
            return false;
        }
    }

    private boolean evaluarCambioGrupo(Estudiante estudiante, Solicitud solicitud) {
        Grupo grupoOrigen = solicitud.getGrupoOrigen();
        Grupo grupoDestino = solicitud.getGrupoDestino();

        if (!grupoDestino.isEstado()) {
            log.warning("Solicitud rechazada: El grupo destino " + grupoDestino.getNumero() + " no está activo");
            solicitud.cambiarEstado("rechazada");
            return false;
        }

        if (grupoDestino.getCapacidad() <= 0) {
            log.warning("Solicitud rechazada: El grupo destino " + grupoDestino.getNumero() + " no tiene cupos disponibles");
            solicitud.cambiarEstado("rechazada");
            return false;
        }

        if (verificarChoqueHorario(estudiante, grupoDestino, grupoOrigen)) {
            log.warning("Solicitud rechazada: Hay choque de horario con el grupo destino " + grupoDestino.getNumero());
            solicitud.cambiarEstado("rechazada");
            return false;
        }

        log.info("Solicitud de cambio de grupo aprobada: de grupo " + grupoOrigen.getNumero() +
                " a grupo " + grupoDestino.getNumero());
        solicitud.cambiarEstado("aprobada");

        return true;
    }

    private boolean evaluarCambioMateria(Estudiante estudiante, Solicitud solicitud) {
        Grupo grupoOrigen = solicitud.getGrupoOrigen();
        Grupo grupoDestino = solicitud.getGrupoDestino();

        if (!grupoDestino.isEstado()) {
            log.warning("Solicitud rechazada: El grupo destino " + grupoDestino.getNumero() + " no está activo");
            solicitud.cambiarEstado("rechazada");
            return false;
        }

        if (grupoDestino.getCapacidad() <= 0) {
            log.warning("Solicitud rechazada: El grupo destino " + grupoDestino.getNumero() + " no tiene cupos disponibles");
            solicitud.cambiarEstado("rechazada");
            return false;
        }

        if (verificarChoqueHorario(estudiante, grupoDestino, grupoOrigen)) {
            log.warning("Solicitud rechazada: Hay choque de horario con la nueva materia");
            solicitud.cambiarEstado("rechazada");
            return false;
        }

        log.info("Solicitud de cambio de materia aprobada: de " + grupoOrigen.getMateria().getNombre() +
                " a " + grupoDestino.getMateria().getNombre());
        solicitud.cambiarEstado("aprobada");
        return true;
    }

    private boolean verificarChoqueHorario(Estudiante estudiante, Grupo grupoDestino, Grupo grupoExcluir) {
        if (estudiante.getSemestres().isEmpty()) {
            return false;
        }

        Semestre semestreActual = estudiante.getSemestres().get(estudiante.getSemestres().size() - 1);
        Inscripcion inscripcionTemporal = new Inscripcion(grupoDestino, 0, "temporal", 0.0);

        for (Inscripcion inscripcion : semestreActual.getMaterias()) {
            if (inscripcion.getGrupo().equals(grupoExcluir)) {
                continue;
            }

            if (inscripcionTemporal.validarChoque(inscripcion)) {
                System.out.println(" El grupo " + grupoDestino.getNumero() +
                        " choca con " + inscripcion.getGrupo().getMateria().getNombre());
                return true;
            }
        }

        return false;
    }
}
