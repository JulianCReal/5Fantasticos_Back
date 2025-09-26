package com.example.fantasticosback.Model.Strategy;

import com.example.fantasticosback.Server.DecanaturaService;
import com.example.fantasticosback.util.Rol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PerfilDecanoStrategy implements PerfilStrategy {

    @Autowired
    private DecanaturaService decanaturaService;

    @Override
    public Object obtenerPerfil(String perfilId) {
        return decanaturaService.obtenerPorId(perfilId);
    }

    @Override
    public Rol getRol() {
        return Rol.DECANO;
    }
}