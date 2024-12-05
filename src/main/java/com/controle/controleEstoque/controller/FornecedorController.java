package com.controle.controleEstoque.controller;

import com.controle.controleEstoque.model.Fornecedor;
import com.controle.controleEstoque.service.FornecedorService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/fornecedores")
public class FornecedorController {

    private final FornecedorService fornecedorService;

    public FornecedorController(FornecedorService fornecedorService) {
        this.fornecedorService = fornecedorService;
    }

    @GetMapping("/cadastrarFornecedor")
    public String exibirFormularioCadastro(Model model) {
        model.addAttribute("fornecedor", new Fornecedor());
        return "fornecedores/cadastrarFornecedor";
    }

    @PostMapping("/salvarFornecedor")
    public String salvarFornecedor(@ModelAttribute Fornecedor fornecedor, RedirectAttributes redirectAttributes) {
        try {
            fornecedorService.salvar(fornecedor); // Chama o método com validação
            redirectAttributes.addFlashAttribute("mensagem", "Fornecedor cadastrado com sucesso!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao cadastrar fornecedor: " + e.getMessage());
            return "redirect:/fornecedores/cadastrarFornecedor";
        }
        return "redirect:/fornecedores/listarFornecedor";
    }

    @GetMapping("/listarFornecedor")
    public String listarFornecedores(Model model) {
        model.addAttribute("fornecedores", fornecedorService.listarTodos());
        return "fornecedores/listarFornecedor";
    }

    @GetMapping("/editar/{id}")
    public String editarFornecedor(@PathVariable Long id, Model model) {
        Fornecedor fornecedor = fornecedorService.obterPorId(id);
        model.addAttribute("fornecedor", fornecedor);
        return "fornecedores/editarFornecedor";
    }

    @PostMapping("/atualizar/{id}")
    public String atualizarFornecedor(@PathVariable("id") Long id, @ModelAttribute Fornecedor fornecedorAtualizado) {
        fornecedorService.atualizar(id, fornecedorAtualizado); // Atualiza o fornecedor no banco de dados
        return "redirect:/fornecedores/listarFornecedor"; // Redireciona para a lista de fornecedores
    }

    @GetMapping("/deletar/{id}")
    public String deletarFornecedor(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            fornecedorService.deletar(id);
            redirectAttributes.addFlashAttribute("mensagem", "Fornecedor apagado com sucesso.");
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("erro", "Não é possível apagar o fornecedor, pois ele está associado a um ou mais produtos.");
        }
        return "redirect:/fornecedores/listarFornecedor";
    }
}