package com.pos.techchallenge_backend.controller;

import com.pos.techchallenge_backend.model.dto.user.LoginRequest;
import com.pos.techchallenge_backend.service.LoginService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/login")
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping // Endpoint de login separado, fora do /users
    public ResponseEntity<Void> validateLogin(@Valid @RequestBody LoginRequest request) {
        // Chama o serviço de validação. Se falhar, uma exceção (400 Bad Request) será lançada.
        loginService.validateLogin(request);

        // Retorna 200 OK ou 204 No Content para indicar sucesso na validação.
        return ResponseEntity.ok().build();
    }
}
