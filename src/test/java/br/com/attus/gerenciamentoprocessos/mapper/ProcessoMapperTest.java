package br.com.attus.gerenciamentoprocessos.mapper;

import br.com.attus.gerenciamentoprocessos.Mocker;
import br.com.attus.gerenciamentoprocessos.dto.ProcessoDto;
import br.com.attus.gerenciamentoprocessos.model.Processo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
class ProcessoMapperTest {

    @Autowired
    private Mocker mocker;

    @Autowired
    private ProcessoMapper processoMapper;

    private ParteEnvolvidaMapper parteEnvolvidaMapper;

    private AndamentoProcessualMapper andamentoProcessualMapper;

    @BeforeEach
    void setUp() {
        parteEnvolvidaMapper = mock(ParteEnvolvidaMapper.class);
        andamentoProcessualMapper = mock(AndamentoProcessualMapper.class);
        when(parteEnvolvidaMapper.toDto(any())).thenAnswer(invocation -> {
            return mocker.gerarParteEnvolvidaDto(1L);
        });
        when(andamentoProcessualMapper.toDto(any())).thenAnswer(invocation -> {
            return mocker.gerarAndamentoProcessualDto(1L);
        });
        when(parteEnvolvidaMapper.toEntity(any())).thenAnswer(invocation -> {
            return mocker.gerarParteEnvolvida(1L);
        });
        when(andamentoProcessualMapper.toEntity(any())).thenAnswer(invocation -> {
            return mocker.gerarAndamentoProcessual(1L);
        });
        processoMapper = new ProcessoMapper(parteEnvolvidaMapper, andamentoProcessualMapper);
    }

    @Nested
    class Dado_um_processo {

        Processo processo;

        @BeforeEach
        void setUp() {
            processo = mocker.gerarProcesso(1L);
        }

        @Nested
        class Quando_converter_para_dto {

            ProcessoDto processoDto;

            @BeforeEach
            void setUp() {
                processoDto = processoMapper.toDto(processo);
            }

            @Test
            void Entao_deve_converter_corretamente() {
                assertEquals(processo.getId(), processoDto.getId());
                assertEquals(processo.getDataAbertura(), processoDto.getDataAbertura());
                assertEquals(processo.getDescricaoCaso(), processoDto.getDescricaoCaso());
                assertEquals(processo.getStatus(), processoDto.getStatus());
                assertNotNull(processoDto.getPartesEnvolvidas());
                assertNotNull(processoDto.getAndamentoProcessual());
            }
        }
    }

    @Nested
    class Dado_um_processo_dto {

        ProcessoDto processoDto;

        @BeforeEach
        void setUp() {
            processoDto = mocker.gerarProcessoDto(2L);
        }

        @Nested
        class Quando_converter_para_entity {

            Processo processo;

            @BeforeEach
            void setUp() {
                processo = processoMapper.toEntity(processoDto);
            }

            @Test
            void Entao_deve_converter_corretamente() {
                assertEquals(processoDto.getId(), processo.getId());
                assertEquals(processoDto.getDataAbertura(), processo.getDataAbertura());
                assertEquals(processoDto.getDescricaoCaso(), processo.getDescricaoCaso());
                assertEquals(processoDto.getStatus(), processo.getStatus());
                assertNotNull(processo.getPartesEnvolvidas());
                assertNotNull(processo.getAndamentoProcessual());
            }
        }
    }
}
