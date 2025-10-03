package com.example.fantasticosback.util;

import com.example.fantasticosback.Model.Subject;

import java.util.HashMap;
import java.util.Map;

public class SubjectCatalog {
    private static Map<String, Subject> materias = new HashMap<>();

    static {
        materias.put("CALD", new Subject("101", "Cálculo Diferencial", 3, 1));
        materias.put("DDYA", new Subject("201", "Diseño de datos y Algoritmos", 4, 2));
        materias.put("AYSR", new Subject("301", "Arquitectura y Servicios de Red", 4, 5));
        materias.put("IPRO", new Subject("401", "Introducción a la programación", 3, 1));
        materias.put("DOPO", new Subject("501", "Desarrollo Orientado Por Objetos", 4, 5));
    }

    public static Subject getMateria(String codigo) {
        return materias.get(codigo);
    }

    public static void agregarMateria(String codigo, Subject subject) {
        materias.put(codigo, subject);
    }

    public static Map<String, Subject> getMaterias() {
        return materias;
    }
}