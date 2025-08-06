package br.com.attus.gerenciamentoprocessos.service.impl;


import br.com.attus.gerenciamentoprocessos.exceptions.EntidadeEmUsoException;
import br.com.attus.gerenciamentoprocessos.model.Usuario;
import br.com.attus.gerenciamentoprocessos.repository.UsuariosRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsuarioServiceImplTest {

    @Mock
    private UsuariosRepository usuariosRepository;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        usuario = Usuario.builder()
                .id(1L)
                .nome("Eduarda")
                .email("eduarda@example.com")
                .senha("123456")
                .build();
    }

    @Test
    void deveSalvarUsuario() {
        when(usuariosRepository.save(any(Usuario.class))).thenReturn(usuario);

        Usuario resultado = usuarioService.salvar(usuario);

        assertNotNull(resultado);
        assertEquals(usuario.getId(), resultado.getId());
        verify(usuariosRepository).save(usuario);
    }

    @Test
    void deveBuscarUsuarioPorId_Sucesso() {
        when(usuariosRepository.findById(1L)).thenReturn(Optional.of(usuario));

        Usuario resultado = usuarioService.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals(usuario.getId(), resultado.getId());
    }

    @Test
    void deveLancarExcecaoAoBuscarUsuarioPorIdInexistente() {
        when(usuariosRepository.findById(99L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> usuarioService.buscarPorId(99L));

        assertEquals("Usuário não encontrado para o ID informado.", exception.getMessage());
    }

    @Test
    void deveExcluirUsuario() {
        doNothing().when(usuariosRepository).deleteById(1L);

        usuarioService.excluir(1L);

        verify(usuariosRepository).deleteById(1L);
    }

    @Test
    void deveBuscarUsuarioPorEmail_Sucesso() {
        when(usuariosRepository.findByEmail("eduarda@example.com")).thenReturn(Optional.of(usuario));

        Optional<Usuario> resultado = usuarioService.buscarPorEmail("eduarda@example.com");

        assertTrue(resultado.isPresent());
        assertEquals("eduarda@example.com", resultado.get().getEmail());
    }

    @Test
    void deveBuscarUsuarioPorEmail_NaoEncontrado() {
        when(usuariosRepository.findByEmail("inexistente@example.com")).thenReturn(Optional.empty());

        Optional<Usuario> resultado = usuarioService.buscarPorEmail("inexistente@example.com");

        assertFalse(resultado.isPresent());
    }

    @Test
    void deveLancarExcecaoAoSalvarUsuarioComNomeEEmailJaExistentes_emAlteracaoDeOutroUsuario() {
        Usuario usuario = Usuario.builder()
                .id(2L)
                .nome("João")
                .email("joao@email.com")
                .senha("novaSenha")
                .build();

        Usuario existente = Usuario.builder()
                .id(1L)
                .nome("João")
                .email("joao@email.com")
                .senha("senha")
                .build();

        when(usuariosRepository.findByNomeAndEmail("João", "joao@email.com"))
                .thenReturn(Optional.of(existente));
        assertThrows(EntidadeEmUsoException.class, () -> usuarioService.salvar(usuario));
        verify(usuariosRepository, never()).save(any());
    }

}