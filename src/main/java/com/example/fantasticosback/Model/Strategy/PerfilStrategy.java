package com.example.fantasticosback.Model.Strategy;

import com.example.fantasticosback.util.Rol;

public interface PerfilStrategy {
    Object obtenerPerfil(String perfilId);
    Rol getRol();
}
