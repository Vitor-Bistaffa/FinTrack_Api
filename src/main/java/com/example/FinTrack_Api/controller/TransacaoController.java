package com.example.FinTrack_Api.controller;

import com.example.FinTrack_Api.dto.request.transacao.DadosAtualizarTransacao;
import com.example.FinTrack_Api.dto.request.transacao.DadosCadastroTransacao;
import com.example.FinTrack_Api.dto.request.transacao.DadosListagemTransacao;
import com.example.FinTrack_Api.dto.request.transacao.DadosTotalMes;
import com.example.FinTrack_Api.model.Categoria;
import com.example.FinTrack_Api.model.Conta;
import com.example.FinTrack_Api.model.Transacao;
import com.example.FinTrack_Api.model.enums.TipoTransacao;
import com.example.FinTrack_Api.repository.CategoriaRepository;
import com.example.FinTrack_Api.repository.ContaRepository;
import com.example.FinTrack_Api.repository.TransacaoRepository;
import com.example.FinTrack_Api.service.TransacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transacao")
public class TransacaoController {
    @Autowired
    private TransacaoRepository transacaoRepository;
    @Autowired
    private ContaRepository contaRepository;
    @Autowired
    private CategoriaRepository categoriaRepository;
    @Autowired
    private TransacaoService transacaoService;

    @PostMapping
    @Transactional
    public void cadastrar(@RequestBody DadosCadastroTransacao dados) {
        Conta conta = contaRepository.findById(dados.conta()).orElseThrow(() -> new RuntimeException("Conta inexistente"));

        Categoria categoria = categoriaRepository.findById(dados.categoria()).orElseThrow(() -> new RuntimeException("Categoria inexistente"));

        transacaoRepository.save(new Transacao(dados, conta, categoria));
    }

    @GetMapping
    public List<DadosListagemTransacao> listar(@RequestParam(required = false) Long id) {
        return transacaoService.listarTransacoes(id);
    }

    @GetMapping("/total")
    public List<DadosTotalMes> obterTotaisMensais(@RequestParam TipoTransacao tipo, @RequestParam Integer ano) {
        return transacaoService.calcularTotaisMensais(tipo, ano);
    }

    @PutMapping
    @Transactional
    public void atualizar(@RequestBody DadosAtualizarTransacao dados) {
        Conta conta = contaRepository.findById(dados.conta()).orElseThrow(() -> new RuntimeException("Conta inexistente"));

        Categoria categoria = categoriaRepository.findById(dados.categoria()).orElseThrow(() -> new RuntimeException("Categoria inexistente"));
        var transacao = transacaoRepository.getReferenceById(dados.id());
        transacao.atualizarInformacoes(dados, conta, categoria);
    }

    @DeleteMapping
    @Transactional
    public void deletar(@RequestBody DadosAtualizarTransacao dados) {
        transacaoRepository.deleteById(dados.id());
    }
}
