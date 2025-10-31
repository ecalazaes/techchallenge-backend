package com.pos.techchallenge_backend.service;

import com.pos.techchallenge_backend.exception.custom.InvalidLoginCredentialsException;
import com.pos.techchallenge_backend.model.dto.user.LoginRequest;
import com.pos.techchallenge_backend.model.entity.User;
import com.pos.techchallenge_backend.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class LoginService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public LoginService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean validateLogin(LoginRequest request) {
        // 1. Buscar o usuário pelo login fornecido
        Optional<User> userOptional = userRepository.findByLogin(request.getLogin());

        // 2. Verificar se o usuário existe
        if (userOptional.isEmpty()) {
            throw new InvalidLoginCredentialsException("Login ou senha inválidos.");
        }

        User user = userOptional.get();

        // 3. Verificar se a senha confere (usando o PasswordEncoder)
        // matches(Senha_Texto_Puro, Senha_Criptografada_BD)
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidLoginCredentialsException("Login ou senha inválidos.");
        }

        // Se chegou até aqui, o login é válido
        return true;
    }

}
