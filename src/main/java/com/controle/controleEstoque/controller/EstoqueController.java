package com.controle.controleEstoque.controller;


import com.controle.controleEstoque.model.Estoque;
import com.controle.controleEstoque.model.Fornecedor;
import com.controle.controleEstoque.model.Historico;
import com.controle.controleEstoque.model.Produto;
import com.controle.controleEstoque.repository.EstoqueRepository;
import com.controle.controleEstoque.repository.HistoricoRepository;
import com.controle.controleEstoque.service.EstoqueService;
import com.controle.controleEstoque.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/estoque")
public class EstoqueController {

    private final EstoqueService estoqueService;

    private final ProdutoService produtoService;

    private final EstoqueRepository estoqueRepository;

    private HistoricoRepository historicoRepository;

    @Autowired
    public EstoqueController(EstoqueService estoqueService, ProdutoService produtoService, EstoqueRepository estoqueRepository) {
        this.estoqueService = estoqueService;
        this.produtoService = produtoService;
        this.estoqueRepository = estoqueRepository;
    }

    @GetMapping("/adicionarEstoque")
    public String mostrarFormularioAdicionar(Model model) {
        List<Produto> produtos = produtoService.listarTodos();
        model.addAttribute("estoque", new Estoque());
        model.addAttribute("produtos", produtos);
        return "estoque/adicionarEstoque"; // Página HTML para adicionar produtos ao estoque
    }

    @PostMapping("/salvar")
    public String salvarEstoque(@ModelAttribute Estoque estoque, @RequestParam("produtoId") Long produtoId) {
        Produto produto = produtoService.obterPorId(produtoId);
        Fornecedor fornecedor = produto.getFornecedores().iterator().next(); // Obtém o primeiro fornecedor associado
        estoque.setFornecedor(fornecedor);
        estoque.setProduto(produto);
        estoque.setPreco(produto.getPreco());
        estoqueService.salvar(estoque);
        return "redirect:/pagGeral"; // Redireciona para a página inicial
    }

    @GetMapping("/pagGeral")
    public String listarEstoque(Model model) {
        model.addAttribute("estoques", estoqueService.listarTodos());
        return "estoque/pagGeral"; // Página HTML para listar o estoque
    }

    @GetMapping("/registrarEntrada")
    public String exibirPaginaRegistrarEntrada(Model model) {
        model.addAttribute("estoques", estoqueService.listarTodos());
        return "estoque/registrarEntrada";
    }

    @PostMapping("/registrarEntrada")
    public String registrarEntrada(@RequestParam Long produtoId, @RequestParam int quantidade, RedirectAttributes redirectAttributes) {
        try {
            estoqueService.adicionarQuantidade(produtoId, quantidade);
            redirectAttributes.addFlashAttribute("mensagem", "Entrada registrada com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao registrar entrada: " + e.getMessage());
        }
        return "redirect:/pagGeral";
    }

    @GetMapping("/registrarSaida")
    public String exibirPaginaRegistrarSaida(Model model) {
        model.addAttribute("estoques", estoqueService.listarTodos());
        return "estoque/registrarSaida";
    }

    @PostMapping("/registrarSaida")
    public String registrarSaida(@RequestParam Long produtoId, @RequestParam int quantidade,
                                 @RequestParam String tipo, RedirectAttributes redirectAttributes) {
        try {
            estoqueService.removerQuantidade(produtoId, quantidade, tipo);
            redirectAttributes.addFlashAttribute("mensagem", "Saída registrada com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao registrar saída: " + e.getMessage());
        }
        return "redirect:/pagGeral";
    }
    @GetMapping("/editar/{id}")
    public String editarEstoque(@PathVariable Long id, Model model) {
        Optional<Estoque> estoqueOpt = estoqueRepository.findById(id);
        if (estoqueOpt.isPresent()) {
            Estoque estoque = estoqueOpt.get();
            model.addAttribute("estoque", estoque);
            return "estoque/editarEstoque"; // Página de edição
        }
        return "redirect:/pagGeral";
    }

    // Salvar edição (POST)
    @PostMapping("/editar")
    public String salvarEdicaoEstoque(@ModelAttribute Estoque estoque) {
        estoqueRepository.save(estoque);
        return "redirect:/pagGeral";
    }

    // Apagar produto (POST)
    @PostMapping("/apagar")
    public String apagarEstoque(@RequestParam Long id) {
        if (estoqueRepository.existsById(id)) {
            estoqueRepository.deleteById(id);
        }
        return "redirect:/pagGeral";
    }

    @GetMapping("/controleInventario")
    public String controleInventario(Model model) {
        List<Estoque> estoques = estoqueRepository.findAll();
        LocalDate hoje = LocalDate.now();

        // Atualizar o status para cada item do estoque
        for (Estoque estoque : estoques) {
            if (estoque.getDataValidade() != null) {
                if (estoque.getDataValidade().isBefore(hoje)) {
                    estoque.setStatus("Vencido");
                } else if (estoque.getDataValidade().isBefore(hoje.plusDays(7))) {
                    estoque.setStatus("Próximo da Validade");
                } else {
                    estoque.setStatus("Normal");
                }
            } else {
                estoque.setStatus("Sem Data de Validade");
            }
        }

        model.addAttribute("estoques", estoques);
        return "estoque/controleInventario";
    }

    @PostMapping("/atualizarMinimo")
    public String atualizarEstoqueMinimo(@RequestParam Long estoqueId, @RequestParam int estoqueMinimo) {
        Estoque estoque = estoqueRepository.findById(estoqueId).orElseThrow(() -> new IllegalArgumentException("Estoque não encontrado"));
        estoque.setEstoqueMinimo(estoqueMinimo);
        estoqueRepository.save(estoque);
        return "redirect:/estoque/controleInventario";
    }

}