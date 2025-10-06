package com.example.fantasticosback.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class JwtTokenUtilTest {

    private JwtTokenUtil jwtTokenUtil;
    private final String testEmail = "test@example.com";
    private final String testRole = "STUDENT";

    @BeforeEach
    void setUp() {
        jwtTokenUtil = new JwtTokenUtil();
    }

    @Test
    void testGenerateToken() {
        // Generar token
        String token = jwtTokenUtil.generateToken(testEmail, testRole);

        // Verificar que el token no sea nulo ni vacío
        assertNotNull(token);
        assertFalse(token.isEmpty());

        // Verificar que el token tenga la estructura JWT (3 partes separadas por puntos)
        String[] tokenParts = token.split("\\.");
        assertEquals(3, tokenParts.length, "El token JWT debe tener 3 partes");
    }

    @Test
    void testExtractEmail() {
        // Generar token
        String token = jwtTokenUtil.generateToken(testEmail, testRole);

        // Extraer email del token
        String extractedEmail = jwtTokenUtil.extractEmail(token);

        // Verificar que el email extraído sea correcto
        assertEquals(testEmail, extractedEmail);
    }

    @Test
    void testExtractRole() {
        // Generar token
        String token = jwtTokenUtil.generateToken(testEmail, testRole);

        // Extraer rol del token
        String extractedRole = jwtTokenUtil.extractRole(token);

        // Verificar que el rol extraído sea correcto
        assertEquals(testRole, extractedRole);
    }

    @Test
    void testValidateTokenWithValidToken() {
        // Generar token
        String token = jwtTokenUtil.generateToken(testEmail, testRole);

        // Validar token con el email correcto
        boolean isValid = jwtTokenUtil.validateToken(token, testEmail);

        // Verificar que el token sea válido
        assertTrue(isValid);
    }

    @Test
    void testValidateTokenWithWrongEmail() {
        // Generar token
        String token = jwtTokenUtil.generateToken(testEmail, testRole);

        // Validar token con email incorrecto
        boolean isValid = jwtTokenUtil.validateToken(token, "wrong@example.com");

        // Verificar que el token sea inválido
        assertFalse(isValid);
    }

    @Test
    void testIsTokenExpired() {
        // Generar token reciente
        String token = jwtTokenUtil.generateToken(testEmail, testRole);

        // Verificar que el token no esté expirado
        boolean isExpired = jwtTokenUtil.isTokenExpired(token);
        assertFalse(isExpired);
    }

    @Test
    void testTokenContainsCorrectClaims() {
        // Generar token
        String token = jwtTokenUtil.generateToken(testEmail, testRole);

        // Verificar todos los claims
        assertEquals(testEmail, jwtTokenUtil.extractEmail(token));
        assertEquals(testRole, jwtTokenUtil.extractRole(token));
        assertFalse(jwtTokenUtil.isTokenExpired(token));
    }

    @Test
    void testGenerateTokenWithDifferentRoles() {
        // Probar con diferentes roles
        String[] roles = {"STUDENT", "TEACHER", "DEAN_OFFICE"};

        for (String role : roles) {
            String token = jwtTokenUtil.generateToken(testEmail, role);

            assertNotNull(token);
            assertEquals(testEmail, jwtTokenUtil.extractEmail(token));
            assertEquals(role, jwtTokenUtil.extractRole(token));
            assertTrue(jwtTokenUtil.validateToken(token, testEmail));
        }
    }

    @Test
    void testInvalidTokenThrowsException() {
        // Token inválido
        String invalidToken = "invalid.token.here";

        // Verificar que se lance excepción al procesar token inválido
        assertThrows(Exception.class, () -> {
            jwtTokenUtil.extractEmail(invalidToken);
        });

        assertThrows(Exception.class, () -> {
            jwtTokenUtil.extractRole(invalidToken);
        });

        assertThrows(Exception.class, () -> {
            jwtTokenUtil.isTokenExpired(invalidToken);
        });
    }

    @Test
    void testValidateTokenWithInvalidToken() {
        // Token inválido
        String invalidToken = "invalid.token.here";

        // validateToken debería manejar la excepción y retornar false
        assertThrows(Exception.class, () -> {
            jwtTokenUtil.validateToken(invalidToken, testEmail);
        });
    }
}
