package br.com.attus.gerenciamentoprocessos.service.impl;

import static org.junit.jupiter.api.Assertions.*;


import br.com.attus.gerenciamentoprocessos.exceptions.EntidadeEmUsoException;
import br.com.attus.gerenciamentoprocessos.model.AndamentoProcessual;
import br.com.attus.gerenciamentoprocessos.repository.AndamentosProcessuaisRepository;
import br.com.attus.gerenciamentoprocessos.service.ProcessoService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;

import static org.mockito.Mockito.*;

class AndamentoProcessualServiceImplTest {

    @Mock
    private AndamentosProcessuaisRepository repository;

    @Mock
    private ProcessoService processoService;

    @InjectMocks
    private AndamentoProcessualServiceImpl service;

    private AndamentoProcessual andamento;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        andamento = AndamentoProcessual.builder()
                .id(1L)
                .descricao("Descrição do andamento")
                .build();
    }

    @Test
    void deveSalvarAndamento() {
        when(repository.save(any(AndamentoProcessual.class))).thenReturn(andamento);

        AndamentoProcessual resultado = service.salvar(andamento);

        assertNotNull(resultado);
        assertEquals(andamento.getId(), resultado.getId());
        verify(repository).save(andamento);
    }

    @Test
    void deveBuscarPorId_Sucesso() {
        when(repository.findById(1L)).thenReturn(Optional.of(andamento));

        AndamentoProcessual resultado = service.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals(andamento.getId(), resultado.getId());
    }

    @Test
    void deveLancarExcecaoAoBuscarPorIdInexistente() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> service.buscarPorId(99L));

        assertEquals("Andamento processual não encontrada para o ID informado.", exception.getMessage());
    }

    @Test
    void deveExcluirComSucesso() {
        when(processoService.existsByAndamentoProcessual_Id(1L)).thenReturn(false);
        doNothing().when(repository).deleteById(1L);

        service.excluir(1L);

        verify(processoService).existsByAndamentoProcessual_Id(1L);
        verify(repository).deleteById(1L);
    }

    @Test
    void deveLancarExcecaoAoExcluirQuandoEmUso() {
        when(processoService.existsByAndamentoProcessual_Id(1L)).thenReturn(true);

        EntidadeEmUsoException exception = assertThrows(EntidadeEmUsoException.class,
                () -> service.excluir(1L));

        assertEquals("Não é possível excluir. Este andamento processual está vinculado a um processo.", exception.getMessage());

        verify(processoService).existsByAndamentoProcessual_Id(1L);
        verify(repository, never()).deleteById(anyLong());
    }

}
