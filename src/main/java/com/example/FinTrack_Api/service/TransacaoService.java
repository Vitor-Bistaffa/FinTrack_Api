package com.example.FinTrack_Api.service;

import com.example.FinTrack_Api.dto.request.transacao.DadosListagemTransacao;
import com.example.FinTrack_Api.dto.request.transacao.DadosTotalMes;
import com.example.FinTrack_Api.model.Transacao;
import com.example.FinTrack_Api.model.enums.TipoTransacao;
import com.example.FinTrack_Api.repository.TransacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class TransacaoService {

    @Autowired
    private TransacaoRepository transacaoRepository;

    public List<DadosListagemTransacao> listarTransacoes(Long id) {
        List<Transacao> transacoes;
        if (id != null) {
            transacoes = transacaoRepository.findById(id).stream().toList();
        } else {
            transacoes = transacaoRepository.findAll(Sort.by(Sort.Direction.ASC, "data"));
        }

        List<DadosListagemTransacao> resultado = new ArrayList<>();

        for (Transacao transacao : transacoes) {
            int parcelas = (transacao.getParcela() == null || transacao.getParcela() <= 0) ? 1 : transacao.getParcela();

            BigDecimal valorParcela = transacao.getValor()
                    .divide(BigDecimal.valueOf(parcelas), 2, RoundingMode.DOWN);

            BigDecimal totalCalculado = valorParcela.multiply(BigDecimal.valueOf(parcelas));
            BigDecimal diferenca = transacao.getValor().subtract(totalCalculado);

            for (int i = 0; i < parcelas; i++) {
                LocalDate dataParcela = transacao.getData().plusMonths(i);
                BigDecimal valorFinal = valorParcela;

                if (i == 0) { // primeira parcela recebe ajuste de centavos
                    valorFinal = valorFinal.add(diferenca);
                }

                // Cria DTO já com os dados da parcela
                resultado.add(new DadosListagemTransacao(
                        transacao.getId(),
                        transacao.getConta().getNome(),
                        transacao.getCategoria().getNome(),
                        valorFinal,
                        transacao.getNome(),
                        transacao.getDescricao(),
                        transacao.getTipo().name(),
                        dataParcela,
                        (i + 1) + "/" + parcelas // <- aqui fica 1/2, 2/2...
                ));
            }
        }

        return resultado;
    }


    public List<DadosTotalMes> calcularTotaisMensais(TipoTransacao tipo, Integer ano) {
        List<Transacao> resultados = transacaoRepository.findByTipo(tipo);


        BigDecimal[] totalPorMes = new BigDecimal[12];
        Arrays.fill(totalPorMes, BigDecimal.ZERO);

        for (Transacao transacao : resultados) {

            int parcelas = transacao.getParcela();
            BigDecimal valorParcela = transacao.getValor()
                    .divide(BigDecimal.valueOf(parcelas), 2, RoundingMode.DOWN);
            BigDecimal totalCalculado = valorParcela.multiply(BigDecimal.valueOf(parcelas));
            BigDecimal diferenca = transacao.getValor().subtract(totalCalculado);

            for (int i = 0; i < parcelas; i++) {
                LocalDate dataParcela = transacao.getData().plusMonths(i);

                if (dataParcela.getYear() != ano) continue;

                int mes = dataParcela.getMonthValue() -1;

                BigDecimal valorFinal = valorParcela;
                if (i == 0) {
                    valorFinal = valorFinal.add(diferenca).setScale(2, RoundingMode.DOWN);
                }

                totalPorMes[mes] = totalPorMes[mes].add(valorFinal);
            }
        }

        List<DadosTotalMes> totais = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            totais.add(new DadosTotalMes(i + 1, totalPorMes[i]));
        }

        return totais;
    }
}
