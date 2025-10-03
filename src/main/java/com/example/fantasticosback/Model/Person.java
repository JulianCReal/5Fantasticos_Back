package com.example.fantasticosback.Model;

public abstract class Person {

    protected String nombre;
    protected String lastName;
    protected int document;

    public Person() {
    }

    public Person(String nombre, String lastName, int document) {
        this.nombre = nombre;
        this.lastName = lastName;
        this.document = document;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getDocument() {
        return document;
    }

    public void setDocument(int document) {
        this.document = document;
    }

    @Override
    public String toString() {
        return "Persona{" +
                "nombre='" + nombre + '\'' +
                ", lastNameapellido='" + lastName + '\'' +
                ", documento=" + document +
                '}';
    }

    public abstract void mostrarInformacion();
}
