package com.pos.techchallenge_backend.service;

import com.pos.techchallenge_backend.exception.custom.InvalidLoginCredentialsException;
import com.pos.techchallenge_backend.model.dto.user.LoginRequest;
import com.pos.techchallenge_backend.model.entity.User;
import com.pos.techchallenge_backend.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @Service
 * Implementa o requisito obrigatório de Validação de Login.
 * Este serviço é responsável por verificar se as credenciais (login e senha)
 * fornecidas pelo usuário são válidas, utilizando o PasswordEncoder para
 * segurança na comparação da senha.
 * @author Erick Calazães
 */
@Service
@Transactional(readOnly = true)
public class LoginService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public LoginService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Requisito: Serviço que verifica se login e senha são válidos.
     * 1. Busca o usuário pelo login.
     * 2. Compara a senha em texto puro do Request com a senha criptografada do banco.
     * @param request DTO contendo o login e a senha para validação.
     * @return boolean True se o login for bem-sucedido.
     * @throws InvalidLoginCredentialsException Se o login ou a senha forem inválidos.
     */
    public boolean validateLogin(LoginRequest request) {
        // Buscar o usuário pelo login fornecido
        Optional<User> userOptional = userRepository.findByLogin(request.getLogin());

        // Verificar se o usuário existe
        if (userOptional.isEmpty()) {
            throw new InvalidLoginCredentialsException("Login ou senha inválidos.");
        }

        User user = userOptional.get();

        // Verificar se a senha confere (usando o PasswordEncoder)
        // matches(Senha_Texto_Puro, Senha_Criptografada_BD)
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidLoginCredentialsException("Login ou senha inválidos.");
        }

        // login é válido
        return true;
    }

}
