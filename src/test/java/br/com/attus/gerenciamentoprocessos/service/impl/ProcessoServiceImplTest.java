package br.com.attus.gerenciamentoprocessos.service.impl;

import br.com.attus.gerenciamentoprocessos.model.AndamentoProcessual;
import br.com.attus.gerenciamentoprocessos.model.ParteEnvolvida;
import br.com.attus.gerenciamentoprocessos.model.Processo;
import br.com.attus.gerenciamentoprocessos.model.enums.StatusProcesso;
import br.com.attus.gerenciamentoprocessos.repository.AndamentosProcessuaisRepository;
import br.com.attus.gerenciamentoprocessos.repository.PartesEnvolvidasRepository;
import br.com.attus.gerenciamentoprocessos.repository.ProcessosRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProcessoServiceImplTest {

    @Mock
    private ProcessosRepository processosRepository;

    @Mock
    private AndamentosProcessuaisRepository andamentosProcessuaisRepository;

    @Mock
    private PartesEnvolvidasRepository partesEnvolvidasRepository;

    @InjectMocks
    private ProcessoServiceImpl service;

    private Processo processo;
    private AndamentoProcessual andamento;
    private ParteEnvolvida parte1, parte2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        service = new ProcessoServiceImpl(
                processosRepository,
                andamentosProcessuaisRepository,
                partesEnvolvidasRepository
        );

        andamento = AndamentoProcessual.builder()
                .id(1L)
                .descricao("Andamento 1")
                .build();

        parte1 = ParteEnvolvida.builder().id(10L).build();
        parte2 = ParteEnvolvida.builder().id(20L).build();

        processo = Processo.builder()
                .id(100L)
                .andamentoProcessual(andamento)
                .partesEnvolvidas(List.of(parte1, parte2))
                .status(StatusProcesso.ATIVO)
                .descricaoCaso("Caso teste")
                .dataAbertura(LocalDate.now())
                .build();
    }

    @Nested
    class Dado_salvar_processo {

        @Test
        void Entao_deve_salvar_com_andamento_e_partes_gerenciadas() {
            when(andamentosProcessuaisRepository.findById(1L)).thenReturn(Optional.of(andamento));
            when(partesEnvolvidasRepository.findAllById(List.of(10L, 20L))).thenReturn(List.of(parte1, parte2));
            when(processosRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

            Processo salvo = service.salvar(processo);

            assertNotNull(salvo);
            assertEquals(2, salvo.getPartesEnvolvidas().size());
            assertEquals(andamento, salvo.getAndamentoProcessual());

            verify(andamentosProcessuaisRepository).findById(1L);
            verify(partesEnvolvidasRepository).findAllById(List.of(10L, 20L));
            verify(processosRepository).save(processo);
        }
    }


    @Nested
    class Dado_buscar_por_id {

        @Test
        void Entao_deve_retornar_processo_quando_existir() {
            when(processosRepository.findById(100L)).thenReturn(Optional.of(processo));

            Processo encontrado = service.buscarPorId(100L);

            assertNotNull(encontrado);
            assertEquals(100L, encontrado.getId());
        }

        @Test
        void Entao_deve_lancar_excecao_quando_processo_nao_existir() {
            when(processosRepository.findById(999L)).thenReturn(Optional.empty());

            EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                    () -> service.buscarPorId(999L));

            assertEquals("Processo não encontrado para o ID informado.", exception.getMessage());
        }
    }

    @Nested
    class Dado_excluir_processo {

        @Test
        void Deve_excluir_com_sucesso() {
            doNothing().when(processosRepository).deleteById(100L);
            service.excluir(100L);
            verify(processosRepository).deleteById(100L);
        }
    }

    @Nested
    class Quando_arquivar_processo {

        @Test
        void Entao_Deve_arquivar_processo_com_sucesso() {
            Processo processoArquivado = Processo.builder()
                    .id(100L)
                    .status(StatusProcesso.ARQUIVADO)
                    .build();

            when(processosRepository.findById(100L)).thenReturn(Optional.of(processo));
            when(processosRepository.save(any())).thenReturn(processoArquivado);

            service.arquivarProcesso(100L);

            assertEquals(StatusProcesso.ARQUIVADO, processo.getStatus());
            verify(processosRepository).save(processo);
        }

        @Test
        void Entao_deve_lancar_excecao_ao_arquivar_processo_inexistente() {
            when(processosRepository.findById(999L)).thenReturn(Optional.empty());
            EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                    () -> service.arquivarProcesso(999L));

            assertEquals("Processo com id 999 não encontrado", exception.getMessage());
        }
    }

    @Nested
    class Quando_buscar_por_filtros {

        @Test
        void Entao_deve_retornar_pagina_de_processos_filtrados() {
            Pageable pageable = PageRequest.of(0, 10);
            Page<Processo> pageMock = new PageImpl<>(List.of(processo));

            when(processosRepository.buscarPorFiltrosComData(StatusProcesso.ATIVO, processo.getDataAbertura(), "12345678900", pageable))
                    .thenReturn(pageMock);

            Page<Processo> resultado = service.buscarPorFiltros(StatusProcesso.ATIVO, processo.getDataAbertura(), "12345678900", pageable);

            assertNotNull(resultado);
            assertEquals(1, resultado.getTotalElements());
        }
    }

    @Nested
    class Quando_verificar_existencia_por_andamento {

        @Test
        void Entao_deve_retornar_true_se_existir() {
            when(processosRepository.existsByAndamentoProcessualId(1L)).thenReturn(true);

            boolean existe = service.existsByAndamentoProcessualId(1L);

            assertTrue(existe);
        }
    }
}
