package com.example.fantasticosback.Server;


import com.example.fantasticosback.Repository.UserRepository;
import com.example.fantasticosback.Model.Usuario;

import com.example.fantasticosback.Model.Strategy.PerfilStrategy;
import com.example.fantasticosback.Model.Factory.PerfilStrategyFactory;
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
    private PerfilStrategyFactory strategyFactory;

    public Object autenticarYObtenerPerfil(String email, String rawPassword) {
        Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!passwordEncoder.matches(rawPassword, usuario.getPassword())) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        PerfilStrategy estrategia = strategyFactory.getStrategy(usuario.getRol());
        return estrategia.obtenerPerfil(usuario.getPerfilId());
    }

    public Usuario obtenerUsuario(String email) {
        return usuarioRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }
}
