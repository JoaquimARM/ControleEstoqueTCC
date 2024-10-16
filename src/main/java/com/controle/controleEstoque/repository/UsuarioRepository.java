package com.controle.controleEstoque.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.controle.controleEstoque.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Usuario findByEmail(String email);
}