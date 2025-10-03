package com.example.fantasticosback.Model.Strategy;

import com.example.fantasticosback.Server.ProfessorService;
import com.example.fantasticosback.util.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfessorProfileStrategy implements ProfileStrategy {

    @Autowired
    private ProfessorService professorService;

    @Override
    public Object obtainProfile(String perfilId) {
        return professorService.obtenerPorId(perfilId);
    }

    @Override
    public Role getRol() {
        return Role.PROFESOR;
    }
}