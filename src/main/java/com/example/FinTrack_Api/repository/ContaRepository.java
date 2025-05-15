package com.example.FinTrack_Api.repository;

import com.example.FinTrack_Api.model.Conta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContaRepository extends JpaRepository<Conta, Long> {

    // Retorna contas onde 'excluido' é false ou null
    List<Conta> findByExcluidoFalse();
}
