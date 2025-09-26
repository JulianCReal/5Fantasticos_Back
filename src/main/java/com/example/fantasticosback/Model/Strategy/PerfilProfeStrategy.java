package com.example.fantasticosback.Model.Strategy;

import com.example.fantasticosback.Server.TeacherService;
import com.example.fantasticosback.util.Rol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PerfilProfeStrategy implements PerfilStrategy {

    @Autowired
    private TeacherService teacherService;

    @Override
    public Object obtenerPerfil(String perfilId) {
        return teacherService.obtenerPorId(perfilId);
    }

    @Override
    public Rol getRol() {
        return Rol.PROFESOR;
    }
}