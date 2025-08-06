package br.com.attus.gerenciamentoprocessos.mapper;

import br.com.attus.gerenciamentoprocessos.dto.ParteEnvolvidaDocumentoDto;
import br.com.attus.gerenciamentoprocessos.dto.ParteEnvolvidaDto;
import br.com.attus.gerenciamentoprocessos.model.ParteEnvolvida;
import br.com.attus.gerenciamentoprocessos.model.ParteEnvolvidaDocumento;
import br.com.attus.gerenciamentoprocessos.model.enums.TipoDocumento;
import br.com.attus.gerenciamentoprocessos.model.enums.TipoParteEnvolvida;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ParteEnvolvidaMapperTest {

    private ParteEnvolvidaMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ParteEnvolvidaMapper(new ParteEnvolvidaDocumentoMapper());
    }

    @Test
    void testToDto() {
        ParteEnvolvidaDocumento documento = ParteEnvolvidaDocumento.builder()
                .id(1L)
                .tipoDocumento(TipoDocumento.CPF)
                .valor("12345678901")
                .build();

        ParteEnvolvida entity = ParteEnvolvida.builder()
                .id(1L)
                .nomeCompleto("Maria")
                .documento(documento)
                .tipoParteEnvolvida(TipoParteEnvolvida.AUTOR)
                .email("maria@email.com")
                .telefone("11999999999")
                .build();

        ParteEnvolvidaDto dto = mapper.toDto(entity);

        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getNomeCompleto(), dto.getNomeCompleto());
        assertEquals(entity.getEmail(), dto.getEmail());
        assertEquals(entity.getTelefone(), dto.getTelefone());
        assertEquals(entity.getTipoParteEnvolvida(), dto.getTipoParteEnvolvida());
        assertNotNull(dto.getDocumento());
        assertEquals(documento.getValor(), dto.getDocumento().getValor());
    }

    @Test
    void testToEntity() {
        ParteEnvolvidaDocumentoDto documentoDto = ParteEnvolvidaDocumentoDto.builder()
                .id(2L)
                .tipoDocumento(TipoDocumento.CNPJ)
                .valor("12345678000199")
                .build();

        ParteEnvolvidaDto dto = ParteEnvolvidaDto.builder()
                .id(2L)
                .nomeCompleto("Empresa XYZ")
                .documento(documentoDto)
                .tipoParteEnvolvida(TipoParteEnvolvida.REU)
                .email("empresa@xyz.com")
                .telefone("1133334444")
                .build();

        ParteEnvolvida entity = mapper.toEntity(dto);

        assertEquals(dto.getId(), entity.getId());
        assertEquals(dto.getNomeCompleto(), entity.getNomeCompleto());
        assertEquals(dto.getEmail(), entity.getEmail());
        assertEquals(dto.getTelefone(), entity.getTelefone());
        assertEquals(dto.getTipoParteEnvolvida(), entity.getTipoParteEnvolvida());
        assertNotNull(entity.getDocumento());
        assertEquals(documentoDto.getValor(), entity.getDocumento().getValor());
    }

}