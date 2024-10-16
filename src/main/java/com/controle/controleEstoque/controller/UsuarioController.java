package com.controle.controleEstoque.controller;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.controle.controleEstoque.model.Usuario;
import com.controle.controleEstoque.service.UsuarioService;

@Controller
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final PasswordEncoder passwordEncoder;

    public UsuarioController(UsuarioService usuarioService, PasswordEncoder passwordEncoder) {
        this.usuarioService = usuarioService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/registrar")
    public String exibirFormularioCadastro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "cadastro/registrar";
    }

    @PostMapping("/registrar")
    public String cadastrarUsuario(Usuario usuario) {
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        usuarioService.salvar(usuario);
        return "redirect:cadastro/login";
    }

    @GetMapping("/login")
    public String exibirFormularioLogin() {
        return "cadastro/login";
    }

    @GetMapping("/inicio")
    public String paginaInicial() {
        return "PagGeral";
    }
}
