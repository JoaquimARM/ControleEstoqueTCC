package com.controle.controleEstoque.service;

import com.controle.controleEstoque.model.Estoque;
import com.controle.controleEstoque.model.Historico;
import com.controle.controleEstoque.model.Produto;
import com.controle.controleEstoque.repository.EstoqueRepository;
import com.controle.controleEstoque.repository.ProdutoRepository;
import com.controle.controleEstoque.repository.HistoricoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EstoqueService {
    @Autowired
    private final EstoqueRepository estoqueRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private HistoricoRepository historicoRepository;

    @Autowired
    public EstoqueService(EstoqueRepository estoqueRepository) {
        this.estoqueRepository = estoqueRepository;
    }

    public void salvar(Estoque estoque) {
        estoqueRepository.save(estoque);
    }

    public List<Estoque> listarTodos() {
        return estoqueRepository.findAll();
    }

    @Transactional
    public void adicionarQuantidade(Long produtoId, int quantidade) {
        Estoque estoque = estoqueRepository.findById(produtoId)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado no estoque."));

        // Atualizar quantidade no estoque
        estoque.setQuantidade(estoque.getQuantidade() + quantidade);
        estoqueRepository.save(estoque);

        // Salvar no histórico
        Historico historico = new Historico();
        historico.setProduto(estoque.getProduto());
        historico.setQuantidade(quantidade);
        historico.setTipo("Entrada"); // Define que foi uma entrada
        historico.setData(LocalDate.now());
        historicoRepository.save(historico);
    }

    public void removerQuantidade(Long produtoId, int quantidade, String tipo) {
        Estoque estoque = estoqueRepository.findById(produtoId)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado no estoque."));

        if (estoque.getQuantidade() < quantidade) {
            throw new IllegalArgumentException("Quantidade insuficiente no estoque.");
        }

        // Atualizar quantidade no estoque
        estoque.setQuantidade(estoque.getQuantidade() - quantidade);
        estoqueRepository.save(estoque);

        // Salvar no histórico
        Historico historico = new Historico();
        historico.setProduto(estoque.getProduto());
        historico.setQuantidade(quantidade);
        historico.setTipo(tipo); // Define o tipo como "Venda" ou "Perda"
        historico.setData(LocalDate.now());
        historicoRepository.save(historico);
    }
    public Produto obterProdutoPorId(Long produtoId) {
        return produtoRepository.findById(produtoId)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado"));
    }


}