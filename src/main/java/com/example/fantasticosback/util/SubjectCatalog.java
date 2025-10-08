package com.example.fantasticosback.util;

import com.example.fantasticosback.Model.Entities.Subject;

import java.util.HashMap;
import java.util.Map;

public class SubjectCatalog {
    private static Map<String, Subject> subjects = new HashMap<>();

    static {
        subjects.put("CALD", new Subject("101", "Cálculo Diferencial", 3, 1));
        subjects.put("DDYA", new Subject("201", "Diseño de Datos y Algoritmos", 4, 2));
        subjects.put("AYSR", new Subject("301", "Arquitectura y Servicios de Red", 4, 5));
        subjects.put("IPRO", new Subject("401", "Introducción a la Programación", 3, 1));
        subjects.put("DOPO", new Subject("501", "Desarrollo Orientado a Objetos", 4, 5));
    }

    public static Subject getSubject(String code) {
        return subjects.get(code);
    }

    public static void addSubject(String code, Subject subject) {
        subjects.put(code, subject);
    }

    public static Map<String, Subject> getSubjects() {
        return subjects;
    }
}