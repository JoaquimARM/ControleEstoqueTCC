package com.controle.controleEstoque.service;

import com.controle.controleEstoque.model.Estoque;
import com.controle.controleEstoque.model.Fornecedor;
import com.controle.controleEstoque.model.Produto;
import com.controle.controleEstoque.repository.EstoqueRepository;
import com.controle.controleEstoque.repository.ProdutoRepository;
import com.controle.controleEstoque.repository.FornecedorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EstoqueService {

    private final EstoqueRepository estoqueRepository;
    private final ProdutoRepository produtoRepository;
    private final FornecedorRepository fornecedorRepository;

    public EstoqueService(EstoqueRepository estoqueRepository, ProdutoRepository produtoRepository, FornecedorRepository fornecedorRepository) {
        this.estoqueRepository = estoqueRepository;
        this.produtoRepository = produtoRepository;
        this.fornecedorRepository = fornecedorRepository;
    }

    public List<Estoque> listarTodos() {
        return estoqueRepository.findAll();
    }

    public void salvar(Estoque estoque) {
        estoqueRepository.save(estoque);
    }

    public Optional<Estoque> obterPorId(Long id) {
        return estoqueRepository.findById(id);
    }

    public void atualizar(Long id, Estoque estoqueAtualizado) {
        Optional<Estoque> estoqueExistente = obterPorId(id);
        if (estoqueExistente.isPresent()) {
            Estoque estoque = estoqueExistente.get();
            estoque.setQuantidade(estoqueAtualizado.getQuantidade());
            estoque.setDataValidade(estoqueAtualizado.getDataValidade());
            estoque.setUltimaDataEntrada(estoqueAtualizado.getUltimaDataEntrada());
            estoque.setFornecedor(estoqueAtualizado.getFornecedor());
            estoque.setProduto(estoqueAtualizado.getProduto());
            estoqueRepository.save(estoque);
        }
    }

    public void apagar(Long id) {
        estoqueRepository.deleteById(id);
    }

    public List<Produto> listarProdutos() {
        return produtoRepository.findAll();
    }

    public List<Fornecedor> listarFornecedores() {
        return fornecedorRepository.findAll();
    }
}
