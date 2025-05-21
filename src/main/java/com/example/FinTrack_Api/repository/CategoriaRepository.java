package com.example.FinTrack_Api.repository;

import com.example.FinTrack_Api.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoriaRepository extends JpaRepository<Categoria, Long > {
    List<Categoria> findByExcluidoFalse();
}
