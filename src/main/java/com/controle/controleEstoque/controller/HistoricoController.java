package com.controle.controleEstoque.controller;

import com.controle.controleEstoque.model.Historico;
import com.controle.controleEstoque.repository.HistoricoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
}