package com.example.FinTrack_Api.service;

import com.example.FinTrack_Api.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class TokenServiceTest {

    TokenService tokenService;

    @BeforeEach
    void setup() {
        tokenService = new TokenService();
        // injeta o secret manualmente, porque @Value não funciona em teste unitário puro
        ReflectionTestUtils.setField(tokenService, "secret", "123456");
    }

    @Test
    void gerarToken() {
        Usuario usuario = new Usuario(1L, "admin", "senha");

        String token = tokenService.gerarToken(usuario);

        assertNotNull(token);
    }

    @Test
    void getSubject() {
        Usuario usuario = new Usuario(1L, "admin", "senha");

        String token = tokenService.gerarToken(usuario);

        String subject = tokenService.getSubject(token);

        assertEquals("admin", subject);
    }
}