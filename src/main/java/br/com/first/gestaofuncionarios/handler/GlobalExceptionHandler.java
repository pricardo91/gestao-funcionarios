package br.com.first.gestaofuncionarios.handler;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(APIException.class)
    public ResponseEntity<ErrorApiResponse> handleAPIException(APIException ex, HttpServletRequest request) {

        ex.getBodyException().setPath(request.getRequestURI());
        log.warn("APIException capturada: {} - Path: {}", ex.getMessage(), request.getRequestURI());
        return ex.buildResponseEntity();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorApiResponse> handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String descricao = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " +
                        (error.getDefaultMessage() != null ? error.getDefaultMessage() : "Erro de validação"))
                .collect(Collectors.joining("; "));

        ErrorApiResponse errorApiResponse = ErrorApiResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .message("Erro de validação nos dados enviados")
                .description(descricao)
                .path(request.getRequestURI())
                .build();

        log.warn("Erro de validação: {} - Path: {}", descricao, request.getRequestURI());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(errorApiResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorApiResponse> handleGenericException(
            Exception ex,
            HttpServletRequest request) {

        log.error("Erro não tratado: {} - Path: {}",
                ex.getMessage(), request.getRequestURI(), ex);

        ErrorApiResponse errorResponse = ErrorApiResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .message("Erro interno do servidor")
                .description("Entre em contato com o suporte se o problema persistir")
                .path(request.getRequestURI())
                .build();

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorResponse);
    }
}
