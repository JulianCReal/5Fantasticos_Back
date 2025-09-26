package com.example.fantasticosback.Model.Strategy;

import com.example.fantasticosback.Controller.StudentController;
import com.example.fantasticosback.Server.StudentService;
import com.example.fantasticosback.util.Rol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class PerfilEstudianteStrategy implements PerfilStrategy {

    @Autowired
    private StudentService studentService;

    @Override
    public Object obtenerPerfil(String perfilId) {
        return studentService.obtenerPorId(perfilId);
    }

    @Override
    public Rol getRol() {
        return Rol.ESTUDIANTE;
    }
}
