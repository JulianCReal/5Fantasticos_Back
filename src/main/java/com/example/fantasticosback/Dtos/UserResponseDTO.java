package com.example.fantasticosback.Dtos;

import com.example.fantasticosback.util.Role;

public class UserResponseDTO {
    private String email;
    private Role role;
    private String profileId;

    public UserResponseDTO(String email, Role role, String profileId) {
        this.email = email;
        this.role = role;
        this.profileId = profileId;
    }

    public String getEmail() { return email; }
    public Role getRole() { return role; }
    public String getProfileId() { return profileId; }

    public void setEmail(String email) { this.email = email; }
    public void setRole(Role role) { this.role = role; }
    public void setProfileId(String profileId) { this.profileId = profileId; }
}
