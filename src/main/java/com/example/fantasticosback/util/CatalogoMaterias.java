package com.example.fantasticosback.util;

import com.example.fantasticosback.Model.Materia;

import java.util.HashMap;
import java.util.Map;

public class CatalogoMaterias {
    private static Map<String, Materia> materias = new HashMap<>();

    static {
        materias.put("CALD", new Materia(101, "Cálculo Diferencial", 3, 1));
        materias.put("DDYA", new Materia(201, "Diseño de datos y Algoritmos", 4, 2));
        materias.put("AYSR", new Materia(301, "Arquitectura y Servicios de Red", 4, 5));
        materias.put("IPRO", new Materia(401, "Introducción a la programación", 3, 1));
        materias.put("DOPO", new Materia(501, "Desarrollo Orientado Por Objetos", 4, 5));
    }

    public static Materia getMateria(String codigo) {
        return materias.get(codigo);
    }

    public static void agregarMateria(String codigo, Materia materia) {
        materias.put(codigo, materia);
    }

    public static Map<String, Materia> getMaterias() {
        return materias;
    }
}