package com.controle.controleEstoque.service;


import com.controle.controleEstoque.model.Fornecedor;
import com.controle.controleEstoque.model.Produto;
import com.controle.controleEstoque.repository.FornecedorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FornecedorService {

    private final FornecedorRepository fornecedorRepository;

    public FornecedorService(FornecedorRepository fornecedorRepository) {
        this.fornecedorRepository = fornecedorRepository;
    }

    public void salvar(Fornecedor fornecedor) {
        fornecedorRepository.save(fornecedor);
    }

    public List<Fornecedor> listarTodos() {
        return fornecedorRepository.findAll();
    }

    public List<Fornecedor> obterPorIds(List<Long> ids) {
        return fornecedorRepository.findAllById(ids);
    }

    public void deletar(Long id) {
        fornecedorRepository.deleteById(id);
    }

    public Fornecedor obterPorId(Long id) {
        return fornecedorRepository.findById(id).orElse(null);
    }
    public void atualizar(Long id, Fornecedor fornecedorAtualizado) {
        Fornecedor fornecedorExistente = obterPorId(id);
        if (fornecedorExistente != null) {
            fornecedorExistente.setNome(fornecedorAtualizado.getNome());
            fornecedorExistente.setEndereco(fornecedorAtualizado.getEndereco());
            fornecedorExistente.setCnpj(fornecedorAtualizado.getCnpj());
            fornecedorExistente.setTipo(fornecedorAtualizado.getTipo());
            fornecedorRepository.save(fornecedorExistente);
        }
    }
}