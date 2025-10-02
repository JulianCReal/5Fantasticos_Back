package com.example.fantasticosback.Controller;

import com.example.fantasticosback.Dtos.RegisterRequestDTO;
import com.example.fantasticosback.Dtos.ResponseDTO;
import com.example.fantasticosback.Dtos.UserResponseDTO;
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

            LoginResponse response = new LoginResponse(perfil, usuario.getRol());

            return ResponseEntity.ok(ResponseDTO.success(response, "Login successful"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(ResponseDTO.error("Invalid email or password"));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequestDTO registerRequest) {
        if (usuarioRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body(ResponseDTO.error("Email is already in use"));
        }

        Usuario nuevoUsuario = new Usuario(
                registerRequest.getEmail(),
                passwordEncoder.encode(registerRequest.getPassword()),
                registerRequest.getRol(),
                registerRequest.getPerfilId()
        );

        Usuario guardado = usuarioRepository.save(nuevoUsuario);

        UserResponseDTO response = new UserResponseDTO(
                guardado.getEmail(),
                guardado.getRol(),
                guardado.getPerfilId()
        );

        return ResponseEntity.ok(ResponseDTO.success(response, "User registered successfully"));
    }
}
