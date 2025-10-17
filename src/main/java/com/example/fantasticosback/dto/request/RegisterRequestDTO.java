package com.example.fantasticosback.dto.request;

import com.example.fantasticosback.enums.Role;

public class RegisterRequestDTO {
    private String email;
    private String password;
    private Role role;
    private String profileId;

    public RegisterRequestDTO() {}

    public RegisterRequestDTO(String email, String password, Role role, String profileId) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.profileId = profileId;
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public String getProfileId() { return profileId; }
    public void setProfileId(String profileId) { this.profileId = profileId; }
}
