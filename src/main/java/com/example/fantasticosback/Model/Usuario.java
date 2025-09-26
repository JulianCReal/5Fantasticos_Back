package com.example.fantasticosback.Model;



import com.example.fantasticosback.util.Rol;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Usuarios")
public class Usuario {

    @Id
    private String idUsuario;

    private String email;
    private String password;
    private Rol rol;
    private String perfilId;


    public Usuario() {}

    public Usuario(String email, String password, Rol rol, String perfilId) {
        this.email = email;
        this.password = password;
        this.rol = rol;
        this.perfilId = perfilId;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Rol getRol() {
        return rol;
    }

    public String getPerfilId() {
        return perfilId;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setRol(Rol rol) {
        this.rol = rol;
    }
    public void setPerfilId(String perfilId) {
        this.perfilId = perfilId;
    }
}
