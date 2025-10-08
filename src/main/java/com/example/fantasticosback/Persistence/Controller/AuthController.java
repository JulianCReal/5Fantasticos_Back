package com.example.fantasticosback.Persistence.Controller;

import com.example.fantasticosback.Dtos.RegisterRequestDTO;
import com.example.fantasticosback.Dtos.ResponseDTO;
import com.example.fantasticosback.Dtos.UserResponseDTO;
import com.example.fantasticosback.Model.Entities.User;
import com.example.fantasticosback.Persistence.Repository.UserRepository;
import com.example.fantasticosback.Persistence.Server.MatcherService;
import com.example.fantasticosback.util.JwtTokenUtil;
import com.example.fantasticosback.util.LoginRequest;
import com.example.fantasticosback.util.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private MatcherService matcherService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            Object profile = matcherService.authenticateAndGetProfile(loginRequest.getEmail(), loginRequest.getPassword());
            User user = matcherService.getUser(loginRequest.getEmail());

            String token = jwtTokenUtil.generateToken(user.getEmail(), user.getRole().toString());

            LoginResponse response = new LoginResponse(profile, user.getRole(), token);

            return ResponseEntity.ok(ResponseDTO.success(response, "Login successful"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(ResponseDTO.error("Invalid email or password"));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequestDTO registerRequest) {
        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body(ResponseDTO.error("Email is already in use"));
        }

        User newUser = new User(
                registerRequest.getEmail(),
                passwordEncoder.encode(registerRequest.getPassword()),
                registerRequest.getRole(),
                registerRequest.getProfileId()
        );

        User saved = userRepository.save(newUser);

        UserResponseDTO response = new UserResponseDTO(
                saved.getEmail(),
                saved.getRole(),
                saved.getProfileId()
        );

        return ResponseEntity.ok(ResponseDTO.success(response, "User registered successfully"));
    }
}
