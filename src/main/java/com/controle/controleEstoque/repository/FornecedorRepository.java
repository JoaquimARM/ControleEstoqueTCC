package com.controle.controleEstoque.repository;

import com.controle.controleEstoque.model.Fornecedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface FornecedorRepository extends JpaRepository<Fornecedor, Long> {

}