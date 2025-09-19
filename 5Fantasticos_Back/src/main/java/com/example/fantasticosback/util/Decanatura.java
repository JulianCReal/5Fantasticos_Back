package com.example.fantasticosback.util;

import java.util.logging.Logger;

public class Decanatura {
    private int id;
    private String facultad;
    private static final Logger log = Logger.getLogger(Decanatura.class.getName());

    public Decanatura(int id, String facultad) {
        this.id = id;
        this.facultad = facultad;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFacultad() {
        return facultad;
    }

    public void setFacultad(String facultad) {
        this.facultad = facultad;
    }

    public void consultarSemaforo(Estudiante e) {
        //e.verSemaforo();
    }

    public void verHorario(Estudiante e) {
        //e.obtenerHorario();
    }

    public void consultarGrupos(Materia m) {
        //m.obtenerGrupos();
    }

    public void verSolicitudes(Estudiante e) {
        //e.obtenerSolicitudes();
    }

    public boolean evaluarSolicitud(Estudiante estudiante, Solicitud solicitud) {
        log.info("Evaluando solicitud ID: " + solicitud.getId() + " de estudiante: " + estudiante.getNombre());

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

        // Verificar que el grupo destino tenga cupos disponibles
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

        // Verificar que el grupo destino esté activo
        if (!grupoDestino.isEstado()) {
            log.warning("Solicitud rechazada: El grupo destino " + grupoDestino.getNumero() + " no está activo");
            solicitud.cambiarEstado("rechazada");
            return false;
        }

        // Verificar que el grupo destino tenga cupos disponibles
        if (grupoDestino.getCapacidad() <= 0) {
            log.warning("Solicitud rechazada: El grupo destino " + grupoDestino.getNumero() + " no tiene cupos disponibles");
            solicitud.cambiarEstado("rechazada");
            return false;
        }

        // Verificar que no haya choque de horario
        if (verificarChoqueHorario(estudiante, grupoDestino, grupoOrigen)) {
            log.warning("Solicitud rechazada: Hay choque de horario con la nueva materia");
            solicitud.cambiarEstado("rechazada");
            return false;
        }


        // Si todas las validaciones pasan, aprobar la solicitud
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
                return true;
            }
        }

        return false;
    }
}
