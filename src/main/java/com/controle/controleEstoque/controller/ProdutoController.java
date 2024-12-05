package com.controle.controleEstoque.controller;

import com.controle.controleEstoque.model.Fornecedor;
import com.controle.controleEstoque.model.Produto;
import com.controle.controleEstoque.service.FornecedorService;
import com.controle.controleEstoque.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        List<Fornecedor> fornecedores = fornecedorService.listarTodos();
        model.addAttribute("produto", new Produto()); // Adiciona um novo objeto Produto para o formulário
        model.addAttribute("fornecedores", fornecedores);
        return "produtos/cadastrarProduto"; // Nome da página HTML do Thymeleaf (cadastrar_produto.html)
    }

    // Salvar o produto após o preenchimento do formulário
    @PostMapping("/salvar")
    public String salvarProduto(@ModelAttribute Produto produto, @RequestParam("fornecedores") List<Long> fornecedoresIds) {
        Set<Fornecedor> fornecedorSet = new HashSet<>(fornecedorService.obterPorIds(fornecedoresIds));
        produto.setFornecedores(fornecedorSet);
        produtoService.salvar(produto);
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
        Produto produto = produtoService.obterPorId(id);
        List<Fornecedor> fornecedores = fornecedorService.listarTodos();
        model.addAttribute("produto", produto);
        model.addAttribute("fornecedores", fornecedores);
        return "produtos/editarProduto"; // Nome da página HTML do Thymeleaf (editar_produto.html)
    }

    // Salvar as alterações do produto após a edição
    @PostMapping("/atualizar/{id}")
    public String atualizarProduto(@PathVariable("id") Long id,
                                   @ModelAttribute Produto produto,
                                   @RequestParam("fornecedoresIds") List<Long> fornecedoresIds) {
        Set<Fornecedor> fornecedorSet = new HashSet<>(fornecedorService.obterPorIds(fornecedoresIds));
        produto.setFornecedores(fornecedorSet);
        produtoService.atualizar(id, produto);
        return "redirect:/produtos/listarProduto"; // Redireciona para a lista de produtos
    }

    // Apagar um produto
    @GetMapping("/apagar/{id}")
    public String apagarProduto(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            produtoService.apagar(id);
            redirectAttributes.addFlashAttribute("mensagem", "Produto apagado com sucesso.");
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("erro", "Não é possível apagar o produto, pois ele está associado a outros registros.");
        }
        return "redirect:/produtos/listarProduto"; // Redireciona para a lista de produtos
    }
}
