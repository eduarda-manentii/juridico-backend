package br.com.attus.gerenciamentoprocessos.service.impl;

import br.com.attus.gerenciamentoprocessos.Mocker;
import br.com.attus.gerenciamentoprocessos.exceptions.EntidadeEmUsoException;
import br.com.attus.gerenciamentoprocessos.model.Usuario;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class UsuarioServiceImplTest {

    @Autowired
    private UsuarioServiceImpl usuarioService;

    @Autowired
    private Mocker mocker;

    @Nested
    class Dado_um_usuario {

        Usuario usuario;

        @BeforeEach
        void setUp() {
            usuario = mocker.gerarUsuario(null);
        }

        @Nested
        class Quando_usuario_novo {

            Usuario usuarioSalvo;

            @BeforeEach
            void setUp() {
                usuarioSalvo = usuarioService.salvar(usuario);
            }

            @Test
            void Entao_deve_salvar_corretamente() {
                assertNotNull(usuarioSalvo.getId());
                assertEquals(usuario.getNome(), usuarioSalvo.getNome());
                assertEquals(usuario.getEmail(), usuarioSalvo.getEmail());
                assertEquals(usuario.getSenha(), usuarioSalvo.getSenha());
            }
        }

        @Nested
        class Quando_existe_usuario_salvo {

            Usuario usuarioSalvo;

            @BeforeEach
            void setUp() {
                usuarioSalvo = usuarioService.salvar(usuario);
            }

            @Nested
            class Quando_email_igual_e_id_diferente {

                Usuario usuarioNovo;

                @BeforeEach
                void setUp() {
                    usuarioNovo = mocker.gerarUsuario(null);
                }

                @Test
                void Entao_deve_lancar_excecao() {
                    assertThrows(EntidadeEmUsoException.class,
                            () -> usuarioService.salvar(usuarioNovo)
                    );
                }
            }

            @Nested
            class Quando_email_diferente {

                Usuario usuarioNovo;

                @BeforeEach
                void setUp() {
                    usuarioNovo = mocker.gerarUsuario(null);
                    usuarioNovo.setEmail("emailDiferente@gmail.com");
                    usuarioNovo = usuarioService.salvar(usuarioNovo);
                }

                @Test
                void Entao_deve_salvar_com_email_diferente() {
                    assertNotNull(usuarioNovo.getId());
                    assertNotEquals(usuarioSalvo.getId(), usuarioNovo.getId());
                    assertEquals("emailDiferente@gmail.com", usuarioNovo.getEmail());
                }
            }
        }
    }
}
