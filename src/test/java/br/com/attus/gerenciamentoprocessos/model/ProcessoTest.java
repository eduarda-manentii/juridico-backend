package br.com.attus.gerenciamentoprocessos.model;


import br.com.attus.gerenciamentoprocessos.model.enums.StatusProcesso;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class ProcessoTest {

    @Test
    void deveTestarEqualsHashCodeToStringESetters() {
        Processo processo1 = Processo.builder()
                .id(1L)
                .dataAbertura(LocalDate.of(2023, 1, 1))
                .descricaoCaso("Caso de teste")
                .partesEnvolvidas(List.of())
                .andamentoProcessual(null)
                .status(StatusProcesso.ATIVO)
                .build();

        Processo processo2 = new Processo();
        processo2.setId(1L);
        processo2.setDataAbertura(LocalDate.of(2023, 1, 1));
        processo2.setDescricaoCaso("Caso de teste");
        processo2.setPartesEnvolvidas(List.of());
        processo2.setAndamentoProcessual(null);
        processo2.setStatus(StatusProcesso.ATIVO);
        assertEquals(processo1, processo2);
        assertEquals(processo1.hashCode(), processo2.hashCode());
        assertTrue(processo1.toString().contains("Caso de teste"));
        assertTrue(processo2.toString().contains("Caso de teste"));
        assertEquals(1L, processo1.getId());
        assertEquals(LocalDate.of(2023, 1, 1), processo1.getDataAbertura());
        assertEquals("Caso de teste", processo1.getDescricaoCaso());
        assertEquals(List.of(), processo1.getPartesEnvolvidas());
        assertNull(processo1.getAndamentoProcessual());
        assertEquals(StatusProcesso.ATIVO, processo1.getStatus());
    }

}