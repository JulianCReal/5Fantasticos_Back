package com.example._Fantasticos_Back;

import java.util.ArrayList;

public class Grupo {
    private int capacidad;
    private int numero;
    private boolean estado;
    private ArrayList<SesionClase> sesiones = new ArrayList<SesionClase>();

    public Grupo(int capacidad, int numero, boolean estado) {
        this.capacidad = capacidad;
        this.numero = numero;
        this.estado = estado;
    }

    public void modificarCapacidad(int nuevaCapacidad) {
        this.capacidad = nuevaCapacidad;
    }

    public ArrayList<SesionClase> getSesiones() {
        return sesiones;
    }

}
