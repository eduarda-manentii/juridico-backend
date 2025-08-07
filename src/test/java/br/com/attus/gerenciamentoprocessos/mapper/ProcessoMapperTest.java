package br.com.attus.gerenciamentoprocessos.mapper;

import static org.junit.jupiter.api.Assertions.*;


import br.com.attus.gerenciamentoprocessos.dto.AndamentoProcessualDto;
import br.com.attus.gerenciamentoprocessos.dto.ParteEnvolvidaDto;
import br.com.attus.gerenciamentoprocessos.dto.ProcessoDto;
import br.com.attus.gerenciamentoprocessos.model.AndamentoProcessual;
import br.com.attus.gerenciamentoprocessos.model.ParteEnvolvida;
import br.com.attus.gerenciamentoprocessos.model.Processo;
import br.com.attus.gerenciamentoprocessos.model.enums.StatusProcesso;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;

class ProcessoMapperTest {

    private ParteEnvolvidaMapper parteEnvolvidaMapper;
    private AndamentoProcessualMapper andamentoProcessualMapper;
    private ProcessoMapper processoMapper;

    @BeforeEach
    void setUp() {
        parteEnvolvidaMapper = mock(ParteEnvolvidaMapper.class);
        andamentoProcessualMapper = mock(AndamentoProcessualMapper.class);
        processoMapper = new ProcessoMapper(parteEnvolvidaMapper, andamentoProcessualMapper);
    }



    @Test
    void testToDto() {
        ParteEnvolvida parte1 = new ParteEnvolvida();
        parte1.setId(1L);
        ParteEnvolvida parte2 = new ParteEnvolvida();
        parte2.setId(2L);

        ParteEnvolvidaDto dto1 = ParteEnvolvidaDto.builder().id(1L).build();
        ParteEnvolvidaDto dto2 = ParteEnvolvidaDto.builder().id(2L).build();

        when(parteEnvolvidaMapper.toDto(parte1)).thenReturn(dto1);
        when(parteEnvolvidaMapper.toDto(parte2)).thenReturn(dto2);

        AndamentoProcessual andamento = new AndamentoProcessual();
        andamento.setId(10L);
        AndamentoProcessualDto andamentoDto = AndamentoProcessualDto.builder().id(10L).build();

        when(andamentoProcessualMapper.toDto(andamento)).thenReturn(andamentoDto);

        Processo processo = Processo.builder()
                .id(100L)
                .dataAbertura(LocalDate.of(2025, 8, 6))
                .descricaoCaso("Caso de teste")
                .partesEnvolvidas(List.of(parte1, parte2))
                .andamentoProcessual(andamento)
                .status(StatusProcesso.ATIVO)
                .build();

        ProcessoDto dto = processoMapper.toDto(processo);

        assertNotNull(dto);
        assertEquals(processo.getId(), dto.getId());
        assertEquals(processo.getDataAbertura(), dto.getDataAbertura());
        assertEquals(processo.getDescricaoCaso(), dto.getDescricaoCaso());
        assertEquals(StatusProcesso.ATIVO, dto.getStatus());
        assertEquals(2, dto.getPartesEnvolvidas().size());

        assertEquals(dto2, dto.getPartesEnvolvidas().get(0));
        assertEquals(dto1, dto.getPartesEnvolvidas().get(1));

        assertEquals(andamentoDto, dto.getAndamentoProcessual());

        verify(parteEnvolvidaMapper).toDto(parte1);
        verify(parteEnvolvidaMapper).toDto(parte2);
        verify(andamentoProcessualMapper).toDto(andamento);
    }

    @Test
    void testToEntity() {
        ParteEnvolvidaDto dto1 = ParteEnvolvidaDto.builder().id(1L).build();
        ParteEnvolvidaDto dto2 = ParteEnvolvidaDto.builder().id(2L).build();

        ParteEnvolvida parte1 = new ParteEnvolvida();
        parte1.setId(1L);
        ParteEnvolvida parte2 = new ParteEnvolvida();
        parte2.setId(2L);

        when(parteEnvolvidaMapper.toEntity(dto1)).thenReturn(parte1);
        when(parteEnvolvidaMapper.toEntity(dto2)).thenReturn(parte2);

        AndamentoProcessualDto andamentoDto = AndamentoProcessualDto.builder().id(10L).build();
        AndamentoProcessual andamento = new AndamentoProcessual();
        andamento.setId(10L);

        when(andamentoProcessualMapper.toEntity(andamentoDto)).thenReturn(andamento);

        ProcessoDto processoDto = ProcessoDto.builder()
                .id(200L)
                .dataAbertura(LocalDate.of(2025, 8, 7))
                .descricaoCaso("Outro caso")
                .partesEnvolvidas(List.of(dto1, dto2))
                .andamentoProcessual(andamentoDto)
                .status(StatusProcesso.ARQUIVADO)
                .build();

        Processo entity = processoMapper.toEntity(processoDto);

        assertNotNull(entity);
        assertEquals(processoDto.getId(), entity.getId());
        assertEquals(processoDto.getDataAbertura(), entity.getDataAbertura());
        assertEquals(processoDto.getDescricaoCaso(), entity.getDescricaoCaso());
        assertEquals(StatusProcesso.ARQUIVADO, entity.getStatus());
        assertEquals(2, entity.getPartesEnvolvidas().size());

        assertEquals(parte1, entity.getPartesEnvolvidas().get(0));
        assertEquals(parte2, entity.getPartesEnvolvidas().get(1));

        assertEquals(andamento, entity.getAndamentoProcessual());

        verify(parteEnvolvidaMapper).toEntity(dto1);
        verify(parteEnvolvidaMapper).toEntity(dto2);
        verify(andamentoProcessualMapper).toEntity(andamentoDto);
    }

}