package com.example.fantasticosback.Model.Strategy;

import com.example.fantasticosback.util.Enums.Role;

public interface ProfileStrategy {
    Object getProfile(String profileId);
    Role getRole();
}
