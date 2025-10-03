package com.example.fantasticosback.Server;


import com.example.fantasticosback.Model.User;
import com.example.fantasticosback.Repository.UserRepository;

import com.example.fantasticosback.Model.Strategy.ProfileStrategy;
import com.example.fantasticosback.Model.Factory.ProfileStrategyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MatcherService {

    @Autowired
    private UserRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ProfileStrategyFactory strategyFactory;

    public Object autenticarYObtenerPerfil(String email, String rawPassword) {
        User user = usuarioRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        ProfileStrategy estrategia = strategyFactory.getStrategy(user.getRol());
        return estrategia.obtainProfile(user.getProfileId());
    }

    public User obtenerUsuario(String email) {
        return usuarioRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }
}
