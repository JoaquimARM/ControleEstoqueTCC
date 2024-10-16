package com.controle.controleEstoque.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.controle.controleEstoque.service.UsuarioService;

@Configuration
public class SecurityConfig {

    private final UsuarioService usuarioService;

    public SecurityConfig(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Desabilitar CSRF temporariamente (dependendo da necessidade)
            .authorizeHttpRequests(auth -> auth
            	.requestMatchers("/css/**", "/js/**", "/img/**").permitAll()
                .requestMatchers("/cadastro/registrar", "/cadastro/login").permitAll()  // URLs públicas
                .requestMatchers("/admin/**").hasRole("ADMIN")        // URLs com acesso de administrador
                .requestMatchers("/pagGeral").hasAnyRole("ADMIN", "USER") // Página inicial para usuários logados
                .anyRequest().authenticated()                           // Qualquer outra URL requer autenticação
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/pagGeral", true)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/login")
                .permitAll()
            );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return usuarioService;
    }
}
