package com.example.FinTrack_Api.dto.request.transacao;

import com.example.FinTrack_Api.dto.request.categoria.DadosListagemCategoria;
import com.example.FinTrack_Api.dto.request.conta.DadosListagemConta;
import com.example.FinTrack_Api.model.Categoria;
import com.example.FinTrack_Api.model.Conta;
import com.example.FinTrack_Api.model.Transacao;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

public record DadosListagemTransacao(
        Long id
        , DadosListagemConta conta
        , DadosListagemCategoria categoria
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
                , new DadosListagemConta(transacao.getConta())
                , new DadosListagemCategoria(transacao.getCategoria())
                , transacao.getValor()
                , transacao.getNome()
                , transacao.getDescricao()
                , transacao.getTipo().name()
                , transacao.getData()
                , String.valueOf(transacao.getParcela())
        );
    }

}
