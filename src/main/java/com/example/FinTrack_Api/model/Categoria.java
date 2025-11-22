package com.example.FinTrack_Api.model;

import com.example.FinTrack_Api.dto.request.categoria.DadosAtualizarCategoria;
import com.example.FinTrack_Api.dto.request.categoria.DadosCadastroCategoria;
import com.example.FinTrack_Api.dto.request.categoria.DadosRemoverCategoria;
import com.example.FinTrack_Api.dto.request.categoria.DadosRestaurarCategoria;
import jakarta.persistence.*;
import lombok.*;

@Table(name = "categoria")
@Entity(name = "categoria")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private Boolean excluido = false;
    @ManyToOne
    @JoinColumn(name = "fk_id_usuario")
    private Usuario usuario;

    public Categoria(DadosCadastroCategoria dados, Usuario usuario) {
        this.nome = dados.nome();
        this.excluido = false;
        this.usuario = usuario;
    }

    public void atualizarInformacoes(DadosAtualizarCategoria dados) {
        if (dados.nome() != null){
            this.nome = dados.nome();
        }
    }

    public void remover(DadosRemoverCategoria dados) {
        this.excluido = true;
    }

    public void restaurar(DadosRestaurarCategoria dados) {
        this.excluido = false;
    }
}
