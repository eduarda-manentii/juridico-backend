package br.com.attus.gerenciamentoprocessos.model;

import br.com.attus.gerenciamentoprocessos.model.enums.TipoDocumento;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ParteEnvolvidaDocumentoTest {

    @Test
    void deveTestarEqualsHashCodeToStringESetters() {
        ParteEnvolvidaDocumento doc1 = ParteEnvolvidaDocumento.builder()
                .id(1L)
                .tipoDocumento(TipoDocumento.CPF)
                .valor("12345678900")
                .build();

        ParteEnvolvidaDocumento doc2 = new ParteEnvolvidaDocumento();
        doc2.setId(1L);
        doc2.setTipoDocumento(TipoDocumento.CPF);
        doc2.setValor("12345678900");

        assertEquals(doc1, doc2);
        assertEquals(doc1.hashCode(), doc2.hashCode());
        assertTrue(doc1.toString().contains("12345678900"));
        assertTrue(doc2.toString().contains("12345678900"));
        assertEquals(1L, doc1.getId());
        assertEquals(TipoDocumento.CPF, doc1.getTipoDocumento());
        assertEquals("12345678900", doc1.getValor());
    }
}