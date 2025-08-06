package br.com.attus.gerenciamentoprocessos.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioTest {

    @Test
    void deveTestarEqualsHashCodeToStringBuilderESetters() {
        Usuario u1 = Usuario.builder()
                .id(1L)
                .nome("Eduarda")
                .email("eduarda@email.com")
                .senha("senha123")
                .build();

        Usuario u2 = new Usuario();
        u2.setId(1L);
        u2.setNome("Eduarda");
        u2.setEmail("eduarda@email.com");
        u2.setSenha("senha123");

        assertEquals(u1, u2);
        assertEquals(u1.hashCode(), u2.hashCode());
        assertTrue(u1.toString().contains("Eduarda"));
        assertTrue(u2.toString().contains("Eduarda"));
        assertEquals(1L, u1.getId());
        assertEquals("Eduarda", u1.getNome());
        assertEquals("eduarda@email.com", u1.getEmail());
        assertEquals("senha123", u1.getSenha());
    }

}
