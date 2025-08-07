package br.com.attus.gerenciamentoprocessos.mapper;

import static org.junit.jupiter.api.Assertions.*;

import br.com.attus.gerenciamentoprocessos.dto.ParteEnvolvidaDocumentoDto;
import br.com.attus.gerenciamentoprocessos.model.ParteEnvolvidaDocumento;
import br.com.attus.gerenciamentoprocessos.model.enums.TipoDocumento;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ParteEnvolvidaDocumentoMapperTest {

    private ParteEnvolvidaDocumentoMapper mapper;

    @BeforeEach
    void setup() {
        mapper = new ParteEnvolvidaDocumentoMapper();
    }

    @Nested
    class Dado_um_documento {

        ParteEnvolvidaDocumento entity;

        @BeforeEach
        void setUp() {
            entity = ParteEnvolvidaDocumento.builder()
                    .id(10L)
                    .tipoDocumento(TipoDocumento.CPF)
                    .valor("12345678900")
                    .build();
        }

        @Nested
        class Quando_converter_para_dto {

            ParteEnvolvidaDocumentoDto dto;

            @BeforeEach
            void setUp() {
                dto = mapper.toDto(entity);
            }

            @Test
            void Entao_deve_converter_corretamente() {
                assertNotNull(dto);
                assertEquals(entity.getId(), dto.getId());
                assertEquals(entity.getTipoDocumento(), dto.getTipoDocumento());
                assertEquals(entity.getValor(), dto.getValor());
            }
        }
    }

    @Nested
    class Dado_um_documento_dto {

        ParteEnvolvidaDocumentoDto dto;

        @BeforeEach
        void setUp() {
            dto = ParteEnvolvidaDocumentoDto.builder()
                    .id(20L)
                    .tipoDocumento(TipoDocumento.CNPJ)
                    .valor("12345678000199")
                    .build();
        }

        @Nested
        class Quando_converter_para_entity {

            ParteEnvolvidaDocumento entity;

            @BeforeEach
            void setUp() {
                entity = mapper.toEntity(dto);
            }

            @Test
            void Entao_deve_converter_corretamente() {
                assertNotNull(entity);
                assertEquals(dto.getId(), entity.getId());
                assertEquals(dto.getTipoDocumento(), entity.getTipoDocumento());
                assertEquals(dto.getValor(), entity.getValor());
            }
        }
    }
}
