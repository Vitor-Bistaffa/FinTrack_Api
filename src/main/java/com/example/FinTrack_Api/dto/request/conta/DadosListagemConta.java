package com.example.FinTrack_Api.dto.request.conta;

import com.example.FinTrack_Api.model.Conta;

public record DadosListagemConta(Long id, String nome) {
    public DadosListagemConta(Conta conta) {
        this(conta.getId(), conta.getNome());
    }
}
