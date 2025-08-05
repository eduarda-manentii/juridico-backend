package br.com.attus.gerenciamentoprocessos.service.impl;

import br.com.attus.gerenciamentoprocessos.model.ParteEnvolvida;
import br.com.attus.gerenciamentoprocessos.model.ParteEnvolvidaDocumento;
import br.com.attus.gerenciamentoprocessos.model.enums.TipoDocumento;
import br.com.attus.gerenciamentoprocessos.repository.PartesEnvolvidasRepository;
import br.com.attus.gerenciamentoprocessos.service.ParteEnvolvidaService;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = ParteEnvolvidaServiceImpl.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ParteEnvolvidaServiceImplTest {

    @Mock
    private PartesEnvolvidasRepository partesEnvolvidasRepository;

    private ParteEnvolvidaService parteEnvolvidaService;

    @BeforeEach
    void setUp() {
        parteEnvolvidaService = new ParteEnvolvidaServiceImpl(partesEnvolvidasRepository, null);
    }

    @Nested
    class Dado_uma_partida_envolvida {

        ParteEnvolvida parteEnvolvida;

        @BeforeEach
        void setUp() {
            parteEnvolvida = ParteEnvolvida.builder()
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
        class Quando_salvar {

            @BeforeEach
            void setUp() {
                parteEnvolvidaService.salvar(parteEnvolvida);
            }

            @Test
            void Entao_deve_ter_salvo_corretamente() {
                assertNotNull(parteEnvolvida.getId());
            }

        }

    }

    @Nested
    class Dado_uma_parte_envolvida_salva {

        @Nested
        class Quando_tem_nome_diferente_de_um_documento_ja_cadastrado {

            @BeforeEach
            void setUp() {

            }

        }

        @Nested
        class Quando_parte_tem_nome_e_documento_iguais_mas_tipo_parte_envolvida_diferente {

            @BeforeEach
            void setUp() {

            }

        }

    }

}