package br.com.attus.gerenciamentoprocessos.controller;

import br.com.attus.gerenciamentoprocessos.Mocker;
import br.com.attus.gerenciamentoprocessos.dto.LoginRequestDto;
import br.com.attus.gerenciamentoprocessos.dto.ResponseDto;
import br.com.attus.gerenciamentoprocessos.mapper.UsuarioMapper;
import br.com.attus.gerenciamentoprocessos.model.Usuario;
import br.com.attus.gerenciamentoprocessos.security.TokenService;
import br.com.attus.gerenciamentoprocessos.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UsuarioControllerTest {

    @Autowired
    private Mocker mocker;

    @Autowired
    private UsuarioMapper mapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UsuarioService service;

    @Mock
    private TokenService tokenService;

    private UsuarioController usuarioController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        usuarioController = new UsuarioController(service, passwordEncoder, tokenService, mapper);
    }

    @Nested
    class Dado_um_usuario {

        Usuario usuario;

        @BeforeEach
        void setUp() {
            usuario = mocker.gerarUsuario(null);
        }

        @Nested
        class Quando_tenta_fazer_login {

            @Nested
            class Quando_as_credenciais_estao_corretas {

                ResponseDto response;
                HttpStatusCode status;
                LoginRequestDto loginRequest;

                @BeforeEach
                void setUp() {
                    loginRequest = new LoginRequestDto();
                    loginRequest.setEmail(usuario.getEmail());
                    loginRequest.setSenha("senhaCorreta");

                    when(service.buscarPorEmail(usuario.getEmail())).thenReturn(Optional.of(usuario));
                    when(passwordEncoder.matches(any(), any())).thenReturn(true);
                    when(tokenService.generateToken(usuario)).thenReturn("fake-jwt-token");

                    var result = usuarioController.login(loginRequest);
                    response = result.getBody();
                    status = result.getStatusCode();
                }

                @Test
                void Entao_deve_retornar_status_200_e_token() {
                    assertEquals(HttpStatusCode.valueOf(200), status);
                    assertNotNull(response);
                    assertEquals(usuario.getId(), response.getId());
                    assertEquals("fake-jwt-token", response.getToken());
                }
            }

            @Nested
            class Quando_as_credenciais_estao_incorretas {

                ResponseEntity<ResponseDto> response;

                @BeforeEach
                void setUp() {
                    LoginRequestDto loginRequest = new LoginRequestDto();
                    loginRequest.setEmail("email@invalido.com");
                    loginRequest.setSenha("senhaErrada");
                    when(service.buscarPorEmail("email@invalido.com")).thenReturn(Optional.empty());
                    response = usuarioController.login(loginRequest);
                }

                @Test
                void Entao_deve_retornar_status_400() {
                    assertEquals(400, response.getStatusCodeValue());
                    assertNull(response.getBody());
                }
            }
        }
    }
}
