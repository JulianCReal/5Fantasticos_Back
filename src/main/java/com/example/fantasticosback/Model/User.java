package com.example.fantasticosback.Model;



import com.example.fantasticosback.util.Role;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Usuarios")
public class User {

    @Id
    private String idUsuario;

    private String email;
    private String password;
    private Role role;
    private String profileId;


    public User() {}

    public User(String email, String password, Role role, String profileId) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.profileId = profileId;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Role getRol() {
        return role;
    }

    public String getProfileId() {
        return profileId;
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
    public void setRol(Role role) {
        this.role = role;
    }
    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }
}
