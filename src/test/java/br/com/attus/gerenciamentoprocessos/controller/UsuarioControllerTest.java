package br.com.attus.gerenciamentoprocessos.controller;

import br.com.attus.gerenciamentoprocessos.dto.LoginRequestDto;
import br.com.attus.gerenciamentoprocessos.dto.ResponseDto;
import br.com.attus.gerenciamentoprocessos.dto.UsuarioDto;
import br.com.attus.gerenciamentoprocessos.exceptions.ObrigatoriedadeIdException;
import br.com.attus.gerenciamentoprocessos.mapper.UsuarioMapper;
import br.com.attus.gerenciamentoprocessos.model.Usuario;
import br.com.attus.gerenciamentoprocessos.security.TokenService;
import br.com.attus.gerenciamentoprocessos.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioControllerUnitTest {

    @InjectMocks
    private UsuarioController controller;

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private TokenService tokenService;

    @Mock
    private UsuarioMapper usuarioMapper;

    @Test
    void deveInserirUsuarioComSucesso() {
        UsuarioDto dto = new UsuarioDto();
        dto.setEmail("teste@email.com");
        dto.setSenha("123");
        dto.setNome("Eduarda");

        Usuario novoUsuario = new Usuario();
        novoUsuario.setId(1L);
        novoUsuario.setEmail(dto.getEmail());
        novoUsuario.setNome(dto.getNome());

        when(usuarioService.buscarPorEmail(dto.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(dto.getSenha())).thenReturn("senhaCriptografada");
        when(usuarioService.salvar(any(Usuario.class))).thenReturn(novoUsuario);
        when(tokenService.generateToken(any(Usuario.class))).thenReturn("meu-token-jwt");
        ResponseEntity<ResponseDto> response = controller.inserir(dto);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Eduarda", response.getBody().getEmail());
        assertEquals("meu-token-jwt", response.getBody().getToken());
    }

    @Test
    void deveRealizarLoginComSucesso() {
        LoginRequestDto login = new LoginRequestDto("teste@email.com", "123");

        Usuario usuario = new Usuario();
        usuario.setNome("Eduarda");
        usuario.setSenha("senhaCriptografada");

        when(usuarioService.buscarPorEmail(login.getEmail())).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches(login.getSenha(), usuario.getSenha())).thenReturn(true);
        when(tokenService.generateToken(usuario)).thenReturn("token-gerado");

        ResponseEntity<ResponseDto> response = controller.login(login);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Eduarda", response.getBody().getEmail());
        assertEquals("token-gerado", response.getBody().getToken());
    }

    @Test
    void deveFalharLoginSenhaIncorreta() {
        LoginRequestDto login = new LoginRequestDto("teste@email.com", "senhaErrada");

        Usuario usuario = new Usuario();
        usuario.setSenha("senhaCriptografada");

        when(usuarioService.buscarPorEmail(login.getEmail())).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches(login.getSenha(), usuario.getSenha())).thenReturn(false);

        ResponseEntity<ResponseDto> response = controller.login(login);

        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void deveBuscarUsuarioPorId() {
        Long id = 1L;

        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setNome("Eduarda");
        usuario.setEmail("teste@email.com");

        UsuarioDto dto = new UsuarioDto();
        dto.setId(id);
        dto.setNome("Eduarda");
        dto.setEmail("teste@email.com");

        when(usuarioService.buscarPorId(id)).thenReturn(usuario);
        when(usuarioMapper.toDto(usuario)).thenReturn(dto);

        ResponseEntity<UsuarioDto> response = controller.buscarPorId(id);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Eduarda", response.getBody().getNome());
    }

    @Test
    void deveAlterarUsuarioComSucesso() {
        UsuarioDto dto = new UsuarioDto();
        dto.setId(1L);
        dto.setNome("Eduarda Atualizada");

        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("Eduarda Atualizada");

        when(usuarioMapper.toEntity(dto)).thenReturn(usuario);
        when(usuarioService.salvar(usuario)).thenReturn(usuario);
        when(usuarioMapper.toDto(usuario)).thenReturn(dto);

        ResponseEntity<UsuarioDto> response = controller.alterar(dto);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Eduarda Atualizada", response.getBody().getNome());
    }

    @Test
    void deveLancarExcecaoQuandoAlterarSemId() {
        UsuarioDto dto = new UsuarioDto();
        dto.setNome("Sem ID");
        assertThrows(ObrigatoriedadeIdException.class, () -> controller.alterar(dto));
    }

    @Test
    void deveExcluirUsuarioComSucesso() {
        Long id = 1L;
        ResponseEntity<Void> response = controller.excluir(id);
        verify(usuarioService).excluir(id);
        assertEquals(204, response.getStatusCodeValue());
    }

}