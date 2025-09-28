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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class TransacaoService {

    @Autowired
    private TransacaoRepository transacaoRepository;

    public List<DadosListagemTransacao> listarTransacoes(Long id) {
        if (id != null) {
            return transacaoRepository.findById(id)
                    .stream().map(DadosListagemTransacao::new).toList();
        }
        return transacaoRepository.findAll(Sort.by(Sort.Direction.DESC, "data"))
                .stream().map(DadosListagemTransacao::new).toList();
    }

    public List<DadosTotalMes> calcularTotaisMensais(TipoTransacao tipo, Integer ano) {
        List<Transacao> resultados = transacaoRepository.findBtTipoAndAno(tipo, ano);

        BigDecimal[] totalPorMes = new BigDecimal[12];
        Arrays.fill(totalPorMes, BigDecimal.ZERO);

        for (Transacao transacao : resultados) {
            int parcelas = transacao.getParcela();
            BigDecimal valorParcela = transacao.getValor()
                    .divide(BigDecimal.valueOf(parcelas), 2, RoundingMode.DOWN);
            BigDecimal totalCalculado = valorParcela.multiply(BigDecimal.valueOf(parcelas));
            BigDecimal diferenca = transacao.getValor().subtract(totalCalculado);
            for (int i = 0; i < parcelas; i++) {
                int mes = transacao.getData().plusMonths(i).getMonthValue() - 1;
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
