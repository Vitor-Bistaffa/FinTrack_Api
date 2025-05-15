package com.example.FinTrack_Api.controller;

import com.example.FinTrack_Api.dto.request.conta.DadosCadastroConta;
import com.example.FinTrack_Api.dto.request.conta.DadosListagemConta;
import com.example.FinTrack_Api.model.Conta;
import com.example.FinTrack_Api.repository.ContaRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
    public void cadastrar(@RequestBody DadosCadastroConta dados) {
        contaRepository.save(new Conta(dados));
    }

    @GetMapping
    public List<DadosListagemConta> listar() {
        return contaRepository.findByExcluidoFalse()
                .stream().map(DadosListagemConta::new).toList();    }

    @PutMapping
    @Transactional
    public void atualizar(@RequestBody DadosCadastroConta dados) {
        var conta = contaRepository.getReferenceById(dados.id());
        conta.atualizarInformacoes(dados);
    }

    @PutMapping("/excluir")
    @Transactional
    public void remover(@RequestBody DadosCadastroConta dados) {
        var conta = contaRepository.getReferenceById(dados.id());
        conta.deletar(dados);
    }

    @PutMapping("/restaurar")
    @Transactional
    public void restaurar(@RequestBody DadosCadastroConta dados) {
        var conta = contaRepository.getReferenceById(dados.id());
        conta.restaurar(dados);
    }



}
