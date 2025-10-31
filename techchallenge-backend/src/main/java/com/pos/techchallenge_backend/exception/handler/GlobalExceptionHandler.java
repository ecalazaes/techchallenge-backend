package com.pos.techchallenge_backend.exception.handler;

import com.pos.techchallenge_backend.exception.custom.EmailAlreadyExistsException;
import com.pos.techchallenge_backend.exception.custom.InvalidLoginCredentialsException;
import com.pos.techchallenge_backend.exception.custom.InvalidPasswordException;
import com.pos.techchallenge_backend.exception.custom.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;
import java.time.Instant;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    // URI base para o tipo de erro (pode ser um link para a documentação da API)
    private static final URI BASE_URI = URI.create("/problem-details");

    /**
     * Manipula exceções de E-mail Duplicado (409 Conflict)
     */
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ProblemDetail> handleEmailAlreadyExistsException(EmailAlreadyExistsException ex) {
        HttpStatus status = HttpStatus.CONFLICT;

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, ex.getMessage());
        problemDetail.setTitle("E-mail já cadastrado");
        problemDetail.setType(BASE_URI.resolve("/email-already-exists"));
        problemDetail.setProperty("timestamp", Instant.now());

        return ResponseEntity.status(status).body(problemDetail);
    }

    /**
     * Manipula exceções de Recurso Não Encontrado (404 Not Found)
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleResourceNotFoundException(ResourceNotFoundException ex) {
        HttpStatus status = HttpStatus.NOT_FOUND;

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, ex.getMessage());
        problemDetail.setTitle("Recurso não encontrado");
        problemDetail.setType(BASE_URI.resolve("/resource-not-found"));
        problemDetail.setProperty("timestamp", Instant.now());

        return ResponseEntity.status(status).body(problemDetail);
    }

    /**
     * Manipula exceções de Senha Inválida (400 Bad Request) - Uso na troca de senha
     */
    @ExceptionHandler({InvalidPasswordException.class, InvalidLoginCredentialsException.class})
    public ResponseEntity<ProblemDetail> handleInvalidCredentialsException(RuntimeException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, ex.getMessage());
        problemDetail.setTitle("Credenciais Inválidas");
        problemDetail.setType(BASE_URI.resolve("/invalid-credentials"));
        problemDetail.setProperty("timestamp", Instant.now());

        return ResponseEntity.status(status).body(problemDetail);
    }

    /**
     * Manipula exceções de Validação de DTOs (@Valid / MethodArgumentNotValidException)
     * Sobrescreve o método do ResponseEntityExceptionHandler para usar ProblemDetail.
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            // Tipo corrigido para HttpHeaders
            org.springframework.http.HttpHeaders headers,
            // Tipo corrigido para HttpStatusCode
            HttpStatusCode status,
            WebRequest request) {

        // O corpo do seu método é adaptado para usar HttpStatusCode
        HttpStatus httpStatus = HttpStatus.valueOf(status.value());

        // Coleta todos os erros de validação
        String detail = "Campos obrigatórios inválidos: " + ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + " - " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, detail);
        problemDetail.setTitle("Erro de Validação de Campos");
        problemDetail.setType(BASE_URI.resolve("/invalid-fields"));
        problemDetail.setProperty("timestamp", Instant.now());

        return ResponseEntity.badRequest().body(problemDetail);
    }
}
