package com.example.FinTrack_Api.service;

import com.example.FinTrack_Api.model.Usuario;
import com.example.FinTrack_Api.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Transactional
@AutoConfigureMockMvc(addFilters = false)
class AutenticacaoServiceTest {

    @MockitoBean
    UsuarioRepository usuarioRepository;

    @Autowired
    AutenticacaoService autenticacaoService;

    @Test
    void loadUserByUsername() {
        Usuario usuario = new Usuario(1L, "admin", "123");
        when(usuarioRepository.findByLogin("admin")).thenReturn(usuario);

        UserDetails user = autenticacaoService.loadUserByUsername("admin");

        assertEquals("admin", user.getUsername());
    }

}