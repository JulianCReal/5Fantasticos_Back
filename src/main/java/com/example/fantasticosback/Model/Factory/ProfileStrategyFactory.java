package com.example.fantasticosback.Model.Factory;

import com.example.fantasticosback.Model.Strategy.ProfileStrategy;
import com.example.fantasticosback.util.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ProfileStrategyFactory {

    private final Map<Role, ProfileStrategy> estrategyByRole = new HashMap<>();

    @Autowired
    public ProfileStrategyFactory(List<ProfileStrategy> estrategias) {
        for (ProfileStrategy estrategia : estrategias) {
            estrategyByRole.put(estrategia.getRol(), estrategia);
        }
    }

    public ProfileStrategy getStrategy(Role role) {
        ProfileStrategy estrategia = estrategyByRole.get(role);
        if (estrategia == null) {
            throw new IllegalArgumentException("No hay estrategia para el rol: " + role);
        }
        return estrategia;
    }
}
