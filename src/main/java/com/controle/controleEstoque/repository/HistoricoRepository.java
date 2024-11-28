package com.controle.controleEstoque.repository;

import com.controle.controleEstoque.model.Historico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoricoRepository extends JpaRepository<Historico, Long> {
    List<Historico> findAllByOrderByDataDesc();
}