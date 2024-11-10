package com.controle.controleEstoque.service;

import com.controle.controleEstoque.model.Produto;
import com.controle.controleEstoque.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    public void salvar(Produto produto) {
        produtoRepository.save(produto);
    }

    public List<Produto> listarTodos() {
        return produtoRepository.findAll();
    }

    public Produto obterPorId(Long id) {
        return produtoRepository.findById(id).orElse(null);
    }

    public void atualizar(Long id, Produto produtoAtualizado) {
        Produto produtoExistente = obterPorId(id);
        if (produtoExistente != null) {
            produtoExistente.setNome(produtoAtualizado.getNome());
            produtoExistente.setPreco(produtoAtualizado.getPreco());
            produtoRepository.save(produtoExistente);
        }
    }

    public void apagar(Long id) {
        produtoRepository.deleteById(id);
    }
}