package com.example.fantasticosback.Dtos;

public class StudentDTO extends BaseDTO {
    private String nombre;
    private String carrera;
    private int semestre;

    public StudentDTO(String id, String nombre, String carrera, int semestre) {
        super(id);
        this.nombre = nombre;
        this.carrera = carrera;
        this.semestre = semestre;
    }

    // Getters y Setters
    public String getNombre() { return nombre; }

    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCarrera() { return carrera; }

    public void setCarrera(String carrera) { this.carrera = carrera; }

    public int getSemestre() { return semestre; }

    public void setSemestre(int semestre) { this.semestre = semestre; }
}
