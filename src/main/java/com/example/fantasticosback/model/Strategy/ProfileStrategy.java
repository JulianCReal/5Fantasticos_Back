package com.example.fantasticosback.model.Strategy;

import com.example.fantasticosback.enums.Role;

public interface ProfileStrategy {
    Object getProfile(String profileId);
    Role getRole();
}
