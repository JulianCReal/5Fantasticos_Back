package com.example.fantasticosback.Controller;

import com.example.fantasticosback.Dtos.RegisterRequestDTO;
import com.example.fantasticosback.Dtos.ResponseDTO;
import com.example.fantasticosback.Dtos.UserResponseDTO;
import com.example.fantasticosback.Model.User;
import com.example.fantasticosback.Repository.UserRepository;
import com.example.fantasticosback.Server.MatcherService;
import com.example.fantasticosback.util.LoginRequest;
import com.example.fantasticosback.util.LoginResponse;
import com.example.fantasticosback.util.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthControllerTest {
    @Mock
    private MatcherService matcherService;
    @Mock
    private UserRepository usuarioRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLoginSuccess() {
        LoginRequest loginRequest = new LoginRequest("test@mail.com", "1234");
        Object perfil = new Object();
        User user = new User("test@mail.com", "encoded", Role.ESTUDIANTE, "E001");
        LoginResponse loginResponse = new LoginResponse(perfil, user.getRol());

        when(matcherService.autenticarYObtenerPerfil("test@mail.com", "1234")).thenReturn(perfil);
        when(matcherService.obtenerUsuario("test@mail.com")).thenReturn(user);

        ResponseEntity<?> response = authController.login(loginRequest);
        assertEquals(200, response.getStatusCode().value());
        assertTrue(response.getBody() instanceof ResponseDTO);
        ResponseDTO<?> body = (ResponseDTO<?>) response.getBody();
        assertEquals("Success", body.getStatus());
        assertEquals("Login successful", body.getMessage());
        assertTrue(body.getData() instanceof LoginResponse);
        LoginResponse data = (LoginResponse) body.getData();
        assertEquals(user.getRol(), data.getRol());
    }

    @Test
    void testLoginFail() {
        LoginRequest loginRequest = new LoginRequest("fail@mail.com", "wrong");
        when(matcherService.autenticarYObtenerPerfil(anyString(), anyString())).thenThrow(new RuntimeException());
        ResponseEntity<?> response = authController.login(loginRequest);
        assertEquals(401, response.getStatusCode().value());
        assertTrue(response.getBody() instanceof ResponseDTO);
        ResponseDTO<?> body = (ResponseDTO<?>) response.getBody();
        assertEquals("Error", body.getStatus());
        assertEquals("Invalid email or password", body.getMessage());
        assertNull(body.getData());
    }

    @Test
    void testRegisterSuccess() {
        RegisterRequestDTO registerRequest = new RegisterRequestDTO("test@mail.com", "1234", Role.ESTUDIANTE, "E001");
        when(usuarioRepository.findByEmail("test@mail.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("1234")).thenReturn("encoded");
        User user = new User("test@mail.com", "encoded", Role.ESTUDIANTE, "E001");
        when(usuarioRepository.save(any(User.class))).thenReturn(user);
        ResponseEntity<?> response = authController.register(registerRequest);
        assertEquals(200, response.getStatusCode().value());
        assertTrue(response.getBody() instanceof ResponseDTO);
        ResponseDTO<?> body = (ResponseDTO<?>) response.getBody();
        assertEquals("Success", body.getStatus());
        assertEquals("User registered successfully", body.getMessage());
        assertTrue(body.getData() instanceof UserResponseDTO);
        UserResponseDTO data = (UserResponseDTO) body.getData();
        assertEquals("test@mail.com", data.getEmail());
        assertEquals(Role.ESTUDIANTE, data.getRol());
        assertEquals("E001", data.getProfileId());
    }

    @Test
    void testRegisterEmailInUse() {
        RegisterRequestDTO registerRequest = new RegisterRequestDTO("test@mail.com", "1234", Role.ESTUDIANTE, "E001");
        when(usuarioRepository.findByEmail("test@mail.com")).thenReturn(Optional.of(new User()));
        ResponseEntity<?> response = authController.register(registerRequest);
        assertEquals(400, response.getStatusCode().value());
        assertTrue(response.getBody() instanceof ResponseDTO);
        ResponseDTO<?> body = (ResponseDTO<?>) response.getBody();
        assertEquals("Error", body.getStatus());
        assertEquals("Email is already in use", body.getMessage());
        assertNull(body.getData());
    }
}
