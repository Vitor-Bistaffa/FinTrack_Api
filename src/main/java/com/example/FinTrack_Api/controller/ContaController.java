package com.example.FinTrack_Api.controller;

import com.example.FinTrack_Api.dto.request.conta.*;
import com.example.FinTrack_Api.model.Conta;
import com.example.FinTrack_Api.model.Usuario;
import com.example.FinTrack_Api.repository.ContaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/conta")
public class ContaController {
    @Autowired
    private ContaRepository contaRepository;

    @PostMapping
    @Transactional
    public void cadastrar(@RequestBody DadosCadastroConta dados, @AuthenticationPrincipal Usuario usuario) {
        contaRepository.save(new Conta(dados,usuario));
    }

    @GetMapping
    public List<DadosListagemConta> listar(@AuthenticationPrincipal Usuario usuario) {
        return contaRepository.findByExcluidoFalseAndUsuarioIs(usuario)
                .stream().map(DadosListagemConta::new).toList();
    }

    @PutMapping
    @Transactional
    public void atualizar(@RequestBody DadosAtualizarConta dados) {
        var conta = contaRepository.getReferenceById(dados.id());
        conta.atualizarInformacoes(dados);
    }

    @PutMapping("/remover")
    @Transactional
    public void remover(@RequestBody DadosRemoverConta dados) {
        var conta = contaRepository.getReferenceById(dados.id());
        conta.remover(dados);
    }

    @PutMapping("/restaurar")
    @Transactional
    public void restaurar(@RequestBody DadosRestaurarConta dados) {
        var conta = contaRepository.getReferenceById(dados.id());
        conta.restaurar(dados);
    }


}
