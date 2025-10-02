package com.example.fantasticosback.Dtos;

import com.example.fantasticosback.util.Rol;

public class RegisterRequestDTO {
    private String email;
    private String password;
    private Rol rol;
    private String perfilId;

    public RegisterRequestDTO() {}

    public RegisterRequestDTO(String email, String password, Rol rol, String perfilId) {
        this.email = email;
        this.password = password;
        this.rol = rol;
        this.perfilId = perfilId;
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Rol getRol() { return rol; }
    public void setRol(Rol rol) { this.rol = rol; }

    public String getPerfilId() { return perfilId; }
    public void setPerfilId(String perfilId) { this.perfilId = perfilId; }
}
