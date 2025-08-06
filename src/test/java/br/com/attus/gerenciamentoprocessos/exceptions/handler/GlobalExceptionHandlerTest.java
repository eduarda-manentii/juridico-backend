package br.com.attus.gerenciamentoprocessos.exceptions.handler;


import br.com.attus.gerenciamentoprocessos.exceptions.*;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
    }

    @Test
    void handleTokenInvalidoException_deveRetornarUnauthorized() {
        TokenInvalidoException ex = new TokenInvalidoException();

        ResponseEntity<String> response = handler.handleTokenInvalidoException(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody()).isEqualTo("Token inválido ou expirado.");
    }

    @Test
    void handleDuplicidadeDocumentoException_deveRetornarMensagemComConflict() {
        DuplicidadeDocumentoException ex = new DuplicidadeDocumentoException("Duplicidade");

        String response = handler.handleDuplicidadeDocumentoException(ex);

        assertThat(response).isEqualTo("Documento já existente.");
    }

    @Test
    void handleValidationExceptions_deveRetornarMapaDeErrosComBadRequest() {
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("obj", "campo", "erro");

        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));

        ResponseEntity<Map<String, String>> response = handler.handleValidationExceptions(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).containsEntry("campo", "erro");
    }

    @Test
    void handleHttpMessageNotReadableException_comOutraExcecao_deveRetornarMensagemPadrao() {
        HttpMessageNotReadableException ex = mock(HttpMessageNotReadableException.class);

        when(ex.getCause()).thenReturn(new RuntimeException());

        ResponseEntity<String> response = handler.handleHttpMessageNotReadableException(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo("Requisição mal formada.");
    }

    @Test
    void handleEntityNotFoundException_deveRetornarMapaComMensagem() {
        EntityNotFoundException ex = new EntityNotFoundException("Não encontrado");

        Map<String, String> response = handler.handleEntityNotFoundException(ex);

        assertThat(response).containsEntry("mensagem", "Não encontrado");
    }

    @Test
    void handleIllegalArgumentException_deveRetornarBadRequestComMensagem() {
        IllegalArgumentException ex = new IllegalArgumentException("Argumento inválido");

        ResponseEntity<Map<String, String>> response = handler.handleIllegalArgumentException(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).containsEntry("mensagem", "Argumento inválido");
    }

    @Test
    void handleObrigatoriedadeIdException_deveRetornarMensagem() {
        ObrigatoriedadeIdException ex = new ObrigatoriedadeIdException();

        String response = handler.handleObrigatoriedadeIdException(ex);

        assertThat(response).isEqualTo("O 'id' é obrigatório constar no corpo da requisição para alteração.");
    }

    @Test
    void handleEntidadeEmUsoException_deveRetornarConflictComMensagem() {
        EntidadeEmUsoException ex = new EntidadeEmUsoException("Entidade em uso");

        ResponseEntity<String> response = handler.handleEntidadeEmUsoException(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody()).isEqualTo("Entidade em uso");
    }

    private enum TestEnum {
        OPTION1,
        OPTION2
    }

}