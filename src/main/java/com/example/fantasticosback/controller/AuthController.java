package com.example.fantasticosback.controller;

import com.example.fantasticosback.dto.request.RegisterRequestDTO;
import com.example.fantasticosback.dto.response.ResponseDTO;
import com.example.fantasticosback.dto.response.UserResponseDTO;
import com.example.fantasticosback.model.Document.User;
import com.example.fantasticosback.repository.UserRepository;
import com.example.fantasticosback.service.MatcherService;
import com.example.fantasticosback.util.JwtTokenUtil;
import com.example.fantasticosback.util.LoginRequest;
import com.example.fantasticosback.util.LoginResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@Tag(
    name = "Autenticación",
    description = "Endpoints para autenticación y registro de usuarios en el sistema"
)
@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AuthController {

    private final MatcherService matcherService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;

    @Operation(
        summary = "Iniciar sesión",
        description = "Permite a un usuario autenticarse en el sistema con su email y contraseña. " +
                     "Retorna el perfil del usuario, su rol y un token JWT para autenticación en endpoints protegidos."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Login exitoso",
            content = @Content(schema = @Schema(implementation = LoginResponse.class))
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Credenciales inválidas - Email o contraseña incorrectos"
        )
    })
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

    @Operation(
        summary = "Registrar nuevo usuario",
        description = "Permite registrar un nuevo usuario en el sistema. " +
                     "Se debe especificar el email, contraseña, rol (STUDENT, TEACHER, DEAN_OFFICE) y el ID del perfil asociado."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Usuario registrado exitosamente",
            content = @Content(schema = @Schema(implementation = UserResponseDTO.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Email ya está en uso o datos inválidos"
        )
    })
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

    @Operation(
        summary = "Eliminar usuario por correo",
        description = "Elimina un usuario del sistema utilizando su dirección de correo electrónico"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Usuario eliminado exitosamente"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Usuario no encontrado"
        )
    })
    @DeleteMapping("/delete/{email}")
    public ResponseEntity<ResponseDTO<Void>> deleteUserByEmail(@PathVariable String email) {
        if (userRepository.findByEmail(email).isEmpty()) {
            return ResponseEntity.status(404).body(ResponseDTO.error("User not found"));
        }
        
        userRepository.deleteByEmail(email);
        return ResponseEntity.ok(ResponseDTO.success(null, "User deleted successfully"));
    }
}
