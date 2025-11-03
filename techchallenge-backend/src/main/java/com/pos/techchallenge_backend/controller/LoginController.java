package com.pos.techchallenge_backend.controller;

import com.pos.techchallenge_backend.model.dto.user.LoginRequest;
import com.pos.techchallenge_backend.service.LoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @RestController
 * Camada de Controller responsável por expor o endpoint de validação de login.
 * Garante o versionamento da API e a validação do corpo da requisição.
 * @author Erick Calazães
 */
@RestController
@RequestMapping("/api/v1/login")
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @Operation(summary = "Valida as credenciais de login",
            description = "Verifica se o login e a senha fornecidos são válidos. Se for bem-sucedido, retorna mensagem de login bem sucedio.")
    @ApiResponse(responseCode = "200", description = "Login bem-sucedido", content = @Content)
    @ApiResponse(responseCode = "400", description = "Credenciais inválidas ou requisição malformada",
            content = @Content(mediaType = "application/problem+json",
                    examples = @ExampleObject(
                            name = "Falha de Credenciais (ProblemDetail)",
                            value = "{\"type\": \"/problem-details/invalid-credentials\", \"title\": \"Credenciais Inválidas\", \"status\": 400, \"detail\": \"Login ou senha inválidos.\", \"timestamp\": \"2025-11-03T21:00:00Z\"}"
                    )
            ))
    @PostMapping // Endpoint de login separado, fora do /users
    public ResponseEntity<Map<String, String>> validateLogin(@Valid @RequestBody LoginRequest request) {
        // Chama o serviço de validação. Se falhar, uma exceção (400 Bad Request) será lançada.
        loginService.validateLogin(request);

        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Login bem-sucedido.");

        // Retorna 200 OK ou 204 No Content para indicar sucesso na validação.
        return ResponseEntity.ok(response);
    }
}
