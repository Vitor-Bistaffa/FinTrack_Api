package com.example.FinTrack_Api.repository;

import com.example.FinTrack_Api.model.Conta;
import com.example.FinTrack_Api.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface ContaRepository extends JpaRepository<Conta, Long> {

    List<Conta> findByExcluidoFalseAndUsuarioIs(Usuario usuario);
}
