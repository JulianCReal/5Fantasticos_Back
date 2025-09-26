package com.example.fantasticosback.Model.Factory;

import com.example.fantasticosback.Model.Strategy.PerfilStrategy;
import com.example.fantasticosback.util.Rol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PerfilStrategyFactory {

    private final Map<Rol, PerfilStrategy> estrategiaPorRol = new HashMap<>();

    @Autowired
    public PerfilStrategyFactory(List<PerfilStrategy> estrategias) {
        for (PerfilStrategy estrategia : estrategias) {
            estrategiaPorRol.put(estrategia.getRol(), estrategia);
        }
    }

    public PerfilStrategy getStrategy(Rol rol) {
        PerfilStrategy estrategia = estrategiaPorRol.get(rol);
        if (estrategia == null) {
            throw new IllegalArgumentException("No hay estrategia para el rol: " + rol);
        }
        return estrategia;
    }
}
