package com.controle.controleEstoque.controller;

import com.controle.controleEstoque.model.Historico;
import com.controle.controleEstoque.repository.HistoricoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/historico")
public class HistoricoController {

    private final HistoricoRepository historicoRepository;

    public HistoricoController(HistoricoRepository historicoRepository) {
        this.historicoRepository = historicoRepository;
    }

    @GetMapping
    public String exibirHistorico(Model model) {
        List<Historico> historico = historicoRepository.findAllByOrderByDataDesc();
        model.addAttribute("historicos", historico);
        return "historico";
    }
    @PostMapping("/limpar")
    public String limparHistorico(RedirectAttributes redirectAttributes) {
        try {
            historicoRepository.deleteAll();
            redirectAttributes.addFlashAttribute("mensagem", "Histórico limpo com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao limpar histórico: " + e.getMessage());
        }
        return "redirect:/historico";
    }
    @PostMapping("/apagar/{id}")
    public String apagarLinhaHistorico(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            historicoRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("mensagem", "Linha do histórico apagada com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao apagar linha do histórico: " + e.getMessage());
        }
        return "redirect:/historico";
    }
}