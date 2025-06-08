package com.example.FinTrack_Api.dto.request.transacao;

import com.example.FinTrack_Api.model.enums.TipoTransacao;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DadosCadastroTransacao(
        Long conta
        ,Long categoria
        ,BigDecimal valor
        ,String nome
        ,String descricao
        ,TipoTransacao tipo
        ,@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy") LocalDate data
        ,Integer parcela)
{}
