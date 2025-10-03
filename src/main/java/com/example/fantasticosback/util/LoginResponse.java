package com.example.fantasticosback.util;

public class LoginResponse {
    private Object perfil;
    private Role role;

    public LoginResponse(Object perfil, Role role) {
        this.perfil = perfil;
        this.role = role;
    }

    public Object getPerfil() {
        return perfil;
    }

    public Role getRol() {
        return role;
    }
}
