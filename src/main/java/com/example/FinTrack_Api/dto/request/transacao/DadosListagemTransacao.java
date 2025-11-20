package com.example.FinTrack_Api.dto.request.transacao;

import com.example.FinTrack_Api.model.Categoria;
import com.example.FinTrack_Api.model.Conta;
import com.example.FinTrack_Api.model.Transacao;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

public record DadosListagemTransacao(
        Long id
        , Conta conta
        , Categoria categoria
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
                , transacao.getConta()
                , transacao.getCategoria()
                , transacao.getValor()
                , transacao.getNome()
                , transacao.getDescricao()
                , transacao.getTipo().name()
                , transacao.getData()
                , String.valueOf(transacao.getParcela())
        );
    }

}
