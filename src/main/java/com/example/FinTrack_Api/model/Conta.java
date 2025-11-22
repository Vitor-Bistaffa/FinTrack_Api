package com.example.FinTrack_Api.model;

import com.example.FinTrack_Api.dto.request.conta.DadosAtualizarConta;
import com.example.FinTrack_Api.dto.request.conta.DadosCadastroConta;
import com.example.FinTrack_Api.dto.request.conta.DadosRemoverConta;
import com.example.FinTrack_Api.dto.request.conta.DadosRestaurarConta;
import jakarta.persistence.*;
import lombok.*;

@Table(name = "conta")
@Entity(name = "conta")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class Conta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private Boolean excluido = false;
    @ManyToOne
    @JoinColumn(name = "fk_id_usuario")
    private Usuario usuario;

    public Conta(DadosCadastroConta dados, Usuario usuario) {
        this.nome = dados.nome();
        this.excluido = false;
        this.usuario = usuario;
    }

    public void atualizarInformacoes(DadosAtualizarConta dados) {
        if (dados.nome() != null) {
            this.nome = dados.nome();
        }
    }

    public void remover(DadosRemoverConta dados) {
        this.excluido = true;
    }

    public void restaurar(DadosRestaurarConta dados) {
        this.excluido = false;
    }
}
