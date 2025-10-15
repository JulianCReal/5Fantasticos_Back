package com.example.fantasticosback.model.Factory;

import com.example.fantasticosback.model.Strategy.ProfileStrategy;
import com.example.fantasticosback.enums.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ProfileStrategyFactory {

    private final Map<Role, ProfileStrategy> strategyByRole = new HashMap<>();

    @Autowired
    public ProfileStrategyFactory(List<ProfileStrategy> strategies) {
        for (ProfileStrategy strategy : strategies) {
            strategyByRole.put(strategy.getRole(), strategy);
        }
    }

    public ProfileStrategy getStrategy(Role role) {
        ProfileStrategy strategy = strategyByRole.get(role);
        if (strategy == null) {
            throw new IllegalArgumentException("No strategy available for role: " + role);
        }
        return strategy;
    }
}
