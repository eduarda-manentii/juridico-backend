package br.com.attus.gerenciamentoprocessos.exceptions.handler;


import br.com.attus.gerenciamentoprocessos.exceptions.DuplicidadeDocumentoException;
import br.com.attus.gerenciamentoprocessos.exceptions.EntidadeEmUsoException;
import br.com.attus.gerenciamentoprocessos.exceptions.ObrigatoriedadeIdException;
import br.com.attus.gerenciamentoprocessos.exceptions.TokenInvalidoException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntityNotFoundException.class)
    public Map<String, String> handleEntityNotFoundException(EntityNotFoundException ex) {
        return Map.of("mensagem", ex.getMessage());
    }

    @ExceptionHandler(TokenInvalidoException.class)
    public ResponseEntity<String> handleTokenInvalidoException(TokenInvalidoException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Token inválido ou expirado.");
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DuplicidadeDocumentoException.class)
    public String handleDuplicidadeDocumentoException(DuplicidadeDocumentoException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        Throwable cause = ex.getCause();
        if (cause instanceof InvalidFormatException invalidFormatException) {
            Class<?> targetType = invalidFormatException.getTargetType();
            String msg = "Valor inválido para o campo do tipo " + targetType.getSimpleName();
            if (targetType.isEnum()) {
                Object[] enumConstants = targetType.getEnumConstants();
                String options = Arrays.stream(enumConstants)
                        .map(Object::toString)
                        .collect(Collectors.joining(", "));
                msg += ". Opções válidas: [" + options + "]";
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(msg);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Requisição mal formada.");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException ex) {
        Map<String, String> body = new HashMap<>();
        body.put("mensagem", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ObrigatoriedadeIdException.class)
    public String handleObrigatoriedadeIdException(ObrigatoriedadeIdException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(EntidadeEmUsoException.class)
    public ResponseEntity<String> handleEntidadeEmUsoException(EntidadeEmUsoException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

}
