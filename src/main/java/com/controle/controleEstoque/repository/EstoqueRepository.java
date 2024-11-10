package com.controle.controleEstoque.repository;

import com.controle.controleEstoque.model.Estoque;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstoqueRepository extends JpaRepository<Estoque, Long> {
    // Métodos adicionais podem ser definidos aqui se necessário
}
