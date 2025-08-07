package br.com.attus.gerenciamentoprocessos.service.impl;

import br.com.attus.gerenciamentoprocessos.exceptions.EntidadeEmUsoException;
import br.com.attus.gerenciamentoprocessos.model.AndamentoProcessual;
import br.com.attus.gerenciamentoprocessos.repository.AndamentosProcessuaisRepository;
import br.com.attus.gerenciamentoprocessos.repository.ProcessosRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AndamentoProcessualServiceImplTest {

    @InjectMocks
    private AndamentoProcessualServiceImpl service;

    @Mock
    private AndamentosProcessuaisRepository repository;

    @Mock
    private ProcessosRepository processosRepository;

    private AndamentoProcessual andamento;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        andamento = AndamentoProcessual.builder()
                .id(1L)
                .descricao("Descrição do andamento")
                .build();
    }

    @Nested
    class Ao_salvar {

        @Test
        void Deve_salvar_andamento_com_sucesso() {
            when(repository.save(any(AndamentoProcessual.class))).thenReturn(andamento);

            AndamentoProcessual resultado = service.salvar(andamento);

            assertNotNull(resultado);
            assertEquals(andamento.getId(), resultado.getId());
            verify(repository).save(andamento);
        }
    }

    @Nested
    class Ao_buscar_por_id {

        @Test
        void Deve_retornar_andamento_quando_existir() {
            when(repository.findById(1L)).thenReturn(Optional.of(andamento));

            AndamentoProcessual resultado = service.buscarPorId(1L);

            assertNotNull(resultado);
            assertEquals(andamento.getId(), resultado.getId());
        }

        @Test
        void Deve_lancar_excecao_quando_andamento_nao_existir() {
            when(repository.findById(99L)).thenReturn(Optional.empty());

            EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                    () -> service.buscarPorId(99L));

            assertEquals("Andamento processual não encontrada para o ID informado.", exception.getMessage());
        }
    }

    @Nested
    class Ao_excluir {

        @Test
        void Deve_excluir_quando_andamento_nao_estiver_em_uso() {
            when(processosRepository.existsByAndamentoProcessualId(1L)).thenReturn(false);
            doNothing().when(repository).deleteById(1L);

            service.excluir(1L);

            verify(processosRepository).existsByAndamentoProcessualId(1L);
            verify(repository).deleteById(1L);
        }

        @Test
        void Deve_lancar_excecao_quando_andamento_estiver_em_uso() {
            when(processosRepository.existsByAndamentoProcessualId(1L)).thenReturn(true);

            EntidadeEmUsoException exception = assertThrows(EntidadeEmUsoException.class,
                    () -> service.excluir(1L));

            assertEquals("Não é possível excluir. Este andamento processual está vinculado a um processo.", exception.getMessage());

            verify(processosRepository).existsByAndamentoProcessualId(1L);
            verify(repository, never()).deleteById(anyLong());
        }
    }

    @Nested
    class Ao_listar_todos {

        @Test
        void Deve_listar_todos_com_paginacao() {
            Pageable pageable = mock(Pageable.class);
            Page<AndamentoProcessual> pageMock = mock(Page.class);

            when(repository.findAll(pageable)).thenReturn(pageMock);

            Page<AndamentoProcessual> resultado = service.listarTodos(pageable);

            assertNotNull(resultado);
            verify(repository).findAll(pageable);
        }
    }
}
