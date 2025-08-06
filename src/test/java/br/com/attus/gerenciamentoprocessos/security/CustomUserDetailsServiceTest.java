package br.com.attus.gerenciamentoprocessos.security;

import br.com.attus.gerenciamentoprocessos.model.Usuario;
import br.com.attus.gerenciamentoprocessos.repository.UsuariosRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomUserDetailsServiceTest {

    private UsuariosRepository repository;
    private CustomUserDetailsService service;

    @BeforeEach
    void setUp() {
        repository = mock(UsuariosRepository.class);
        service = new CustomUserDetailsService(repository);
    }

    @Test
    void deveCarregarUsuarioExistente() {
        Usuario usuario = new Usuario();
        usuario.setEmail("user@example.com");
        usuario.setSenha("senha123");

        when(repository.findByEmail("user@example.com")).thenReturn(Optional.of(usuario));

        UserDetails userDetails = service.loadUserByUsername("user@example.com");

        assertThat(userDetails.getUsername()).isEqualTo("user@example.com");
        assertThat(userDetails.getPassword()).isEqualTo("senha123");
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoExistir() {
        when(repository.findByEmail("inexistente@example.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.loadUserByUsername("inexistente@example.com"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("User not found");
    }

}