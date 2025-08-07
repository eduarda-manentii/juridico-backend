package br.com.attus.gerenciamentoprocessos.mapper;

import br.com.attus.gerenciamentoprocessos.Mocker;
import br.com.attus.gerenciamentoprocessos.dto.ParteEnvolvidaDocumentoDto;
import br.com.attus.gerenciamentoprocessos.dto.ParteEnvolvidaDto;
import br.com.attus.gerenciamentoprocessos.model.ParteEnvolvida;
import br.com.attus.gerenciamentoprocessos.model.ParteEnvolvidaDocumento;
import br.com.attus.gerenciamentoprocessos.model.enums.TipoDocumento;
import br.com.attus.gerenciamentoprocessos.model.enums.TipoParteEnvolvida;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ParteEnvolvidaMapperTest {

    private ParteEnvolvidaMapper mapper;
    private Mocker mocker;

    @BeforeEach
    void setUp() {
        mapper = new ParteEnvolvidaMapper(new ParteEnvolvidaDocumentoMapper());
        mocker = new Mocker();
    }

    @Nested
    class Dada_uma_parte_envolvida {

        ParteEnvolvida entity;

        @BeforeEach
        void setUp() {
            ParteEnvolvidaDocumento documento = ParteEnvolvidaDocumento.builder()
                    .id(1L)
                    .tipoDocumento(TipoDocumento.CPF)
                    .valor("12345678901")
                    .build();

            entity = ParteEnvolvida.builder()
                    .id(1L)
                    .nomeCompleto("Maria")
                    .documento(documento)
                    .tipoParteEnvolvida(TipoParteEnvolvida.AUTOR)
                    .email("maria@email.com")
                    .telefone("11999999999")
                    .build();
        }

        @Nested
        class Quando_converter_para_dto {

            ParteEnvolvidaDto dto;

            @BeforeEach
            void setUp() {
                dto = mapper.toDto(entity);
            }

            @Test
            void Entao_deve_converter_corretamente() {
                assertEquals(entity.getId(), dto.getId());
                assertEquals(entity.getNomeCompleto(), dto.getNomeCompleto());
                assertEquals(entity.getEmail(), dto.getEmail());
                assertEquals(entity.getTelefone(), dto.getTelefone());
                assertEquals(entity.getTipoParteEnvolvida(), dto.getTipoParteEnvolvida());
                assertNotNull(dto.getDocumento());
                assertEquals(entity.getDocumento().getValor(), dto.getDocumento().getValor());
            }
        }
    }

    @Nested
    class Dado_um_dto_parte_envolvida {

        ParteEnvolvidaDto dto;

        @BeforeEach
        void setUp() {
            ParteEnvolvidaDocumentoDto documentoDto = ParteEnvolvidaDocumentoDto.builder()
                    .id(2L)
                    .tipoDocumento(TipoDocumento.CNPJ)
                    .valor("12345678000199")
                    .build();

            dto = ParteEnvolvidaDto.builder()
                    .id(2L)
                    .nomeCompleto("Empresa XYZ")
                    .documento(documentoDto)
                    .tipoParteEnvolvida(TipoParteEnvolvida.REU)
                    .email("empresa@xyz.com")
                    .telefone("1133334444")
                    .build();
        }

        @Nested
        class Quando_converter_para_entity {

            ParteEnvolvida entity;

            @BeforeEach
            void setUp() {
                entity = mapper.toEntity(dto);
            }

            @Test
            void Entao_deve_converter_corretamente() {
                assertEquals(dto.getId(), entity.getId());
                assertEquals(dto.getNomeCompleto(), entity.getNomeCompleto());
                assertEquals(dto.getEmail(), entity.getEmail());
                assertEquals(dto.getTelefone(), entity.getTelefone());
                assertEquals(dto.getTipoParteEnvolvida(), entity.getTipoParteEnvolvida());
                assertNotNull(entity.getDocumento());
                assertEquals(dto.getDocumento().getValor(), entity.getDocumento().getValor());
            }
        }
    }

}
