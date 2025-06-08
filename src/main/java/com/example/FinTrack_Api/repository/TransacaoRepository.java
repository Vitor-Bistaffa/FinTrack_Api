package com.example.FinTrack_Api.repository;

import com.example.FinTrack_Api.model.Transacao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransacaoRepository extends JpaRepository<Transacao, Long> { }
