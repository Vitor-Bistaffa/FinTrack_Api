package com.example.FinTrack_Api.service;

import com.example.FinTrack_Api.dto.request.categoria.DadosCadastroCategoria;
import com.example.FinTrack_Api.dto.request.conta.DadosCadastroConta;
import com.example.FinTrack_Api.dto.request.transacao.DadosListagemTransacao;
import com.example.FinTrack_Api.dto.request.transacao.DadosTotalMes;
import com.example.FinTrack_Api.model.Categoria;
import com.example.FinTrack_Api.model.Conta;
import com.example.FinTrack_Api.model.Transacao;
import com.example.FinTrack_Api.model.enums.TipoTransacao;
import com.example.FinTrack_Api.repository.CategoriaRepository;
import com.example.FinTrack_Api.repository.ContaRepository;
import com.example.FinTrack_Api.repository.TransacaoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static com.example.FinTrack_Api.model.enums.TipoTransacao.Despesa;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Transactional
@WithMockUser
class TransacaoServiceTest {

    @Autowired
    private ContaRepository contaRepository;
    @Autowired
    private CategoriaRepository categoriaRepository;
    @Autowired
    private TransacaoRepository transacaoRepository;
    @Autowired
    private TransacaoService transacaoService;

    @Test
    @DisplayName("Deve devolver duas transações, uma sendo mês 12 do ano atual e outra sendo mês 1 do ano seguinte")
    void listarTransacoes() {

        var categoria = cadastrarCategoria("Comprar");
        var conta = cadastrarConta("Nubank");

        cadastrarTransacao(conta, categoria, 100.00, "Compra", "Teste de parcelas", Despesa, LocalDate.of(LocalDate.now().getYear(), 12, 1), 2);

        List<DadosListagemTransacao> resultado = transacaoService.listarTransacoes(null);
        assertThat(resultado).hasSize(2);

        assertThat(resultado.get(0).valor()).isEqualByComparingTo("50.00");
        assertThat(resultado.get(0).data()).isEqualTo(LocalDate.of(LocalDate.now().getYear(), 12, 1));
        assertThat(resultado.get(1).data()).isEqualTo(LocalDate.of(LocalDate.now().getYear() + 1, 1, 1));
    }

    @Test
    @DisplayName("Deve devolver o total 50 tanto no mes 12 do ano atual quanto no mês 1 do proximo ano")
    void calcularTotaisMensais() {
        var categoria = cadastrarCategoria("Comprar");
        var conta = cadastrarConta("Nubank");

        cadastrarTransacao(conta, categoria, 100.00, "Compra", "Teste de parcelas", Despesa, LocalDate.of(LocalDate.now().getYear(), 12, 1), 2);

        List<DadosTotalMes> resultado2025 = transacaoService.calcularTotaisMensais(Despesa, 2025);
        List<DadosTotalMes> resultado2026 = transacaoService.calcularTotaisMensais(Despesa, 2026);

        assertThat(resultado2025.getLast().total()).isEqualByComparingTo(BigDecimal.valueOf(50));
        assertThat(resultado2026.getFirst().total()).isEqualByComparingTo(BigDecimal.valueOf(50));
    }


    private void cadastrarTransacao(Conta conta, Categoria categoria, Double valor, String nome, String descricao, TipoTransacao tipoTransacao, LocalDate data, Integer parcela) {

        transacaoRepository.save(new Transacao(null, conta, categoria, new BigDecimal(valor), nome, descricao, tipoTransacao, data, parcela));

    }

    private Categoria cadastrarCategoria(String nome) {
        var categoria = new Categoria(new DadosCadastroCategoria(nome));
        categoriaRepository.save(categoria);
        return categoria;
    }

    private Conta cadastrarConta(String nome) {
        var conta = new Conta(new DadosCadastroConta(nome));
        contaRepository.save(conta);
        return conta;
    }


}
