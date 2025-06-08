package com.example.FinTrack_Api.dto.request.categoria;

import com.example.FinTrack_Api.model.Categoria;

public record DadosListagemCategoria(Long id, String nome) {
    public DadosListagemCategoria(Categoria categoria) {
        this(categoria.getId(), categoria.getNome());
    }
}
