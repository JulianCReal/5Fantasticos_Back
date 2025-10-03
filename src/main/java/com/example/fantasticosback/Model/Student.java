package com.example.fantasticosback.Model;

import com.example.fantasticosback.Model.Observers.RequestObserver;
import com.example.fantasticosback.util.SemaforoAcademico;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.*;
import java.util.logging.Logger;

@Document(collection = "Estudiantes")
public class Student extends Person {

    @Id
    private String studentId;
    private String degreeProgram;
    private String studentCode;
    private int semester;
    private ArrayList<Semester> semesters = new ArrayList<>();
    private static final Logger log = Logger.getLogger(Student.class.getName());
    private ArrayList<Request> requests = new ArrayList<>();
    private SemaforoAcademico semaforoAcademico;

    private ArrayList<RequestObserver> observers = new ArrayList<>();

    public Student(String nombre, String apellido, int documento, String degreeProgram, String studentCode, String studentId, int semester, SemaforoAcademico semaforoAcademico) {
        super(nombre, apellido, documento);
        this.degreeProgram = degreeProgram;
        this.studentCode = studentCode;
        this.studentId = studentId;
        this.semester = semester;
        this.semesters = new ArrayList<>();
        this.requests = new ArrayList<>();
        this.semaforoAcademico = semaforoAcademico;
    }

    // Getters y Setters
    public String getStudentId() {
        return studentId;
    }



    public String getDegreeProgram() {
        return degreeProgram;
    }


    public String getStudentCode() {
        return studentCode;
    }


    public int getSemester() {
        return semester;
    }


    public ArrayList<Semester> getSemestres() {
        return semesters;
    }

    public void setSemestres(ArrayList<Semester> semesters) {
        this.semesters = semesters;
    }

    public void agregarSemestre(Semester semester) {
        this.semesters.add(semester);
    }

    public ArrayList<Request> getRequests() {
        return requests;
    }

    public void setRequests(ArrayList<Request> requests) {
        this.requests = requests;
    }


    public void setId(String _id) {
        this.studentId = _id;
    }

    public boolean verificarChoqueHorario(Group groupDeseado) {
        if (semesters.isEmpty()) {
            return false;
        }

        Semester semesterActual = semesters.get(semesters.size() - 1);
        Enrollment enrollmentTemporal = new Enrollment(groupDeseado, 0, "cursando", 0.0);

        for (Enrollment enrollmentExistente : semesterActual.getSubjects()) {
            if (enrollmentTemporal.validarChoque(enrollmentExistente)) {
                log.warning(" El grupo " + groupDeseado.getNumber() +
                        " choca con " + enrollmentExistente.getGrupo().getMateria().getName());
                return true;
            }
        }
        return false;
    }

    public boolean agregarMateria(Group groupElegido) {
        if (!groupElegido.isEstado()) {
            log.warning("El grupo " + groupElegido.getNumber() + " no está activo.");
            return false;
        }

        if (verificarChoqueHorario(groupElegido)) {
            log.warning("No se puede inscribir: hay choque de horario.");
            return false;
        }

        int inscripcionId = (int) (Math.random() * 100000);
        Enrollment nuevaEnrollment = new Enrollment(groupElegido, inscripcionId, "activa", 0.0);

        if (!semesters.isEmpty()) {
            semesters.get(semesters.size() - 1).agregarMateria(nuevaEnrollment);
            log.info(" Inscripción exitosa al grupo " + groupElegido.getNumber() +
                    " de " + groupElegido.getMateria().getName());
            return true;
        }

        log.warning("No hay semester activo para agregar la materia.");
        return false;
    }

    public void quitarMateria(Enrollment enrollment) {
        if (!semesters.isEmpty()) {
            Semester semesterActual = semesters.get(semesters.size() - 1);
            semesterActual.quitarMateria(enrollment);
            log.info("Materia " + enrollment.getGrupo().getMateria().getName() + " retirada exitosamente.");
        } else {
            log.warning("No hay semester activo para quitar la materia.");
        }
    }

    public void cancelarMateria(Enrollment enrollment) {
        if (!semesters.isEmpty()) {
            Semester semesterActual = semesters.get(semesters.size() - 1);
            semesterActual.cancelarMateria(enrollment);
            log.info("Materia " + enrollment.getGrupo().getMateria().getName() + " cancelada exitosamente.");
        } else {
            log.warning("No hay semester activo para cancelar la materia.");
        }
    }

    public void addObserver(RequestObserver observer){
        observers.add(observer);
    }
    private void alertObserver(Request request){
        for(RequestObserver observer: observers){
            observer.notifyRequest(request);
        }
    }

    public Request crearSolicitud(String tipo, Enrollment materiaActual, Group groupDestino, String observaciones) {
        String solicitudId = String.valueOf((int) (Math.random() * 100000));
        Date fechaActual = new Date();

        Request request = new Request(
                solicitudId,
                materiaActual.getGrupo(),
                groupDestino,
                tipo,
                observaciones,
                fechaActual,
                this.studentId
        );

        if ("grupo".equals(tipo)) {
            log.info("Solicitud de cambio de grupo creada: ID " + solicitudId +
                    " de grupo " + materiaActual.getGrupo().getNumber() + " a grupo " + groupDestino.getNumber());
        } else if ("materia".equals(tipo)) {
            log.info("Solicitud de cambio de materia creada: ID " + solicitudId +
                    " de materia " + materiaActual.getGrupo().getMateria().getName() +
                    " a materia " + groupDestino.getMateria().getName());
        }
        alertObserver(request);
        requests.add(request);
        return request;
    }

    public SemaforoAcademico verSemaforoAcademico() {
        return semaforoAcademico;
    }

    @Override
    public void mostrarInformacion() {
        log.info(() -> "Estudiante: " + nombre + " " + lastName + ", " +
                "Documento: " + document + ", " +
                "Carrera: " + degreeProgram + ", " +
                "Semestres: " + semesters.size());
    }
}
