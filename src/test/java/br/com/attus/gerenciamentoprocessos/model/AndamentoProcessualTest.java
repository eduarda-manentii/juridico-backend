package br.com.attus.gerenciamentoprocessos.model;

import br.com.attus.gerenciamentoprocessos.model.enums.TipoAndamentoProcessual;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class AndamentoProcessualTest {

    private AndamentoProcessual a1;
    private AndamentoProcessual a2;
    private LocalDate dataRegistro;

    @Nested
    class Dado_um_andamento_builder_e_um_andamento_setters {

        @BeforeEach
        void setUp() {
            dataRegistro = LocalDate.of(2025, 8, 6);

            a1 = AndamentoProcessual.builder()
                    .id(1L)
                    .tipoAndamentoProcessual(TipoAndamentoProcessual.SENTENCA)
                    .dataRegistro(dataRegistro)
                    .descricao("Sentença proferida")
                    .build();

            a2 = new AndamentoProcessual();
            a2.setId(1L);
            a2.setTipoAndamentoProcessual(TipoAndamentoProcessual.SENTENCA);
            a2.setDataRegistro(dataRegistro);
            a2.setDescricao("Sentença proferida");
        }

        @Test
        void Entao_equals_e_hashcode_devem_ser_iguais() {
            assertEquals(a1, a2);
            assertEquals(a1.hashCode(), a2.hashCode());
        }

        @Test
        void Entao_toString_deve_conter_descricao() {
            assertTrue(a1.toString().contains("Sentença"));
            assertTrue(a2.toString().contains("Sentença"));
        }

        @Test
        void Entao_getters_devem_retornar_valores_corretos() {
            assertEquals(1L, a1.getId());
            assertEquals(TipoAndamentoProcessual.SENTENCA, a1.getTipoAndamentoProcessual());
            assertEquals(dataRegistro, a1.getDataRegistro());
            assertEquals("Sentença proferida", a1.getDescricao());
        }
    }

}
