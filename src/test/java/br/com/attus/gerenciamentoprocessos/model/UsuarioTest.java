package br.com.attus.gerenciamentoprocessos.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioTest {

    private Usuario usuario1;
    private Usuario usuario2;

    @Nested
    class Dado_um_usuario_builder_e_um_usuario_setters {

        @BeforeEach
        void setUp() {
            usuario1 = Usuario.builder()
                    .id(1L)
                    .nome("Eduarda")
                    .email("eduarda@email.com")
                    .senha("senha123")
                    .build();

            usuario2 = new Usuario();
            usuario2.setId(1L);
            usuario2.setNome("Eduarda");
            usuario2.setEmail("eduarda@email.com");
            usuario2.setSenha("senha123");
        }

        @Test
        void Entao_equals_e_hashcode_devem_ser_iguais() {
            assertEquals(usuario1, usuario2);
            assertEquals(usuario1.hashCode(), usuario2.hashCode());
        }

        @Test
        void Entao_toString_deve_conter_nome() {
            assertTrue(usuario1.toString().contains("Eduarda"));
            assertTrue(usuario2.toString().contains("Eduarda"));
        }

        @Test
        void Entao_getters_devem_retornar_valores() {
            assertEquals(1L, usuario1.getId());
            assertEquals("Eduarda", usuario1.getNome());
            assertEquals("eduarda@email.com", usuario1.getEmail());
            assertEquals("senha123", usuario1.getSenha());
        }
    }
}
