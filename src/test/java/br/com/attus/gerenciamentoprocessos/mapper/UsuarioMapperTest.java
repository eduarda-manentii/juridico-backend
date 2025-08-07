package br.com.attus.gerenciamentoprocessos.mapper;

import br.com.attus.gerenciamentoprocessos.Mocker;
import br.com.attus.gerenciamentoprocessos.dto.UsuarioDto;
import br.com.attus.gerenciamentoprocessos.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UsuarioMapperTest {

    private Mocker mocker;
    private UsuarioMapper usuarioMapper;

    @BeforeEach
    void setUp() {
        mocker = new Mocker();
        usuarioMapper = new UsuarioMapper();
    }

    @Nested
    class Dado_um_usuario {

        Usuario usuario;

        @BeforeEach
        void setUp() {
            usuario = mocker.gerarUsuario(1L);
        }

        @Nested
        class Quando_converter_para_dto {

            UsuarioDto usuarioDto;

            @BeforeEach
            void setUp() {
                usuarioDto = usuarioMapper.toDto(usuario);
            }

            @Test
            void Entao_deve_converter_corretamente() {
                assertEquals(usuario.getId(), usuarioDto.getId());
                assertEquals(usuario.getNome(), usuarioDto.getNome());
                assertEquals(usuario.getEmail(), usuarioDto.getEmail());
            }
        }
    }

    @Nested
    class Dado_um_usuario_dto {

        UsuarioDto usuarioDto;

        @BeforeEach
        void setUp() {
            usuarioDto = usuarioMapper.toDto(mocker.gerarUsuario(2L));
        }

        @Nested
        class Quando_converter_para_entity {

            Usuario usuario;

            @BeforeEach
            void setUp() {
                usuario = usuarioMapper.toEntity(usuarioDto);
            }

            @Test
            void Entao_deve_converter_corretamente() {
                assertEquals(usuarioDto.getId(), usuario.getId());
                assertEquals(usuarioDto.getNome(), usuario.getNome());
                assertEquals(usuarioDto.getEmail(), usuario.getEmail());
            }
        }
    }
}
