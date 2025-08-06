package br.com.attus.gerenciamentoprocessos.service.impl;

import br.com.attus.gerenciamentoprocessos.exceptions.DuplicidadeDocumentoException;
import br.com.attus.gerenciamentoprocessos.exceptions.EntidadeEmUsoException;
import br.com.attus.gerenciamentoprocessos.model.ParteEnvolvida;
import br.com.attus.gerenciamentoprocessos.model.ParteEnvolvidaDocumento;
import br.com.attus.gerenciamentoprocessos.model.enums.TipoDocumento;
import br.com.attus.gerenciamentoprocessos.model.enums.TipoParteEnvolvida;
import br.com.attus.gerenciamentoprocessos.repository.PartesEnvolvidasDocumentosRepository;
import br.com.attus.gerenciamentoprocessos.repository.PartesEnvolvidasRepository;
import br.com.attus.gerenciamentoprocessos.repository.ProcessosRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ParteEnvolvidaServiceImplTest {

    @InjectMocks
    private ParteEnvolvidaServiceImpl service;

    @Mock
    private PartesEnvolvidasRepository partesRepository;

    @Mock
    private PartesEnvolvidasDocumentosRepository documentosRepository;

    @Mock
    private ProcessosRepository processosRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private ParteEnvolvida criarParteEnvolvida(Long id) {
        ParteEnvolvidaDocumento doc = new ParteEnvolvidaDocumento();
        doc.setId(1L);
        doc.setValor("123.456.789-00");
        doc.setTipoDocumento(TipoDocumento.CPF);

        ParteEnvolvida parte = new ParteEnvolvida();
        parte.setId(id);
        parte.setDocumento(doc);
        parte.setTelefone("(48) 99999-9999");
        parte.setTipoParteEnvolvida(TipoParteEnvolvida.AUTOR);
        return parte;
    }

    @Test
    void deveSalvarParteEnvolvida_NovoSemDuplicidade() {
        ParteEnvolvida parte = criarParteEnvolvida(null);
        parte.getDocumento().setValor("12345678900");

        when(documentosRepository.findById(1L)).thenReturn(Optional.of(parte.getDocumento()));

        when(partesRepository.findByTipoParteEnvolvidaAndDocumentoValor(any(), any())).thenReturn(Collections.emptyList());
        when(partesRepository.save(any())).thenReturn(parte);

        ParteEnvolvida resultado = service.salvar(parte);

        assertNotNull(resultado);
        verify(partesRepository).save(parte);
    }

    @Test
    void deveSalvarParteEnvolvida_AtualizacaoSemAlterarDocumento() {
        ParteEnvolvida parte = criarParteEnvolvida(1L);
        parte.getDocumento().setValor("12345678900");

        ParteEnvolvida existente = criarParteEnvolvida(1L);
        existente.getDocumento().setValor("12345678900");

        when(partesRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(documentosRepository.findById(1L)).thenReturn(Optional.of(parte.getDocumento())); // <-- mock necessário
        when(partesRepository.save(any())).thenReturn(parte);

        ParteEnvolvida resultado = service.salvar(parte);

        assertEquals(parte, resultado);
        verify(partesRepository).save(parte);
    }

    @Test
    void deveLancarDuplicidadeAoSalvarComMesmoDocumentoDeOutraParte() {
        ParteEnvolvida parte = criarParteEnvolvida(null);
        parte.getDocumento().setId(null);  // importante para não buscar no repo de documentos
        parte.getDocumento().setValor("12345678900");

        ParteEnvolvida outro = criarParteEnvolvida(2L);
        outro.getDocumento().setValor("12345678900");

        when(partesRepository.findByTipoParteEnvolvidaAndDocumentoValor(any(), any()))
                .thenReturn(List.of(outro));

        assertThrows(DuplicidadeDocumentoException.class, () -> service.salvar(parte));
    }

    @Test
    void deveBuscarPorId_ComSucesso() {
        ParteEnvolvida parte = criarParteEnvolvida(1L);
        when(partesRepository.findById(1L)).thenReturn(Optional.of(parte));

        ParteEnvolvida resultado = service.buscarPorId(1L);

        assertEquals(parte, resultado);
    }

    @Test
    void deveLancarExcecaoAoBuscarPorIdInexistente() {
        when(partesRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.buscarPorId(99L));
    }

    @Test
    void deveExcluir_ComSucesso() {
        when(processosRepository.existsByPartesEnvolvidas_Id(1L)).thenReturn(false);

        service.excluir(1L);

        verify(partesRepository).deleteById(1L);
    }

    @Test
    void deveLancarExcecao_SeParteEstiverEmUso() {
        when(processosRepository.existsByPartesEnvolvidas_Id(1L)).thenReturn(true);

        assertThrows(EntidadeEmUsoException.class, () -> service.excluir(1L));
        verify(partesRepository, never()).deleteById(any());
    }

    @Test
    void deveListarPorIds() {
        List<Long> ids = List.of(1L, 2L);
        List<ParteEnvolvida> partes = List.of(criarParteEnvolvida(1L), criarParteEnvolvida(2L));

        when(partesRepository.findAllById(ids)).thenReturn(partes);

        List<ParteEnvolvida> resultado = service.listarPorIds(ids);

        assertEquals(2, resultado.size());
    }

    @Test
    void deveValidarDocumentoCpfInvalido() {
        ParteEnvolvida parte = criarParteEnvolvida(null);
        parte.getDocumento().setValor("123"); // CPF inválido

        parte.setTelefone("(48) 99999-9999");

        assertThrows(IllegalArgumentException.class, () -> service.salvar(parte));
    }

    @Test
    void deveValidarDocumentoCnpjInvalido() {
        ParteEnvolvida parte = criarParteEnvolvida(null);
        parte.getDocumento().setValor("123");
        parte.getDocumento().setTipoDocumento(TipoDocumento.CNPJ);

        assertThrows(IllegalArgumentException.class, () -> service.salvar(parte));
    }

    @Test
    void deveBuscarDocumentoExistenteAoValidarDocumento() {
        ParteEnvolvida parte = criarParteEnvolvida(null);
        parte.getDocumento().setId(1L);
        parte.getDocumento().setValor("12345678900");

        ParteEnvolvidaDocumento documentoExistente = new ParteEnvolvidaDocumento();
        documentoExistente.setId(1L);
        documentoExistente.setValor("12345678900");

        when(partesRepository.findByTipoParteEnvolvidaAndDocumentoValor(any(), any()))
                .thenReturn(Collections.emptyList());
        when(documentosRepository.findById(1L)).thenReturn(Optional.of(documentoExistente));
        when(partesRepository.save(any())).thenReturn(parte);

        ParteEnvolvida resultado = service.salvar(parte);

        assertNotNull(resultado);
        verify(documentosRepository).findById(1L);
    }

}