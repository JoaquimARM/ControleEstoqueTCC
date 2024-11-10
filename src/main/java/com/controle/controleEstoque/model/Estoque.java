package com.controle.controleEstoque.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;


@Entity
@Table(name = "estoque")
public class Estoque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    @ManyToOne
    @JoinColumn(name = "fornecedor_id", nullable = false)
    private Fornecedor fornecedor;

    @Column(nullable = false)
    private BigDecimal preco;

    @Column
    private Integer quantidade; // Inicialmente nulo ou zero

    @Column(name = "data_validade")
    private LocalDate dataValidade; // Para exibição na tabela

    @Column(name = "ultima_data_entrada")
    private LocalDate ultimaDataEntrada; // A última entrada registrada.

    //public Estoque(Produto produto, int quantidade, BigDecimal preco, LocalDate dataValidade, LocalDate ultimaDataEntrada, Fornecedor fornecedor) {
    //    this.produto = produto;
    //    this.quantidade = quantidade;
    //    this.preco = preco;
    //    this.dataValidade = dataValidade;
    //    this.ultimaDataEntrada = ultimaDataEntrada;
    //    this.fornecedor = fornecedor;
    //}
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public LocalDate getDataValidade() {
        return dataValidade;
    }

    public void setDataValidade(LocalDate dataValidade) {
        this.dataValidade = dataValidade;
    }

    public LocalDate getUltimaDataEntrada() {
        return ultimaDataEntrada;
    }

    public void setUltimaDataEntrada(LocalDate ultimaDataEntrada) {
        this.ultimaDataEntrada = ultimaDataEntrada;
    }
}