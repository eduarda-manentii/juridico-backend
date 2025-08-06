package br.com.attus.gerenciamentoprocessos.mapper;

import static org.junit.jupiter.api.Assertions.*;

import br.com.attus.gerenciamentoprocessos.dto.UsuarioDto;
import br.com.attus.gerenciamentoprocessos.model.Usuario;
import org.junit.jupiter.api.Test;

class UsuarioMapperTest {

    private final UsuarioMapper mapper = new UsuarioMapper();

    @Test
    void testToDto() {
        Usuario usuario = Usuario.builder()
                .id(1L)
                .nome("Eduarda")
                .email("eduarda@example.com")
                .senha("123456")
                .build();

        UsuarioDto dto = mapper.toDto(usuario);

        assertNotNull(dto);
        assertEquals(usuario.getId(), dto.getId());
        assertEquals(usuario.getNome(), dto.getNome());
        assertEquals(usuario.getEmail(), dto.getEmail());
        assertEquals(usuario.getSenha(), dto.getSenha());
    }

    @Test
    void testToEntity() {
        UsuarioDto dto = UsuarioDto.builder()
                .id(2L)
                .nome("Kauan")
                .email("kauan@example.com")
                .senha("abcdef")
                .build();

        Usuario usuario = mapper.toEntity(dto);

        assertNotNull(usuario);
        assertEquals(dto.getId(), usuario.getId());
        assertEquals(dto.getNome(), usuario.getNome());
        assertEquals(dto.getEmail(), usuario.getEmail());
        assertEquals(dto.getSenha(), usuario.getSenha());
    }

}