package com.pos.techchallenge_backend.controller;

import com.pos.techchallenge_backend.model.dto.user.PasswordUpdateRequest;
import com.pos.techchallenge_backend.model.dto.user.UserRegistrationRequest;
import com.pos.techchallenge_backend.model.dto.user.UserResponse;
import com.pos.techchallenge_backend.model.dto.user.UserUpdateRequest;
import com.pos.techchallenge_backend.service.LoginService;
import com.pos.techchallenge_backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    private final LoginService loginService;

    public UserController(UserService userService, LoginService loginService) {
        this.userService = userService;
        this.loginService = loginService;
    }

    /**
     * Requisito: Cadastro de Usuários (POST /api/v1/users)
     */
    @PostMapping
    public ResponseEntity<UserResponse> registerUser(@Valid @RequestBody UserRegistrationRequest request) {
        // O @Valid garante que o DTO seja validado (capturado pelo GlobalExceptionHandler)
        UserResponse response = userService.registerUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Requisito: Busca de usuários pelo nome (GET /api/v1/users?name={name})
     */
    @GetMapping
    public ResponseEntity<List<UserResponse>> findUsersByName(@RequestParam String name) {
        List<UserResponse> users = userService.findUsersByName(name);
        return ResponseEntity.ok(users);
    }

    /**
     * Requisito: Atualização das demais informações do usuário (PUT /api/v1/users/{id}/data)
     */
    // Requisito: Deve ser um endpoint distinto do endpoint de senha [cite: 19, 51]
    @PutMapping("/{id}/data")
    public ResponseEntity<UserResponse> updateUserData(@PathVariable Long id,
                                                       @Valid @RequestBody UserUpdateRequest request) {
        UserResponse response = userService.updateUserData(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Requisito: Troca de senha do usuário (PUT /api/v1/users/{id}/password)
     */
    // Requisito: Deve ser um endpoint separado [cite: 18, 50]
    @PutMapping("/{id}/password")
    public ResponseEntity<Void> updatePassword(@PathVariable Long id,
                                               @Valid @RequestBody PasswordUpdateRequest request) {
        userService.updatePassword(id, request);
        return ResponseEntity.noContent().build(); // Retorna 204 No Content para sucesso
    }

    /**
     * Requisito: Exclusão de usuários (DELETE /api/v1/users/{id})
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build(); // Retorna 204 No Content para sucesso
    }
}
