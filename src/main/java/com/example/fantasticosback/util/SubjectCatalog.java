package com.example.fantasticosback.util;

import com.example.fantasticosback.model.Document.Subject;

import java.util.HashMap;
import java.util.Map;

public class SubjectCatalog {
    private static Map<String, Subject> subjects = new HashMap<>();

    static {
        // Construir Subjects usando el constructor por defecto y setters
        Subject s = new Subject();
        s.setSubjectId("101");
        s.setCode("CALD");
        s.setName("Cálculo Diferencial");
        s.setCredits(3);
        s.setSemester(1);
        subjects.put("CALD", s);

        s = new Subject();
        s.setSubjectId("201");
        s.setCode("DDYA");
        s.setName("Diseño de Datos y Algoritmos");
        s.setCredits(4);
        s.setSemester(2);
        subjects.put("DDYA", s);

        s = new Subject();
        s.setSubjectId("301");
        s.setCode("AYSR");
        s.setName("Arquitectura y Servicios de Red");
        s.setCredits(4);
        s.setSemester(5);
        subjects.put("AYSR", s);

        s = new Subject();
        s.setSubjectId("401");
        s.setCode("IPRO");
        s.setName("Introducción a la Programación");
        s.setCredits(3);
        s.setSemester(1);
        subjects.put("IPRO", s);

        s = new Subject();
        s.setSubjectId("501");
        s.setCode("DOPO");
        s.setName("Desarrollo Orientado a Objetos");
        s.setCredits(4);
        s.setSemester(5);
        subjects.put("DOPO", s);
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