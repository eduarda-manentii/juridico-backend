package br.com.attus.gerenciamentoprocessos.model;

import br.com.attus.gerenciamentoprocessos.model.enums.TipoDocumento;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ParteEnvolvidaDocumentoTest {

    private ParteEnvolvidaDocumento doc1;
    private ParteEnvolvidaDocumento doc2;

    @Nested
    class Dado_um_documento_builder_e_um_documento_setters {

        @BeforeEach
        void setUp() {
            doc1 = ParteEnvolvidaDocumento.builder()
                    .id(1L)
                    .tipoDocumento(TipoDocumento.CPF)
                    .valor("12345678900")
                    .build();

            doc2 = new ParteEnvolvidaDocumento();
            doc2.setId(1L);
            doc2.setTipoDocumento(TipoDocumento.CPF);
            doc2.setValor("12345678900");
        }

        @Test
        void Entao_equals_e_hashcode_devem_ser_iguais() {
            assertEquals(doc1, doc2);
            assertEquals(doc1.hashCode(), doc2.hashCode());
        }

        @Test
        void Entao_toString_deve_conter_valor() {
            assertTrue(doc1.toString().contains("12345678900"));
            assertTrue(doc2.toString().contains("12345678900"));
        }

        @Test
        void Entao_getters_devem_retornar_valores_corretos() {
            assertEquals(1L, doc1.getId());
            assertEquals(TipoDocumento.CPF, doc1.getTipoDocumento());
            assertEquals("12345678900", doc1.getValor());
        }
    }

}
