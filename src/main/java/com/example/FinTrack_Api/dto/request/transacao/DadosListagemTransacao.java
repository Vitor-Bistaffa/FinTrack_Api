package com.example.FinTrack_Api.dto.request.transacao;

import com.example.FinTrack_Api.model.Transacao;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

public record DadosListagemTransacao(
        Long id
        , String conta
        , String categoria
        , BigDecimal valor
        , String nome
        , String descricao
        , String tipo
        , LocalDate data
        , String parcela

) {
    public DadosListagemTransacao(Transacao transacao) {
        this(
                transacao.getId()
                ,transacao.getConta().getNome()
                , transacao.getCategoria().getNome()
                , transacao.getValor()
                , transacao.getNome()
                , transacao.getDescricao()
                , transacao.getTipo().name()
                , transacao.getData()
                , String.valueOf(transacao.getParcela())
        );
    }
}
