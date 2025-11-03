package com.pos.techchallenge_backend.controller;

import com.pos.techchallenge_backend.model.dto.user.PasswordUpdateRequest;
import com.pos.techchallenge_backend.model.dto.user.UserRegistrationRequest;
import com.pos.techchallenge_backend.model.dto.user.UserResponse;
import com.pos.techchallenge_backend.model.dto.user.UserUpdateRequest;
import com.pos.techchallenge_backend.service.LoginService;
import com.pos.techchallenge_backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @RestController
 * Camada de Controller responsável por expor os endpoints REST para gestão da entidade User.
 * Implementa o versionamento (/api/v1) e lida com as requisições de CRUD, busca,
 * atualização de dados e troca de senha (endpoints distintos).
 * @author Erick Calazães
 */
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
    @Operation(summary = "Cria um novo usuário (Cliente ou Dono de Restaurante)",
            description = "Garante que o e-mail seja único e criptografa a senha.")
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
            // 1. Resposta de SUCESSO (201 Created)
            @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponse.class),
                            examples = @ExampleObject(
                                    name = "Exemplo Sucesso",
                                    value = "{\"id\": 101, \"name\": \"João da Silva\", \"email\": \"joao.silva@teste.com\", \"login\": \"joao.cliente\", \"userType\": \"CLIENT\", \"lastUpdatedDate\": \"2025-11-03T18:00:00\", \"address\": {\"street\": \"Rua das Flores\", \"number\": \"100A\", \"city\": \"São Paulo\", \"zipCode\": \"01234567\"}}"
                            )
                    )),
            // 2. Resposta de ERRO (409 Conflict - E-mail Duplicado)
            @ApiResponse(responseCode = "409", description = "E-mail já cadastrado (Regra de Negócio)",
                    content = @Content(mediaType = "application/problem+json",
                            examples = @ExampleObject(
                                    name = "Conflito de E-mail (ProblemDetail)",
                                    value = "{\"type\": \"/problem-details/email-already-exists\", \"title\": \"E-mail já cadastrado\", \"status\": 409, \"detail\": \"E-mail já cadastrado: joao.silva@teste.com\", \"timestamp\": \"2025-11-03T21:00:00Z\"}"
                            )
                    )),
            // 3. Resposta de ERRO (400 Bad Request - Validação)
            @ApiResponse(responseCode = "400", description = "Erro de validação (Campos obrigatórios)",
                    content = @Content(mediaType = "application/problem+json",
                            examples = @ExampleObject(
                                    name = "Erro de Validação (ProblemDetail)",
                                    value = "{\"type\": \"/problem-details/invalid-fields\", \"title\": \"Erro de Validação de Campos\", \"status\": 400, \"detail\": \"Campos obrigatórios inválidos: name - Nome é obrigatório\", \"timestamp\": \"2025-11-03T21:00:00Z\"}"
                            )
                    ))
    })
    @PostMapping
    public ResponseEntity<UserResponse> registerUser(@Valid @RequestBody UserRegistrationRequest request) {
        // O @Valid garante que o DTO seja validado (capturado pelo GlobalExceptionHandler)
        UserResponse response = userService.registerUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Requisito: Busca de usuários pelo nome (GET /api/v1/users?name={name})
     */
    @Operation(summary = "Busca usuários por nome",
            description = "Retorna uma lista de usuários cujo nome contenha o termo de busca (case-insensitive).")
    @ApiResponse(responseCode = "200", description = "Lista de usuários encontrada",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserResponse.class)))
    @GetMapping
    public ResponseEntity<List<UserResponse>> findUsersByName(@RequestParam String name) {
        List<UserResponse> users = userService.findUsersByName(name);
        return ResponseEntity.ok(users);
    }

    /**
     * Requisito: Atualização das demais informações do usuário (PUT /api/v1/users/{id}/data)
     */
    // Requisito: Deve ser um endpoint distinto do endpoint de senha [cite: 19, 51]
    @Operation(summary = "Atualiza dados gerais do usuário (Nome, E-mail, Login, Endereço)",
            description = "Endpoint distinto para atualizar dados, mas não a senha. Atualiza a data da última alteração.")
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dados atualizados com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado",
                    content = @Content(mediaType = "application/problem+json")),
            @ApiResponse(responseCode = "409", description = "E-mail ou Login já cadastrado",
                    content = @Content(mediaType = "application/problem+json",
                            examples = @ExampleObject(
                                    name = "Conflito de E-mail (ProblemDetail)",
                                    value = "{\"type\": \"/problem-details/email-already-exists\", \"title\": \"E-mail já cadastrado\", \"status\": 409, \"detail\": \"Novo e-mail já cadastrado: outro.email@teste.com\", \"timestamp\": \"2025-11-03T21:00:00Z\"}"
                            )
                    ))
    })
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
    @Operation(summary = "Troca a senha do usuário",
            description = "Endpoint exclusivo para troca de senha. Requer a senha atual para validação.")
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Senha alterada com sucesso", content = @Content),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado",
                    content = @Content(mediaType = "application/problem+json")),
            @ApiResponse(responseCode = "400", description = "Senha atual incorreta",
                    content = @Content(mediaType = "application/problem+json",
                            examples = @ExampleObject(
                                    name = "Senha Atual Incorreta (ProblemDetail)",
                                    value = "{\"type\": \"/problem-details/invalid-credentials\", \"title\": \"Credenciais Inválidas\", \"status\": 400, \"detail\": \"Senha atual incorreta.\", \"timestamp\": \"2025-11-03T21:00:00Z\"}"
                            )
                    ))
    })
    @PutMapping("/{id}/password")
    public ResponseEntity<Void> updatePassword(@PathVariable Long id,
                                               @Valid @RequestBody PasswordUpdateRequest request) {
        userService.updatePassword(id, request);
        return ResponseEntity.noContent().build(); // Retorna 204 No Content para sucesso
    }

    /**
     * Requisito: Exclusão de usuários (DELETE /api/v1/users/{id})
     */
    @Operation(summary = "Exclui um usuário",
            description = "Remove o usuário do sistema pelo ID.")
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuário excluído com sucesso", content = @Content),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado",
                    content = @Content(mediaType = "application/problem+json"))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build(); // Retorna 204 No Content para sucesso
    }
}
