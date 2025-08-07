package br.com.attus.gerenciamentoprocessos.model;

import br.com.attus.gerenciamentoprocessos.model.enums.StatusProcesso;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProcessoTest {

    private Processo processo1;
    private Processo processo2;

    @Nested
    class Dado_um_processo_builder_e_um_processo_setters {

        @BeforeEach
        void setUp() {
            processo1 = Processo.builder()
                    .id(1L)
                    .dataAbertura(LocalDate.of(2023, 1, 1))
                    .descricaoCaso("Caso de teste")
                    .partesEnvolvidas(List.of())
                    .andamentoProcessual(null)
                    .status(StatusProcesso.ATIVO)
                    .build();

            processo2 = new Processo();
            processo2.setId(1L);
            processo2.setDataAbertura(LocalDate.of(2023, 1, 1));
            processo2.setDescricaoCaso("Caso de teste");
            processo2.setPartesEnvolvidas(List.of());
            processo2.setAndamentoProcessual(null);
            processo2.setStatus(StatusProcesso.ATIVO);
        }

        @Test
        void Entao_equals_e_hashcode_devem_ser_iguais() {
            assertEquals(processo1, processo2);
            assertEquals(processo1.hashCode(), processo2.hashCode());
        }

        @Test
        void Entao_toString_deve_conter_descricao() {
            assertTrue(processo1.toString().contains("Caso de teste"));
            assertTrue(processo2.toString().contains("Caso de teste"));
        }

        @Test
        void Entao_getters_devem_retornar_valores() {
            assertEquals(1L, processo1.getId());
            assertEquals(LocalDate.of(2023, 1, 1), processo1.getDataAbertura());
            assertEquals("Caso de teste", processo1.getDescricaoCaso());
            assertEquals(List.of(), processo1.getPartesEnvolvidas());
            assertNull(processo1.getAndamentoProcessual());
            assertEquals(StatusProcesso.ATIVO, processo1.getStatus());
        }
    }

}
