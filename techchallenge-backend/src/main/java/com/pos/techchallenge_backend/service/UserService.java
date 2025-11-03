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

/**
 * @Service
 * Camada de Serviço responsável pela regra de negócio e gestão completa da entidade User.
 * * Implementa os requisitos obrigatórios de CRUD, unicidade de e-mail e registro
 * da data da última alteração. Segue o padrão SOLID de Responsabilidade Única.
 *  @author Erick Calazães
 */
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

    // ----------------------------------------------------------------------
    // 1. REQUISITO: CADASTRO (Criação)
    // ----------------------------------------------------------------------
    /**
     * Requisito: Cadastro de Usuário (Dono de restaurante ou Cliente)
     * 1. Garante que o e-mail seja único.
     * 2. Criptografa a senha antes de persistir.
     * 3. Registra a data da última alteração.
     * @param request DTO com os dados do usuário a ser registrado.
     * @return UserResponse DTO do usuário criado.
     * @throws EmailAlreadyExistsException Se o e-mail já estiver em uso.
     */
    public UserResponse registerUser(UserRegistrationRequest request) {
        // Garantia de que o e-mail cadastrado seja único
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("E-mail já cadastrado: " + request.getEmail());
        }

        // Mapeia DTO para Entidade (resolvendo o Single Table Inheritance e criptografando a senha)
        User newUser = userMapper.mapRegistrationRequestToUser(request);

        // Registro da data da última alteração
        newUser.setLastUpdateDate(LocalDateTime.now());

        User savedUser = userRepository.save(newUser);
        return userMapper.mapUserToUserResponse(savedUser);
    }

    // ----------------------------------------------------------------------
    // 2. REQUISITO: BUSCA PELO NOME
    // ----------------------------------------------------------------------
    /**
     * Requisito: Busca de usuários pelo nome.
     * Retorna uma lista de usuários cujo nome contenha o termo de busca (case-insensitive).
     * @param name Termo de busca (parte do nome).
     * @return List<UserResponse> Lista de usuários encontrados.
     */
    @Transactional(readOnly = true)
    public List<UserResponse> findUsersByName(String name) {
        // Busca de usuários pelo nome
        List<User> users = userRepository.findByNameContainingIgnoreCase(name);

        return users.stream()
                .map(userMapper::mapUserToUserResponse)
                .collect(Collectors.toList());
    }

    // ----------------------------------------------------------------------
    // 3. REQUISITO: ATUALIZAÇÃO DE DADOS (Endpoint Distinto)
    // ----------------------------------------------------------------------
    /**
     * Requisito: Atualização de dados em endpoint distinto (PUT /{id}/data).
     * 1. Atualiza nome, e-mail, login e endereço.
     * 2. Garante que o novo e-mail (se alterado) não esteja em uso por outro usuário.
     * 3. Atualiza a data da última alteração.
     * @param id ID do usuário a ser atualizado.
     * @param request DTO com os novos dados.
     * @return UserResponse DTO do usuário atualizado.
     * @throws ResourceNotFoundException Se o usuário não for encontrado.
     * @throws EmailAlreadyExistsException Se o novo e-mail já estiver em uso.
     */
    public UserResponse updateUserData(Long id, UserUpdateRequest request) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));

        // Verificar unicidade se o e-mail foi alterado
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

        // Registro da data da última alteração
        existingUser.setLastUpdateDate(LocalDateTime.now());

        User updatedUser = userRepository.save(existingUser);
        return userMapper.mapUserToUserResponse(updatedUser);
    }

    // ----------------------------------------------------------------------
    // 4. REQUISITO: TROCA DE SENHA (Endpoint Exclusivo)
    // ----------------------------------------------------------------------
    /**
     * Requisito: Troca de senha do usuário em endpoint separado (PUT /{id}/password).
     * 1. Valida a senha atual fornecida pelo usuário antes de aplicar a nova.
     * 2. Criptografa a nova senha.
     * 3. Registra a data da última alteração.
     * @param id ID do usuário.
     * @param request DTO contendo a senha atual e a nova senha.
     * @throws ResourceNotFoundException Se o usuário não for encontrado.
     * @throws InvalidPasswordException Se a senha atual estiver incorreta.
     */
    public void updatePassword(Long id, PasswordUpdateRequest request) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));

        // Verificar se a senha atual confere (uso do PasswordEncoder)
        if (!passwordEncoder.matches(request.getCurrentPassword(), existingUser.getPassword())) {
            throw new InvalidPasswordException("Senha atual incorreta.");
        }

        // Atualiza senha e data de alteração
        existingUser.setPassword(passwordEncoder.encode(request.getNewPassword()));

        // Registro da data da última alteração
        existingUser.setLastUpdateDate(LocalDateTime.now());

        userRepository.save(existingUser);
    }

    // ----------------------------------------------------------------------
    // 5. REQUISITO: EXCLUSÃO (Delete)
    // ----------------------------------------------------------------------
    /**
     * Requisito: Exclusão de usuário (Delete /users/{id}).
     * @param id ID do usuário a ser excluído.
     * @throws ResourceNotFoundException Se o usuário não for encontrado.
     */
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("Usuário não encontrado.");
        }
        userRepository.deleteById(id);
    }
}
