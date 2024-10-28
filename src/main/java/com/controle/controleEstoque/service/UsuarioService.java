package com.controle.controleEstoque.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.controle.controleEstoque.model.Usuario;
import com.controle.controleEstoque.model.UsuarioDetalhes;
import com.controle.controleEstoque.repository.UsuarioRepository;


@Service
public class UsuarioService implements UserDetailsService {

	@Autowired
    private UsuarioRepository usuarioRepository;


	public void salvar(Usuario usuario) {
	    if (usuarioRepository.findByEmail(usuario.getEmail()) != null) {
	        throw new RuntimeException("Email já está em uso");
	    }
	    usuarioRepository.save(usuario);
		    
	}
	
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(email);
        if (usuario == null) {
            throw new UsernameNotFoundException("Usuário não encontrado");
        }
        return new UsuarioDetalhes(usuario);
    }
}