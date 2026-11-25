package com.amintech.rag.exception;

import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> pedidoInvalido(MethodArgumentNotValidException e) {
        String mensagem = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .findFirst()
                .orElse("Pedido inválido");
        return ResponseEntity.badRequest().body(Map.of("erro", mensagem));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> corpoInvalido(HttpMessageNotReadableException e) {
        return ResponseEntity.badRequest().body(Map.of("erro", "Corpo do pedido inválido"));
    }

    @ExceptionHandler(LlmException.class)
    public ResponseEntity<Map<String, String>> problemaNoModelo(LlmException e) {
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(Map.of("erro", e.getMessage()));
    }
}
