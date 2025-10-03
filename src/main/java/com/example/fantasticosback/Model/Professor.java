package com.example.fantasticosback.Model;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.logging.Logger;

@Document(collection = "Profesores")
public class Professor extends Person {

    @Id
    private String id;
    private String departament;
    private HashMap<String, Subject> enrolledSubjects;
    private static final Logger log = Logger.getLogger(Professor.class.getName());

    public Professor() {
        super();
        this.enrolledSubjects = new HashMap<>();
    }

    public Professor(String nombre, String apellido, int documento, String departament) {
        super(nombre, apellido, documento);
        this.departament = departament;
        this.enrolledSubjects = new HashMap<>();
    }

    // Getters y Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDepartament() {
        return departament;
    }

    public void setDepartament(String departament) {
        this.departament = departament;
    }

    public HashMap<String, Subject> getEnrolledSubjects() {
        return enrolledSubjects;
    }

    public void setEnrolledSubjects(HashMap<String, Subject> enrolledSubjects) {
        this.enrolledSubjects = enrolledSubjects;
    }

    public void asignarMateria(Subject subject) {
        String codigo = String.valueOf(subject.getSubjectId());
        if (enrolledSubjects.containsKey(codigo)) {
            log.warning("La materia " + subject.getName() + " ya está asignada al profesor " + this.nombre + " " + this.lastName);
        } else {
            enrolledSubjects.put(codigo, subject);
            log.info("Materia " + subject.getName() + " asignada al profesor " + this.nombre + " " + this.lastName);
        }
    }

    @Override
    public void mostrarInformacion() {
        log.info(() -> "Profesor: " + nombre + " " + lastName +
                ", Documento: " + document +
                ", Departamento: " + departament +
                ", Materias asignadas: " + enrolledSubjects.size());
    }
}
