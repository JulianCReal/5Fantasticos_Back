package com.example._Fantasticos_Back.util;

public abstract class Persona {

    protected String nombre;
    protected String apellido;
    protected int documento;

    public Persona(String nombre, String apellido, int documento) {
        this.documento = documento;
        this.nombre = nombre;
        this.apellido = apellido;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public int getDocumento() {
        return documento;
    }

    public void mostrarInformacion(){

    }

}
