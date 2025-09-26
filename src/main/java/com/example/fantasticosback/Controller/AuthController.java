package com.example.fantasticosback.Controller;

import com.example.fantasticosback.Model.Usuario;
import com.example.fantasticosback.Repository.UserRepository;
import com.example.fantasticosback.Server.MatcherService;
import com.example.fantasticosback.util.LoginRequest;
import com.example.fantasticosback.util.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private MatcherService matcherService;
    @Autowired
    private UserRepository usuarioRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            Object perfil = matcherService.autenticarYObtenerPerfil(loginRequest.getEmail(), loginRequest.getPassword());

            Usuario usuario = matcherService.obtenerUsuario(loginRequest.getEmail());

            return ResponseEntity.ok(new LoginResponse(perfil, usuario.getRol()));

        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Usuario usuario) {
        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("El correo ya está en uso");
        }

        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        Usuario guardado = usuarioRepository.save(usuario);
        guardado = new Usuario(guardado.getEmail(), null, guardado.getRol(), guardado.getPerfilId());

        return ResponseEntity.ok(guardado);
    }
}
