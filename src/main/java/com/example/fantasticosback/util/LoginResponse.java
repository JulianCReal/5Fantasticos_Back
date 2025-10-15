package com.example.fantasticosback.util;

import com.example.fantasticosback.util.Enums.Role;

public class LoginResponse {
    private Object profile;
    private Role role;
    private String token;
    private String tokenType = "Bearer";

    public LoginResponse(Object profile, Role role, String token) {
        this.profile = profile;
        this.role = role;
        this.token = token;
    }

    public Object getProfile() {
        return profile;
    }

    public Role getRole() {
        return role;
    }

    public String getToken() {
        return token;
    }

    public String getTokenType() {
        return tokenType;
    }
}
