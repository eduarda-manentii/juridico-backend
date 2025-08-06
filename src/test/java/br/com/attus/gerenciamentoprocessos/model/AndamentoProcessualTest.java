package br.com.attus.gerenciamentoprocessos.model;

import br.com.attus.gerenciamentoprocessos.model.enums.TipoAndamentoProcessual;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class AndamentoProcessualTest {

    @Test
    void deveTestarEqualsHashCodeToStringBuilderESetters() {
        LocalDate dataRegistro = LocalDate.of(2025, 8, 6);

        AndamentoProcessual a1 = AndamentoProcessual.builder()
                .id(1L)
                .tipoAndamentoProcessual(TipoAndamentoProcessual.SENTENCA)
                .dataRegistro(dataRegistro)
                .descricao("Sentença proferida")
                .build();

        AndamentoProcessual a2 = new AndamentoProcessual();
        a2.setId(1L);
        a2.setTipoAndamentoProcessual(TipoAndamentoProcessual.SENTENCA);
        a2.setDataRegistro(dataRegistro);
        a2.setDescricao("Sentença proferida");

        assertEquals(a1, a2);
        assertEquals(a1.hashCode(), a2.hashCode());
        assertTrue(a1.toString().contains("Sentença"));
        assertTrue(a2.toString().contains("Sentença"));
        assertEquals(1L, a1.getId());
        assertEquals(TipoAndamentoProcessual.SENTENCA, a1.getTipoAndamentoProcessual());
        assertEquals(dataRegistro, a1.getDataRegistro());
        assertEquals("Sentença proferida", a1.getDescricao());
    }

}