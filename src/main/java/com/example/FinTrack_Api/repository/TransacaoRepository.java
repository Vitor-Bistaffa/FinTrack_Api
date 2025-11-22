package com.example.FinTrack_Api.repository;

import com.example.FinTrack_Api.model.Transacao;
import com.example.FinTrack_Api.model.Usuario;
import com.example.FinTrack_Api.model.enums.TipoTransacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransacaoRepository extends JpaRepository<Transacao, Long> {

    List<Transacao> findByTipo(TipoTransacao tipo);

    List<Transacao> findByUsuarioOrderByDataAsc(Usuario usuario);

}
