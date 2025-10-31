package com.pos.techchallenge_backend.service;

import com.pos.techchallenge_backend.exception.custom.EmailAlreadyExistsException;
import com.pos.techchallenge_backend.exception.custom.InvalidPasswordException;
import com.pos.techchallenge_backend.exception.custom.ResourceNotFoundException;
import com.pos.techchallenge_backend.mapper.UserMapper;
import com.pos.techchallenge_backend.model.dto.user.PasswordUpdateRequest;
import com.pos.techchallenge_backend.model.dto.user.UserRegistrationRequest;
import com.pos.techchallenge_backend.model.dto.user.UserResponse;
import com.pos.techchallenge_backend.model.dto.user.UserUpdateRequest;
import com.pos.techchallenge_backend.model.entity.User;
import com.pos.techchallenge_backend.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponse registerUser(UserRegistrationRequest request) {
        // Validação obrigatória 1: Garantia de que o e-mail cadastrado seja único [cite: 22, 48]
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("E-mail já cadastrado: " + request.getEmail());
        }

        // Mapeia DTO para Entidade (resolvendo o Single Table Inheritance e criptografando a senha)
        User newUser = userMapper.mapRegistrationRequestToUser(request);

        // Requisito: Registro da data da última alteração [cite: 20, 38]
        newUser.setLastUpdateDate(LocalDateTime.now());

        User savedUser = userRepository.save(newUser);
        return userMapper.mapUserToUserResponse(savedUser);
    }
    @Transactional(readOnly = true)
    public List<UserResponse> findUsersByName(String name) {
        // Requisito: Busca de usuários pelo nome [cite: 21, 47]
        List<User> users = userRepository.findByNameContainingIgnoreCase(name);

        return users.stream()
                .map(userMapper::mapUserToUserResponse)
                .collect(Collectors.toList());
    }

    public UserResponse updateUserData(Long id, UserUpdateRequest request) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));

        // Validação: Verificar unicidade se o e-mail foi alterado [cite: 22, 48]
        if (!existingUser.getEmail().equals(request.getEmail())) {
            if (userRepository.findByEmail(request.getEmail()).isPresent()) {
                throw new EmailAlreadyExistsException("Novo e-mail já cadastrado por outro usuário.");
            }
        }

        // Atualiza campos (exceto senha e userType)
        existingUser.setName(request.getName());
        existingUser.setEmail(request.getEmail());
        existingUser.setLogin(request.getLogin());
        existingUser.setAddress(userMapper.mapAddressRequestToAddress(request.getAddress()));

        // Requisito: Registro da data da última alteração [cite: 20, 38]
        existingUser.setLastUpdateDate(LocalDateTime.now());

        User updatedUser = userRepository.save(existingUser);
        return userMapper.mapUserToUserResponse(updatedUser);
    }

    public void updatePassword(Long id, PasswordUpdateRequest request) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));

        // Validação de Segurança: Verificar se a senha atual confere (uso do PasswordEncoder)
        if (!passwordEncoder.matches(request.getCurrentPassword(), existingUser.getPassword())) {
            throw new InvalidPasswordException("Senha atual incorreta.");
        }

        // Atualiza senha e data de alteração
        existingUser.setPassword(passwordEncoder.encode(request.getNewPassword()));

        // Requisito: Registro da data da última alteração [cite: 20, 38]
        existingUser.setLastUpdateDate(LocalDateTime.now());

        userRepository.save(existingUser);
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("Usuário não encontrado.");
        }
        userRepository.deleteById(id);
    }
}
