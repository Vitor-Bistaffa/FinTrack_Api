package com.example.FinTrack_Api.dto.request.transacao;

import com.example.FinTrack_Api.model.Transacao;

import java.math.BigDecimal;

public record DadosListagemTransacao(Long id, BigDecimal valor) {
    public DadosListagemTransacao(Transacao transacao){
        this(transacao.getId(),transacao.getValor());
    }
}
