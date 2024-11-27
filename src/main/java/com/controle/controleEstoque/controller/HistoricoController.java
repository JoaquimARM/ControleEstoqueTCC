package com.controle.controleEstoque.controller;

import com.controle.controleEstoque.model.Historico;
import com.controle.controleEstoque.repository.HistoricoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
public class HistoricoController {

    @Autowired
    private HistoricoRepository historicoRepository;

    @GetMapping("/historico")
    public String exibirHistorico(Model model) {
        List<Historico> historicos = historicoRepository.findAll();

        historicos.forEach(h -> {
            h.setDataFormatada(h.getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        });

        model.addAttribute("historicos", historicos);
        return "historico";
    }
}