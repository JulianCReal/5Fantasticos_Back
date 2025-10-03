package com.example.fantasticosback.Model.Strategy;

import com.example.fantasticosback.Server.StudentService;
import com.example.fantasticosback.util.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class StudentProfileStrategy implements ProfileStrategy {

    @Autowired
    private StudentService studentService;

    @Override
    public Object obtainProfile(String perfilId) {
        return studentService.obtenerPorId(perfilId);
    }

    @Override
    public Role getRol() {
        return Role.ESTUDIANTE;
    }
}
