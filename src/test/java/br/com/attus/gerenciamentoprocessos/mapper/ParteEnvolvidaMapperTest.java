package br.com.attus.gerenciamentoprocessos.mapper;

import br.com.attus.gerenciamentoprocessos.dto.ParteEnvolvidaDocumentoDto;
import br.com.attus.gerenciamentoprocessos.dto.ParteEnvolvidaDto;
import br.com.attus.gerenciamentoprocessos.model.ParteEnvolvida;
import br.com.attus.gerenciamentoprocessos.model.ParteEnvolvidaDocumento;
import br.com.attus.gerenciamentoprocessos.model.enums.TipoDocumento;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = ParteEnvolvidaMapper.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ParteEnvolvidaMapperTest {

    @Autowired
    private ParteEnvolvidaMapper parteEnvolvidaMapper;

    @Nested
    class Dado_uma_parte_envolvida {

        ParteEnvolvida parteEnvolvida;

        @BeforeEach
        void setUp() {
            parteEnvolvida = ParteEnvolvida.builder()
                    .id(1L)
                    .email("duda@gmail.com")
                    .telefone("00000")
                    .nomeCompleto("duda")
                    .documento(ParteEnvolvidaDocumento.builder()
                            .tipoDocumento(TipoDocumento.CPF)
                            .valor("000.000.000-00")
                            .build())
                    .build();
        }

        @Nested
        class Quando_converter_para_dto {

            ParteEnvolvidaDto parteEnvolvidaDto;

            @BeforeEach
            void setUp() {
                parteEnvolvidaDto = parteEnvolvidaMapper.toDto(parteEnvolvida);
            }

            @Test
            void Entao_deve_retornar_corretamente_o_dto() {
                assertEquals(parteEnvolvida.getId(), parteEnvolvidaDto.getId());
                assertEquals(parteEnvolvida.getEmail(), parteEnvolvidaDto.getEmail());
                assertEquals(parteEnvolvida.getNomeCompleto(), parteEnvolvidaDto.getNomeCompleto());
            }

        }

    }

    @Nested
    class Dado_uma_parte_envolvida_dto {

        ParteEnvolvidaDto parteEnvolvidaDto;

        @BeforeEach
        void setUp() {
            parteEnvolvidaDto = ParteEnvolvidaDto.builder()
                    .id(1L)
                    .email("duda@gmail.com")
                    .telefone("00000")
                    .nomeCompleto("duda")
                    .documento(ParteEnvolvidaDocumentoDto.builder()
                            .tipoDocumento(TipoDocumento.CPF)
                            .valor("000.000.000-00")
                            .build())
                    .build();
        }
        @Nested
        class Quando_converter_para_entity {

            ParteEnvolvida parteEnvolvida;

            @BeforeEach
            void setUp() {
                parteEnvolvida = parteEnvolvidaMapper.toEntity(parteEnvolvidaDto);
            }

            @Test
            void Entao_deve_retornar_corretamente_o_dto() {
                assertEquals(parteEnvolvida.getId(), parteEnvolvidaDto.getId());
                assertEquals(parteEnvolvida.getEmail(), parteEnvolvidaDto.getEmail());
                assertEquals(parteEnvolvida.getNomeCompleto(), parteEnvolvidaDto.getNomeCompleto());
            }

        }

    }

}