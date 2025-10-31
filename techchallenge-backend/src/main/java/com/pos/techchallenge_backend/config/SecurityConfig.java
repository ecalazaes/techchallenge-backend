package com.pos.techchallenge_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Configuração para desabilitar a proteção para os endpoints da API
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // Desabilita a proteção CSRF (necessário para APIs REST)
        http.csrf(AbstractHttpConfigurer::disable)
                // Configura a autorização de requisições
                .authorizeHttpRequests(auth -> auth
                        // Permite acesso irrestrito aos seus endpoints REST (Cadastro, Login, etc.)
                        .requestMatchers("/api/v1/**").permitAll()
                        // Permite acesso irrestrito à documentação Swagger/OpenAPI
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        // Qualquer outra requisição é permitida (sem proteção)
                        .anyRequest().permitAll()
                );

        // O método .httpBasic() e .formLogin() (que causa a tela de login)
        // são removidos ou ignorados pela configuração acima.

        return http.build();
    }
}
