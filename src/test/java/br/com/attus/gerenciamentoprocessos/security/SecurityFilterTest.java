package br.com.attus.gerenciamentoprocessos.security;

import static org.junit.jupiter.api.Assertions.*;


import br.com.attus.gerenciamentoprocessos.model.Usuario;
import br.com.attus.gerenciamentoprocessos.repository.UsuariosRepository;
import br.com.attus.gerenciamentoprocessos.exceptions.TokenInvalidoException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class SecurityFilterTest {

    private TokenService tokenService;
    private UsuariosRepository usuariosRepository;
    private SecurityFilter securityFilter;

    @BeforeEach
    void setUp() {
        tokenService = mock(TokenService.class);
        usuariosRepository = mock(UsuariosRepository.class);
        securityFilter = new SecurityFilter(tokenService, usuariosRepository);
        SecurityContextHolder.clearContext();
    }

    @Test
    void deveAutenticarUsuarioQuandoTokenValido() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        when(request.getHeader("Authorization")).thenReturn("Bearer token-valido");
        when(tokenService.validateToken("token-valido")).thenReturn("user@example.com");

        Usuario usuario = new Usuario();
        usuario.setEmail("user@example.com");
        when(usuariosRepository.findByEmail("user@example.com")).thenReturn(Optional.of(usuario));

        securityFilter.doFilterInternal(request, response, filterChain);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNotNull();
        assertThat(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).isEqualTo(usuario);
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void deveRetornar401QuandoTokenInvalido() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter responseWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(responseWriter);

        FilterChain filterChain = mock(FilterChain.class);

        when(request.getHeader("Authorization")).thenReturn("Bearer token-invalido");
        when(tokenService.validateToken("token-invalido")).thenThrow(new TokenInvalidoException());

        when(response.getWriter()).thenReturn(printWriter);

        securityFilter.doFilterInternal(request, response, filterChain);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(response).setContentType("application/json");
        printWriter.flush();
        assertThat(responseWriter.toString()).contains("Token inválido ou expirado.");
        verify(filterChain, never()).doFilter(request, response);
    }

    @Test
    void deveContinuarFiltroQuandoNaoTemToken() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        when(request.getHeader("Authorization")).thenReturn(null);

        securityFilter.doFilterInternal(request, response, filterChain);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        verify(filterChain).doFilter(request, response);
    }

}