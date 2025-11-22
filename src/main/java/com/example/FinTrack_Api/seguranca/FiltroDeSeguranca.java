package com.example.FinTrack_Api.seguranca;

import com.example.FinTrack_Api.repository.UsuarioRepository;
import com.example.FinTrack_Api.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
public class FiltroDeSeguranca extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var tokenJWT = recuperarToken(request);

        if (tokenJWT != null) {

            var subsject = tokenService.getSubject(tokenJWT);

            var usuario = usuarioRepository.findByLogin(subsject);

            var authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private String recuperarToken(HttpServletRequest request) {
        var cabecalho = request.getHeader("Authorization");
        if (cabecalho != null) {
            return cabecalho.replace("Bearer ", "");

        }
        return null;

    }
}
