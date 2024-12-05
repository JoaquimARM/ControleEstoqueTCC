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

    private boolean isCNPJValido(String cnpj) {
        // Remove caracteres não numéricos
        cnpj = cnpj.replaceAll("\\D", "");

        // Validações básicas
        if (cnpj.length() != 14 || cnpj.matches("(\\d)\\1{13}")) return false;

        try {
            // Cálculo do dígito verificador
            int[] pesos = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
            for (int j = 0; j < 2; j++) {
                int soma = 0;
                for (int i = 0; i < 12 + j; i++) {
                    soma += (cnpj.charAt(i) - '0') * pesos[i + 1 - j];
                }
                int digito = soma % 11 < 2 ? 0 : 11 - soma % 11;
                if (digito != cnpj.charAt(12 + j) - '0') return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}