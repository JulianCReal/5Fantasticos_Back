package com.example.fantasticosback.Dtos;

import com.example.fantasticosback.util.Rol;

public class UserResponseDTO {
    private String email;
    private Rol rol;
    private String perfilId;

    public UserResponseDTO(String email, Rol rol, String perfilId) {
        this.email = email;
        this.rol = rol;
        this.perfilId = perfilId;
    }

    public String getEmail() { return email; }
    public Rol getRol() { return rol; }
    public String getPerfilId() { return perfilId; }

    public void setEmail(String email) { this.email = email; }
    public void setRol(Rol rol) { this.rol = rol; }
    public void setPerfilId(String perfilId) { this.perfilId = perfilId; }
}
