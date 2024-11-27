package com.controle.controleEstoque.controller;


import com.controle.controleEstoque.model.Estoque;
import com.controle.controleEstoque.model.UsuarioDetalhes;
import com.controle.controleEstoque.service.EstoqueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.controle.controleEstoque.model.Usuario;
import com.controle.controleEstoque.service.UsuarioService;

import java.util.List;

@Controller
public class UsuarioController {


	@Autowired
	private UsuarioService usuarioService;
    private final EstoqueService estoqueService;
    public UsuarioController(EstoqueService estoqueService) {
        this.estoqueService = estoqueService;
    }
	
    @Autowired
	private PasswordEncoder passwordEncoder;

    
    @GetMapping("/registrar")
    public String exibirFormularioCadastro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "cadastro/registrar";
    }

    @PostMapping("/registrar")
    public String cadastrarUsuario(@ModelAttribute Usuario usuario, Model modelo) {
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        usuarioService.salvar(usuario);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String exibirFormularioLogin() {
        return "cadastro/login";
    }

    @GetMapping("/pagGeral")
    public String paginaInicial(Model model) {
        List<Estoque> estoques = estoqueService.listarTodos(); // Adiciona a lógica do estoque
        model.addAttribute("estoques", estoques);
        return "pagGeral";
    }
}
