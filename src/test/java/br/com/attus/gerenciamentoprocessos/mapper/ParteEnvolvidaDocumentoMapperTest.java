package br.com.attus.gerenciamentoprocessos.mapper;

import static org.junit.jupiter.api.Assertions.*;


import br.com.attus.gerenciamentoprocessos.dto.ParteEnvolvidaDocumentoDto;
import br.com.attus.gerenciamentoprocessos.model.ParteEnvolvidaDocumento;
import br.com.attus.gerenciamentoprocessos.model.enums.TipoDocumento;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ParteEnvolvidaDocumentoMapperTest {

    private ParteEnvolvidaDocumentoMapper mapper;

    @BeforeEach
    void setup() {
        mapper = new ParteEnvolvidaDocumentoMapper();
    }

    @Test
    void testToDto() {
        ParteEnvolvidaDocumento entity = ParteEnvolvidaDocumento.builder()
                .id(10L)
                .tipoDocumento(TipoDocumento.CPF)
                .valor("12345678900")
                .build();

        ParteEnvolvidaDocumentoDto dto = mapper.toDto(entity);

        assertNotNull(dto);
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getTipoDocumento(), dto.getTipoDocumento());
        assertEquals(entity.getValor(), dto.getValor());
    }

    @Test
    void testToEntity() {
        ParteEnvolvidaDocumentoDto dto = ParteEnvolvidaDocumentoDto.builder()
                .id(20L)
                .tipoDocumento(TipoDocumento.CNPJ)
                .valor("12345678000199")
                .build();

        ParteEnvolvidaDocumento entity = mapper.toEntity(dto);

        assertNotNull(entity);
        assertEquals(dto.getId(), entity.getId());
        assertEquals(dto.getTipoDocumento(), entity.getTipoDocumento());
        assertEquals(dto.getValor(), entity.getValor());
    }

}