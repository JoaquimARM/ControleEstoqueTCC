package com.controle.controleEstoque.controller;

import com.controle.controleEstoque.model.Produto;
import com.controle.controleEstoque.service.FornecedorService;
import com.controle.controleEstoque.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/produtos")
public class ProdutoController {

   
    private final ProdutoService produtoService;
    private final FornecedorService fornecedorService;

    @Autowired
    public ProdutoController(ProdutoService produtoService, FornecedorService fornecedorService) {
        this.produtoService = produtoService;
        this.fornecedorService = fornecedorService;
    }

    // Exibir a página de cadastro de produto
    @GetMapping("/cadastrarProduto")
    public String mostrarFormularioCadastro(Model model) {
        model.addAttribute("produto", new Produto()); // Adiciona um novo objeto Produto para o formulário
        model.addAttribute("fornecedores", fornecedorService.listarTodos());
        return "produtos/cadastrarProduto"; // Nome da página HTML do Thymeleaf (cadastrar_produto.html)
    }

    // Salvar o produto após o preenchimento do formulário
    @PostMapping("/salvar")
    public String salvarProduto(@ModelAttribute Produto produto) {
        produtoService.salvar(produto); // Salva o produto no banco de dados
        return "redirect:/pagGeral"; // Redireciona para a tela inicial após o cadastro
    }

    // Listar todos os produtos cadastrados
    @GetMapping("/listarProduto")
    public String listarProdutos(Model model) {
        model.addAttribute("produtos", produtoService.listarTodos()); // Adiciona a lista de produtos para exibir
        return "produtos/listarProduto"; // Nome da página HTML do Thymeleaf (listar_produtos.html)
    }

    // Editar produto - Exibir formulário com dados do produto específico
    @GetMapping("/editar/{id}")
    public String editarProduto(@PathVariable("id") Long id, Model model) {
        Produto produto = produtoService.obterPorId(id); // Busca o produto pelo ID
        model.addAttribute("produto", produto); // Passa o produto para o formulário de edição
        return "produtos/editarProduto"; // Nome da página HTML do Thymeleaf (editar_produto.html)
    }

    // Salvar as alterações do produto após a edição
    @PostMapping("/atualizar/{id}")
    public String atualizarProduto(@PathVariable("id") Long id, @ModelAttribute Produto produtoAtualizado) {
        produtoService.atualizar(id, produtoAtualizado); // Atualiza o produto no banco de dados
        return "redirect:/produtos/listarProduto"; // Redireciona para a lista de produtos
    }

    // Apagar um produto
    @GetMapping("/apagar/{id}")
    public String apagarProduto(@PathVariable("id") Long id) {
        produtoService.apagar(id); // Remove o produto do banco de dados
        return "redirect:/produtos/listarProduto"; // Redireciona para a lista de produtos
    }
}
