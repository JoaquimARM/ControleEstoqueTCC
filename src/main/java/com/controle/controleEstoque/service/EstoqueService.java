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

    public void registrarEntrada(Long produtoId, int quantidade) {
        Estoque estoque = estoqueRepository.findByProdutoId(produtoId)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado no estoque"));

        estoque.setQuantidade(estoque.getQuantidade() + quantidade); // Atualiza a quantidade
        estoqueRepository.save(estoque);

        // Adiciona um registro no histórico
        Historico historico = new Historico();
        historico.setProduto(estoque.getProduto());
        historico.setQuantidade(quantidade);
        historico.setTipo("Entrada");
        historico.setData(LocalDate.now());
        historicoRepository.save(historico);
    }

    public void registrarSaida(Long produtoId, int quantidade, String tipo) {
        Estoque estoque = estoqueRepository.findByProdutoId(produtoId)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado no estoque"));

        if (estoque.getQuantidade() < quantidade) {
            throw new RuntimeException("Quantidade insuficiente no estoque");
        }

        estoque.setQuantidade(estoque.getQuantidade() - quantidade); // Atualiza a quantidade
        estoqueRepository.save(estoque);

        // Adiciona um registro no histórico
        Historico historico = new Historico();
        historico.setProduto(estoque.getProduto());
        historico.setQuantidade(quantidade);
        historico.setTipo("Saída - " + tipo); // Indica se foi venda ou perda
        historico.setData(LocalDate.now());
        historicoRepository.save(historico);
    }
    public Produto obterProdutoPorId(Long produtoId) {
        return produtoRepository.findById(produtoId)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado"));
    }

    public void atualizar(Estoque estoque) {
        Estoque estoqueExistente = estoqueRepository.findById(estoque.getId())
                .orElseThrow(() -> new IllegalArgumentException("Estoque não encontrado!"));

        estoqueExistente.setQuantidade(estoque.getQuantidade());
        estoqueExistente.setDataEntrada(estoque.getDataEntrada());
        estoqueExistente.setDataValidade(estoque.getDataValidade());

        estoqueRepository.save(estoqueExistente);
    }

}