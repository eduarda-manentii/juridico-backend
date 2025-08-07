package br.com.attus.gerenciamentoprocessos.mapper;

import static org.junit.jupiter.api.Assertions.*;

import br.com.attus.gerenciamentoprocessos.dto.AndamentoProcessualDto;
import br.com.attus.gerenciamentoprocessos.model.AndamentoProcessual;
import br.com.attus.gerenciamentoprocessos.model.enums.TipoAndamentoProcessual;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

class AndamentoProcessualMapperTest {

    private AndamentoProcessualMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new AndamentoProcessualMapper();
    }

    @Nested
    class Dado_um_andamento_processual {

        AndamentoProcessual entity;

        @BeforeEach
        void setUp() {
            entity = AndamentoProcessual.builder()
                    .id(1L)
                    .tipoAndamentoProcessual(TipoAndamentoProcessual.AUDIENCIA)
                    .dataRegistro(LocalDate.of(2025, 8, 6))
                    .descricao("Descrição do andamento")
                    .build();
        }

        @Nested
        class Quando_converter_para_dto {

            AndamentoProcessualDto dto;

            @BeforeEach
            void setUp() {
                dto = mapper.toDto(entity);
            }

            @Test
            void Entao_deve_converter_corretamente() {
                assertNotNull(dto);
                assertEquals(entity.getId(), dto.getId());
                assertEquals(entity.getTipoAndamentoProcessual(), dto.getTipoAndamentoProcessual());
                assertEquals(entity.getDataRegistro(), dto.getDataRegistro());
                assertEquals(entity.getDescricao(), dto.getDescricao());
            }
        }
    }

    @Nested
    class Dado_um_andamento_processual_dto {

        AndamentoProcessualDto dto;

        @BeforeEach
        void setUp() {
            dto = AndamentoProcessualDto.builder()
                    .id(2L)
                    .tipoAndamentoProcessual(TipoAndamentoProcessual.AUDIENCIA)
                    .dataRegistro(LocalDate.of(2025, 8, 5))
                    .descricao("Descrição do despacho")
                    .build();
        }

        @Nested
        class Quando_converter_para_entity {

            AndamentoProcessual entity;

            @BeforeEach
            void setUp() {
                entity = mapper.toEntity(dto);
            }

            @Test
            void Entao_deve_converter_corretamente() {
                assertNotNull(entity);
                assertEquals(dto.getId(), entity.getId());
                assertEquals(dto.getTipoAndamentoProcessual(), entity.getTipoAndamentoProcessual());
                assertEquals(dto.getDataRegistro(), entity.getDataRegistro());
                assertEquals(dto.getDescricao(), entity.getDescricao());
            }
        }
    }

}
