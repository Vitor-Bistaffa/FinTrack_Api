package com.example.FinTrack_Api.service;

import com.example.FinTrack_Api.dto.request.transacao.DadosListagemTransacao;
import com.example.FinTrack_Api.dto.request.transacao.DadosTotalMes;
import com.example.FinTrack_Api.model.Transacao;
import com.example.FinTrack_Api.model.Usuario;
import com.example.FinTrack_Api.model.enums.TipoTransacao;
import com.example.FinTrack_Api.repository.TransacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    // Lista todas as transações ou apenas a transação com o ID informado
    public List<DadosListagemTransacao> listarTransacoes(Long id, @AuthenticationPrincipal Usuario usuario) {
        List<Transacao> transacoes;

        if (id != null) {
            transacoes = transacaoRepository.findById(id).stream().toList();
            return transacoes.stream().map(DadosListagemTransacao::new).toList();
        } else {
            transacoes = transacaoRepository.findByUsuarioOrderByDataAsc(usuario);
        }

        List<DadosListagemTransacao> resultado = new ArrayList<>();

        // Cria entradas para cada parcela da transação
        for (Transacao transacao : transacoes) {
            int parcelas = (transacao.getParcela() == null || transacao.getParcela() <= 0) ? 1 : transacao.getParcela();

            BigDecimal valorParcela = transacao.getValor()
                    .divide(BigDecimal.valueOf(parcelas), 2, RoundingMode.DOWN);

            BigDecimal totalCalculado = valorParcela.multiply(BigDecimal.valueOf(parcelas));
            BigDecimal diferenca = transacao.getValor().subtract(totalCalculado);

            for (int i = 0; i < parcelas; i++) {
                LocalDate dataParcela = transacao.getData().plusMonths(i);
                BigDecimal valorFinal = valorParcela;

                // Ajusta centavos na primeira parcela
                if (i == 0) {
                    valorFinal = valorFinal.add(diferenca);
                }

                // Adiciona DTO com dados da parcela
                resultado.add(new DadosListagemTransacao(
                        transacao.getId(),
                        transacao.getConta(),
                        transacao.getCategoria(),
                        valorFinal,
                        transacao.getNome(),
                        transacao.getDescricao(),
                        transacao.getTipo().name(),
                        dataParcela,
                        (i + 1) + "/" + parcelas
                ));
            }
        }

        return resultado;
    }

    // Calcula os totais mensais de receitas ou despesas para um ano específico
    public List<DadosTotalMes> calcularTotaisMensais(TipoTransacao tipo, Integer ano) {
        List<Transacao> resultados = transacaoRepository.findByTipo(tipo);

        // Inicializa array com total zero para cada mês
        BigDecimal[] totalPorMes = new BigDecimal[12];
        Arrays.fill(totalPorMes, BigDecimal.ZERO);

        for (Transacao transacao : resultados) {
            int parcelas = transacao.getParcela();
            BigDecimal valorParcela = transacao.getValor()
                    .divide(BigDecimal.valueOf(parcelas), 2, RoundingMode.DOWN);
            BigDecimal totalCalculado = valorParcela.multiply(BigDecimal.valueOf(parcelas));
            BigDecimal diferenca = transacao.getValor().subtract(totalCalculado);

            // Distribui cada parcela nos meses correspondentes
            for (int i = 0; i < parcelas; i++) {
                LocalDate dataParcela = transacao.getData().plusMonths(i);

                if (dataParcela.getYear() != ano) continue;

                int mes = dataParcela.getMonthValue() - 1;
                BigDecimal valorFinal = valorParcela;

                // Ajusta centavos na primeira parcela
                if (i == 0) {
                    valorFinal = valorFinal.add(diferenca).setScale(2, RoundingMode.DOWN);
                }

                totalPorMes[mes] = totalPorMes[mes].add(valorFinal);
            }
        }

        // Cria lista de totais por mês
        List<DadosTotalMes> totais = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            totais.add(new DadosTotalMes(i + 1, totalPorMes[i]));
        }

        return totais;
    }
}
