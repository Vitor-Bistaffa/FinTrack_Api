package com.example.FinTrack_Api.controller;

import com.example.FinTrack_Api.dto.request.usuario.DadosAutenticacao;
import com.example.FinTrack_Api.model.Transacao;
import com.example.FinTrack_Api.seguranca.DadosTokenJWT;
import com.example.FinTrack_Api.model.Usuario;
import com.example.FinTrack_Api.service.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/login")
public class AutenticacaoController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenService tokenService;

    @PostMapping
    public ResponseEntity autenticar(@RequestBody @Valid DadosAutenticacao dados) {

        var AuthenticationToken = new UsernamePasswordAuthenticationToken(dados.login(), dados.senha());
        var authentication = authenticationManager.authenticate(AuthenticationToken);

        var token = tokenService.gerarToken((Usuario) authentication.getPrincipal());

        return ResponseEntity.ok(new DadosTokenJWT(token));
    }
}
