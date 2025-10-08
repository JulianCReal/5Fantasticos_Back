package com.example.fantasticosback.Model.Entities;
import com.example.fantasticosback.util.Role;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Users")
public class User {

    @Id
    private String userId;

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

    public Role getRole() {
        return role;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }
}
