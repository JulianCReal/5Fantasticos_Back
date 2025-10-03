package com.example.fantasticosback.Model.Strategy;

import com.example.fantasticosback.util.Role;

public interface ProfileStrategy {
    Object obtainProfile(String perfilId);
    Role getRol();
}
