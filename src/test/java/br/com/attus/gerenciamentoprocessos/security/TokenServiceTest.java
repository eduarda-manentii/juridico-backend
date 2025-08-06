package br.com.attus.gerenciamentoprocessos.security;


import br.com.attus.gerenciamentoprocessos.exceptions.TokenInvalidoException;
import br.com.attus.gerenciamentoprocessos.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.*;

class TokenServiceTest {

    private TokenService tokenService;

    @BeforeEach
    void setUp() {
        tokenService = new TokenService();
        ReflectionTestUtils.setField(tokenService, "secret", "mysecret1234567890");
    }

    @Test
    void deveGerarTokenValido() {
        Usuario usuario = new Usuario();
        usuario.setEmail("teste@example.com");

        String token = tokenService.generateToken(usuario);

        assertThat(token).isNotNull().isNotEmpty();
    }

    @Test
    void deveValidarTokenValido() {
        Usuario usuario = new Usuario();
        usuario.setEmail("teste@example.com");
        String token = tokenService.generateToken(usuario);

        String email = tokenService.validateToken(token);

        assertThat(email).isEqualTo("teste@example.com");
    }

    @Test
    void deveLancarExceptionParaTokenInvalido() {
        String tokenInvalido = "token-invalido";

        assertThatThrownBy(() -> tokenService.validateToken(tokenInvalido))
                .isInstanceOf(TokenInvalidoException.class);
    }

}