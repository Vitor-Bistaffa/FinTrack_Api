package com.example.FinTrack_Api.model;

import com.example.FinTrack_Api.dto.request.transacao.DadosAtualizarTransacao;
import com.example.FinTrack_Api.dto.request.transacao.DadosCadastroTransacao;
import com.example.FinTrack_Api.model.enums.TipoTransacao;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@Table(name = "transacao")
@Entity(name = "transacao")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class Transacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "fk_id_conta")
    private Conta conta;
    @ManyToOne
    @JoinColumn(name = "fk_id_categoria")
    private Categoria categoria;
    @Column(precision = 19, scale = 4)
    private BigDecimal valor;
    private String nome;
    @Column(columnDefinition = "TEXT")
    private String descricao;
    @Enumerated(EnumType.STRING)
    private TipoTransacao tipo;
    private LocalDate data;
    private Integer parcela;

    public Transacao(DadosCadastroTransacao dados, Conta conta, Categoria categoria) {
        this.conta = conta;
        this.categoria = categoria;
        this.valor = dados.valor();
        this.nome = dados.nome();
        this.descricao = dados.descricao();
        this.tipo = dados.tipo();
        this.data = dados.data();
        this.parcela = dados.parcela();
    }

    public void atualizarInformacoes(DadosAtualizarTransacao dados, Conta conta, Categoria categoria) {
        Optional.ofNullable(conta)
                .filter(v -> !v.equals(this.conta))
                .ifPresent(v -> this.conta = v);

        Optional.ofNullable(categoria)
                .filter(v -> !v.equals(this.categoria))
                .ifPresent(v -> this.categoria = v);

        Optional.ofNullable(dados.valor())
                .filter(v -> !v.equals(this.valor))
                .ifPresent(v -> this.valor = v);

        Optional.ofNullable(dados.nome())
                .filter(v -> !v.equals(this.nome))
                .ifPresent(v -> this.nome = v);

        Optional.ofNullable(dados.descricao())
                .filter(v -> !v.equals(this.descricao))
                .ifPresent(v -> this.descricao = v);

        Optional.ofNullable(dados.tipo())
                .filter(v -> !v.equals(this.tipo))
                .ifPresent(v -> this.tipo = v);

        Optional.ofNullable(dados.data())
                .filter(v -> !v.equals(this.data))
                .ifPresent(v -> this.data = v);

        Optional.ofNullable(dados.parcela())
                .filter(v -> !v.equals(this.parcela))
                .ifPresent(v -> this.parcela = v);
    }
}
