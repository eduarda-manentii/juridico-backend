package br.com.attus.gerenciamentoprocessos.exceptions.handler;


import br.com.attus.gerenciamentoprocessos.exceptions.*;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
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

    @Nested
    class HandleTokenInvalidoException {

        @Test
        void deve_Retornar_Unauthorized() {
            TokenInvalidoException ex = new TokenInvalidoException();

            ResponseEntity<String> response = handler.handleTokenInvalidoException(ex);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
            assertThat(response.getBody()).isEqualTo("Token inválido ou expirado.");
        }
    }

    @Nested
    class HandleDuplicidadeDocumentoException {

        @Test
        void deve_Retornar_Mensagem_Com_Conflict() {
            DuplicidadeDocumentoException ex = new DuplicidadeDocumentoException("Duplicidade");

            String response = handler.handleDuplicidadeDocumentoException(ex);

            assertThat(response).isEqualTo("Documento já existente.");
        }
    }

    @Nested
    class HandleValidationExceptions {

        @Test
        void deve_Retornar_Mapa_De_Erros_Com_BadRequest() {
            MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
            BindingResult bindingResult = mock(BindingResult.class);
            FieldError fieldError = new FieldError("obj", "campo", "erro");

            when(ex.getBindingResult()).thenReturn(bindingResult);
            when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));

            ResponseEntity<Map<String, String>> response = handler.handleValidationExceptions(ex);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            assertThat(response.getBody()).containsEntry("campo", "erro");
        }
    }

    @Nested
    class HandleHttpMessageNotReadableException {

        @Test
        void deve_Com_Outra_Excecao_Retornar_Mensagem_Padrao() {
            HttpMessageNotReadableException ex = mock(HttpMessageNotReadableException.class);

            when(ex.getCause()).thenReturn(new RuntimeException());

            ResponseEntity<String> response = handler.handleHttpMessageNotReadableException(ex);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            assertThat(response.getBody()).isEqualTo("Requisição mal formada.");
        }
    }

    @Nested
    class HandleEntityNotFoundException {

        @Test
        void deve_Retornar_Mapa_Com_Mensagem() {
            EntityNotFoundException ex = new EntityNotFoundException("Não encontrado");

            Map<String, String> response = handler.handleEntityNotFoundException(ex);

            assertThat(response).containsEntry("mensagem", "Não encontrado");
        }
    }

    @Nested
    class HandleIllegalArgumentException {

        @Test
        void deve_Retornar_BadRequest_Com_Mensagem() {
            IllegalArgumentException ex = new IllegalArgumentException("Argumento inválido");

            ResponseEntity<Map<String, String>> response = handler.handleIllegalArgumentException(ex);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            assertThat(response.getBody()).containsEntry("mensagem", "Argumento inválido");
        }
    }

    @Nested
    class HandleObrigatoriedadeIdException {

        @Test
        void deve_Retornar_Mensagem() {
            ObrigatoriedadeIdException ex = new ObrigatoriedadeIdException();

            String response = handler.handleObrigatoriedadeIdException(ex);

            assertThat(response).isEqualTo("O 'id' é obrigatório constar no corpo da requisição para alteração.");
        }
    }

    @Nested
    class HandleEntidadeEmUsoException {

        @Test
        void deve_Retornar_Conflict_Com_Mensagem() {
            EntidadeEmUsoException ex = new EntidadeEmUsoException("Entidade em uso");

            ResponseEntity<String> response = handler.handleEntidadeEmUsoException(ex);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
            assertThat(response.getBody()).isEqualTo("Entidade em uso");
        }
    }
}
