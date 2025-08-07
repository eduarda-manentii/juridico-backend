package br.com.attus.gerenciamentoprocessos.service.impl;


import br.com.attus.gerenciamentoprocessos.model.AndamentoProcessual;
import br.com.attus.gerenciamentoprocessos.model.ParteEnvolvida;
import br.com.attus.gerenciamentoprocessos.model.Processo;
import br.com.attus.gerenciamentoprocessos.model.enums.StatusProcesso;
import br.com.attus.gerenciamentoprocessos.repository.ProcessosRepository;
import br.com.attus.gerenciamentoprocessos.service.AndamentoProcessualService;
import br.com.attus.gerenciamentoprocessos.service.ParteEnvolvidaService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
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
    private AndamentoProcessualService andamentoService;

    @Mock
    private ParteEnvolvidaService parteEnvolvidaService;

    @InjectMocks
    private ProcessoServiceImpl service;

    private Processo processo;
    private AndamentoProcessual andamento;
    private ParteEnvolvida parte1, parte2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

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

    @Test
    void deveSalvarComAndamentoEPartesGerenciadas() {
        when(andamentoService.buscarPorId(1L)).thenReturn(andamento);
        when(parteEnvolvidaService.listarPorIds(List.of(10L, 20L))).thenReturn(List.of(parte1, parte2));
        when(processosRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Processo salvo = service.salvar(processo);

        assertNotNull(salvo);
        assertEquals(2, salvo.getPartesEnvolvidas().size());
        assertEquals(andamento, salvo.getAndamentoProcessual());

        verify(andamentoService).buscarPorId(1L);
        verify(parteEnvolvidaService).listarPorIds(List.of(10L, 20L));
        verify(processosRepository).save(processo);
    }

    @Test
    void deveLancarExcecaoSePartesNaoEncontradas() {
        when(andamentoService.buscarPorId(1L)).thenReturn(andamento);
        when(parteEnvolvidaService.listarPorIds(List.of(10L, 20L))).thenReturn(List.of(parte1));
        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> service.salvar(processo));

        assertEquals("Uma ou mais partes envolvidas não foram encontradas para os IDs informados.", ex.getMessage());

        verify(andamentoService).buscarPorId(1L);
        verify(parteEnvolvidaService).listarPorIds(List.of(10L, 20L));
        verify(processosRepository, never()).save(any());
    }

    @Test
    void deveBuscarPorIdComSucesso() {
        when(processosRepository.findById(100L)).thenReturn(Optional.of(processo));

        Processo encontrado = service.buscarPorId(100L);

        assertNotNull(encontrado);
        assertEquals(100L, encontrado.getId());
    }

    @Test
    void deveLancarExcecaoAoBuscarPorIdInexistente() {
        when(processosRepository.findById(999L)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> service.buscarPorId(999L));

        assertEquals("Processo não encontrado para o ID informado.", ex.getMessage());
    }

    @Test
    void deveExcluirProcesso() {
        doNothing().when(processosRepository).deleteById(100L);

        service.excluir(100L);

        verify(processosRepository).deleteById(100L);
    }

    @Test
    void deveArquivarProcesso() {
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
    void deveLancarExcecaoAoArquivarProcessoInexistente() {
        when(processosRepository.findById(999L)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> service.arquivarProcesso(999L));

        assertEquals("Processo com id 999 não encontrado", ex.getMessage());
    }

    @Test
    void deveBuscarPorFiltros() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Processo> pageMock = new PageImpl<>(List.of(processo));

        when(processosRepository.buscarPorFiltrosComData(StatusProcesso.ATIVO, processo.getDataAbertura(), "12345678900", pageable))
                .thenReturn(pageMock);

        Page<Processo> resultado = service.buscarPorFiltros(StatusProcesso.ATIVO, processo.getDataAbertura(), "12345678900", pageable);

        assertNotNull(resultado);
        assertEquals(1, resultado.getTotalElements());
    }

    @Test
    void deveVerificarExistenciaPorAndamentoProcessual() {
        when(processosRepository.existsByAndamentoProcessual_Id(1L)).thenReturn(true);

        boolean existe = service.existsByAndamentoProcessual_Id(1L);

        assertTrue(existe);
    }

}