package com.example.FinTrack_Api.dto.request.transacao;

import com.example.FinTrack_Api.model.enums.TipoTransacao;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DadosRemoverTransacao(
        Long id
) {
}
