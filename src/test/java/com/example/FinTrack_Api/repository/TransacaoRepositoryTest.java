package com.example.FinTrack_Api.repository;

import com.example.FinTrack_Api.dto.request.categoria.DadosCadastroCategoria;
import com.example.FinTrack_Api.dto.request.conta.DadosCadastroConta;
import com.example.FinTrack_Api.model.Categoria;
import com.example.FinTrack_Api.model.Conta;
import com.example.FinTrack_Api.model.Transacao;
import com.example.FinTrack_Api.model.enums.TipoTransacao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;

import static com.example.FinTrack_Api.model.enums.TipoTransacao.Despesa;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class TransacaoRepositoryTest {
    @Autowired
    private TransacaoRepository transacaoRepository;
    @Autowired
    private TestEntityManager testEntityManager;


    @Test
    void findBtTipoAndAno() {

        var categoria = cadastrarCategoria("test");
        var conta = cadastrarConta("test");

        cadastrarTransacao(conta, categoria, 270.00, "test", "sla", Despesa, LocalDate.now(), 1);

        var transacao = transacaoRepository.findBtTipoAndAno(Despesa, LocalDate.now().getYear());

        assertThat(transacao).isNotNull();
    }

    private void cadastrarTransacao(Conta conta, Categoria categoria, Double valor, String nome, String descricao, TipoTransacao tipoTransacao, LocalDate data, Integer parcela) {
        testEntityManager.persist(new Transacao(null, conta, categoria, new BigDecimal(valor), nome, descricao, tipoTransacao, data, parcela));
    }

    private Categoria cadastrarCategoria(String nome) {
        var categoria = new Categoria(new DadosCadastroCategoria(nome));
        testEntityManager.persist(categoria);
        return categoria;
    }

    private Conta cadastrarConta(String nome) {
        var conta = new Conta(new DadosCadastroConta(nome));
        testEntityManager.persist(conta);
        return conta;
    }

}