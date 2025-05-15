package com.example.FinTrack_Api.model;

import com.example.FinTrack_Api.dto.request.conta.DadosCadastroConta;
import jakarta.persistence.*;
import jakarta.validation.Valid;
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

    public Conta(DadosCadastroConta dados) {
        this.nome = dados.nome();
    }

    public void atualizarInformacoes(DadosCadastroConta dados) {
        if (dados.nome() != null){
            this.nome = dados.nome();
        }
    }

    public void deletar(DadosCadastroConta dados) {
        this.excluido = true;
    }

    public void restaurar(DadosCadastroConta dados) {
        this.excluido = false;
    }
}
