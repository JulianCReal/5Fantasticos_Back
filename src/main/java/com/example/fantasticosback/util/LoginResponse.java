package com.example.fantasticosback.util;

public class LoginResponse {
    private Object profile;
    private Role role;

    public LoginResponse(Object profile, Role role) {
        this.profile = profile;
        this.role = role;
    }

    public Object getProfile() {
        return profile;
    }

    public Role getRole() {
        return role;
    }
}
