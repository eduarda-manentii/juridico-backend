package br.com.attus.gerenciamentoprocessos.model;

import br.com.attus.gerenciamentoprocessos.model.enums.TipoDocumento;
import br.com.attus.gerenciamentoprocessos.model.enums.TipoParteEnvolvida;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class ParteEnvolvidaTest {

    private ParteEnvolvidaDocumento documento;
    private ParteEnvolvida parte1;
    private ParteEnvolvida parte2;

    @Nested
    class Dado_uma_parte_envolvida_builder_e_uma_parte_envolvida_setters {

        @BeforeEach
        void setUp() {
            documento = ParteEnvolvidaDocumento.builder()
                    .id(1L)
                    .tipoDocumento(TipoDocumento.CPF)
                    .valor("12345678900")
                    .build();

            parte1 = ParteEnvolvida.builder()
                    .id(1L)
                    .nomeCompleto("João Silva")
                    .tipoParteEnvolvida(TipoParteEnvolvida.AUTOR)
                    .documento(documento)
                    .email("joao@email.com")
                    .telefone("99999-9999")
                    .processos(Collections.emptyList())
                    .build();

            parte2 = new ParteEnvolvida();
            parte2.setId(1L);
            parte2.setNomeCompleto("João Silva");
            parte2.setTipoParteEnvolvida(TipoParteEnvolvida.AUTOR);
            parte2.setDocumento(documento);
            parte2.setEmail("joao@email.com");
            parte2.setTelefone("99999-9999");
            parte2.setProcessos(Collections.emptyList());
        }

        @Test
        void Entao_equals_e_hashcode_devem_ser_iguais() {
            assertEquals(parte1, parte2);
            assertEquals(parte1.hashCode(), parte2.hashCode());
        }

        @Test
        void Entao_toString_deve_conter_nome() {
            assertTrue(parte1.toString().contains("João Silva"));
            assertTrue(parte2.toString().contains("João Silva"));
        }

        @Test
        void Entao_getters_devem_retornar_valores_corretos() {
            assertEquals("João Silva", parte1.getNomeCompleto());
            assertEquals("99999-9999", parte1.getTelefone());
            assertEquals("joao@email.com", parte1.getEmail());
            assertEquals(TipoParteEnvolvida.AUTOR, parte1.getTipoParteEnvolvida());
            assertEquals(documento, parte1.getDocumento());
            assertEquals(Collections.emptyList(), parte1.getProcessos());
        }
    }

}
