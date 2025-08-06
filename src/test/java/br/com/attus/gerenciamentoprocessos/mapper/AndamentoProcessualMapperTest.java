package br.com.attus.gerenciamentoprocessos.mapper;

import static org.junit.jupiter.api.Assertions.*;

import br.com.attus.gerenciamentoprocessos.dto.AndamentoProcessualDto;
import br.com.attus.gerenciamentoprocessos.model.AndamentoProcessual;
import br.com.attus.gerenciamentoprocessos.model.enums.TipoAndamentoProcessual;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;


class AndamentoProcessualMapperTest {

    private final AndamentoProcessualMapper mapper = new AndamentoProcessualMapper();

    @Test
    void testToDto() {
        AndamentoProcessual entity = AndamentoProcessual.builder()
                .id(1L)
                .tipoAndamentoProcessual(TipoAndamentoProcessual.AUDIENCIA)
                .dataRegistro(LocalDate.of(2025, 8, 6))
                .descricao("Descrição do andamento")
                .build();

        AndamentoProcessualDto dto = mapper.toDto(entity);

        assertNotNull(dto);
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getTipoAndamentoProcessual(), dto.getTipoAndamentoProcessual());
        assertEquals(entity.getDataRegistro(), dto.getDataRegistro());
        assertEquals(entity.getDescricao(), dto.getDescricao());
    }

    @Test
    void testToEntity() {
        AndamentoProcessualDto dto = AndamentoProcessualDto.builder()
                .id(2L)
                .tipoAndamentoProcessual(TipoAndamentoProcessual.AUDIENCIA)
                .dataRegistro(LocalDate.of(2025, 8, 5))
                .descricao("Descrição do despacho")
                .build();

        AndamentoProcessual entity = mapper.toEntity(dto);

        assertNotNull(entity);
        assertEquals(dto.getId(), entity.getId());
        assertEquals(dto.getTipoAndamentoProcessual(), entity.getTipoAndamentoProcessual());
        assertEquals(dto.getDataRegistro(), entity.getDataRegistro());
        assertEquals(dto.getDescricao(), entity.getDescricao());
    }

}