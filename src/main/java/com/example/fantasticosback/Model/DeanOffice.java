package com.example.fantasticosback.Model;

import com.example.fantasticosback.Model.Observers.RequestObserver;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.logging.Logger;

@Document(collection = "Decanaturas")
public class DeanOffice {

    @Id
    private String id;
    private String faculty;
    private static final Logger log = Logger.getLogger(DeanOffice.class.getName());
    private ArrayList<Student> students = new ArrayList<>();

    private ArrayList<RequestObserver> observers = new ArrayList<>();

    public DeanOffice(String id, String faculty) {
        this.id = id;
        this.faculty = faculty;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getFaculty() {
        return faculty;
    }

    public void addObserver(RequestObserver observer){
        observers.add(observer);
    }
    private void alertObserver(Request request) {
        for(RequestObserver observer: observers){
            observer.notifyRequest(request);
        }
    }

    public void addEstudiante(Student student) {
        students.add(student);
    }

    public void gestionarSolicitud(Student student, Request request) {
        try {
            request.recoverState();

            if (request.getState().getStateName().equals("Pendiente")) {
                request.procesar("Decanatura");
            }

            boolean aprobada = evaluar(student, request);
            request.setEvaluacionAprobada(aprobada);

            request.procesar("Decanatura");
            alertObserver(request);

        } catch (Exception e) {
            log.severe("Error al gestionar solicitud: " + e.getMessage());
            throw new IllegalStateException("No se pudo procesar la solicitud.");
        }
    }

    private boolean evaluar(Student student, Request request) {
        switch (request.getType()) {
            case "grupo":
                return evaluarCambio(student, request, "grupo");
            case "materia":
                return evaluarCambio(student, request, "materia");
            default:
                log.warning("Tipo de solicitud no reconocido: " + request.getType());
                return false;
        }
    }

    private boolean evaluarCambio(Student student, Request request, String tipo) {
        Group origen = request.getGrupoOrigen();
        Group destino = request.getGrupoDestino();

        if (!destino.isEstado()) {
            log.warning("Solicitud rechazada: El grupo destino " + destino.getNumber() + " no está activo");
            return false;
        }
        if (destino.getCapacity() <= 0) {
            log.warning("Solicitud rechazada: El grupo destino " + destino.getNumber() + " no tiene cupos disponibles");
            return false;
        }
        if (hayChoqueHorario(student, destino, origen)) {
            log.warning("Solicitud rechazada: Hay choque de horario con el grupo destino " + destino.getNumber());
            return false;
        }

        log.info("Solicitud de cambio de " + tipo + " aprobada: de " + origen.getNumber() + " a " + destino.getNumber());
        return true;
    }

    private boolean hayChoqueHorario(Student student, Group groupDestino, Group groupExcluir) {
        if (student.getSemestres().isEmpty()) {
            return false;
        }

        Semester semesterActual = student.getSemestres().get(student.getSemestres().size() - 1);
        Enrollment enrollmentTemporal = new Enrollment(groupDestino, 0, "temporal", 0.0);

        return semesterActual.getSubjects().stream().filter(inscripcion -> !inscripcion.getGrupo().equals(groupExcluir)).anyMatch(enrollmentTemporal::validarChoque);
    }

    public  ArrayList<Request> getSolicitudesPorFacultad() {
        ArrayList<Request> solicitudesFacultad = new ArrayList<>();
        for (Student student : students) {
            solicitudesFacultad.addAll(student.getRequests());
        }
        return solicitudesFacultad;
    }

    public ArrayList<Student> getEstudiantes() {
        return students;
    }


}

