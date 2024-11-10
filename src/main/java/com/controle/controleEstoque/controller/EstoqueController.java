package com.controle.controleEstoque.controller;

import com.controle.controleEstoque.model.Estoque;
import com.controle.controleEstoque.model.Fornecedor;
import com.controle.controleEstoque.model.Produto;
import com.controle.controleEstoque.repository.FornecedorRepository;
import com.controle.controleEstoque.repository.ProdutoRepository;
import com.controle.controleEstoque.service.EstoqueService;
import com.controle.controleEstoque.service.FornecedorService;
import com.controle.controleEstoque.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/estoque")
public class EstoqueController {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private FornecedorRepository fornecedorRepository;

    private final ProdutoService produtoService;
    private final FornecedorService fornecedorService;
    private final EstoqueService estoqueService;

    public EstoqueController(ProdutoService produtoService, FornecedorService fornecedorService, EstoqueService estoqueService) {
        this.produtoService = produtoService;
        this.fornecedorService = fornecedorService;
        this.estoqueService = estoqueService;
    }

    @GetMapping
    public String mostrarEstoque(Model model) {
        List<Estoque> estoque = estoqueService.listarTodos();
        model.addAttribute("estoque", estoque);
        return "pagGeral";  // Carrega a página principal com a lista de produtos
    }

    @GetMapping("/adicionarEstoque")
    public String exibirFormAdicionarProduto(Model model) {
        List<Produto> produtos = produtoService.listarTodos();
        List<Fornecedor> fornecedores = fornecedorService.listarTodos();

        model.addAttribute("produtos", produtoRepository.findAll());
        model.addAttribute("fornecedores", fornecedorRepository.findAll());
        model.addAttribute("estoque", new Estoque());
        return "estoque/adicionarEstoque";  // Página de formulário para adicionar produto ao estoque
    }

    @PostMapping("/adicionarEstoque")
    public String adicionarProdutoEstoque(@ModelAttribute("estoque") Estoque estoque) {
        estoqueService.salvar(estoque);
        return "redirect:/pagGeral";  // Retorna à página inicial após adicionar o produto ao estoque
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable("id") Long id, Model model) {
        Estoque estoque = estoqueService.obterPorId(id).orElse(null);
        if (estoque != null) {
            model.addAttribute("estoque", estoque);
            model.addAttribute("produtos", produtoService.listarTodos());
            model.addAttribute("fornecedores", fornecedorService.listarTodos());
            return "estoque/editarEstoque"; // Nome da página HTML para editar estoque
        }
        return "redirect:/pagGeral"; // Redireciona caso o ID não seja encontrado
    }

    @PostMapping("/atualizar/{id}")
    public String atualizarEstoque(@PathVariable("id") Long id, @ModelAttribute Estoque estoqueAtualizado) {
        estoqueService.atualizar(id, estoqueAtualizado);
        return "redirect:/pagGeral"; // Redireciona para a página de listagem de estoque
    }

    @GetMapping("/apagar/{id}")
    public String apagarEstoque(@PathVariable("id") Long id) {
        estoqueService.apagar(id);
        return "redirect:/pagGeral"; // Redireciona para a página de listagem de estoque
    }
}
