package com.example.fantasticosback.util;

public class LoginResponse {
    private Object perfil;
    private Rol rol;

    public LoginResponse(Object perfil, Rol rol) {
        this.perfil = perfil;
        this.rol = rol;
    }

    public Object getPerfil() {
        return perfil;
    }

    public Rol getRol() {
        return rol;
    }
}
