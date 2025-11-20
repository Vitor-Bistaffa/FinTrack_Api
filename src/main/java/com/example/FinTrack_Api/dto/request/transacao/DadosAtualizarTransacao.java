package com.example.FinTrack_Api.dto.request.transacao;

import com.example.FinTrack_Api.model.Categoria;
import com.example.FinTrack_Api.model.Conta;
import com.example.FinTrack_Api.model.enums.TipoTransacao;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DadosAtualizarTransacao(
        Long id
        , Conta conta
        , Categoria categoria
        , BigDecimal valor
        , String nome
        , String descricao
        , TipoTransacao tipo
        , @JsonFormat(shape = JsonFormat.Shape.STRING) LocalDate data
        , Integer parcela)
{}
