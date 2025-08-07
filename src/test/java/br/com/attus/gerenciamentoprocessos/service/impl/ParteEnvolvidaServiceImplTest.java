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
import org.junit.jupiter.api.Nested;
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

    private ParteEnvolvida parte;
    private ParteEnvolvidaDocumento documento;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        documento = new ParteEnvolvidaDocumento();
        documento.setId(1L);
        documento.setValor("12345678900");
        documento.setTipoDocumento(TipoDocumento.CPF);

        parte = new ParteEnvolvida();
        parte.setId(1L);
        parte.setDocumento(documento);
        parte.setTelefone("(48) 99999-9999");
        parte.setTipoParteEnvolvida(TipoParteEnvolvida.AUTOR);
    }

    @Nested
    class Dado_salvar {

        @Test
        void Deve_salvar_uma_parte_envolvida_nova_sem_duplicidade() {
            parte.setId(null);
            parte.getDocumento().setId(1L);

            when(documentosRepository.findById(1L)).thenReturn(Optional.of(documento));
            when(partesRepository.findByTipoParteEnvolvidaAndDocumentoValor(any(), any()))
                    .thenReturn(Collections.emptyList());
            when(partesRepository.save(any())).thenReturn(parte);

            ParteEnvolvida resultado = service.salvar(parte);

            assertNotNull(resultado);
            verify(partesRepository).save(parte);
        }

        @Test
        void Deve_atualizar_uma_parte_envolvida_existente_sem_alterar_documento() {
            ParteEnvolvida existente = new ParteEnvolvida();
            existente.setId(1L);
            existente.setDocumento(documento);

            when(partesRepository.findById(1L)).thenReturn(Optional.of(existente));
            when(documentosRepository.findById(1L)).thenReturn(Optional.of(documento));
            when(partesRepository.save(any())).thenReturn(parte);

            ParteEnvolvida resultado = service.salvar(parte);

            assertEquals(parte, resultado);
            verify(partesRepository).save(parte);
        }

        @Test
        void Deve_lancar_excecao_quando_tentar_salvar_com_documento_duplicado() {
            parte.setId(null);
            parte.setDocumento(new ParteEnvolvidaDocumento());
            parte.getDocumento().setValor("12345678900");
            parte.getDocumento().setId(null);

            ParteEnvolvida outraParte = new ParteEnvolvida();
            outraParte.setId(2L);
            ParteEnvolvidaDocumento outroDoc = new ParteEnvolvidaDocumento();
            outroDoc.setValor("12345678900");
            outraParte.setDocumento(outroDoc);

            when(partesRepository.findByTipoParteEnvolvidaAndDocumentoValor(any(), any()))
                    .thenReturn(List.of(outraParte));

            assertThrows(DuplicidadeDocumentoException.class, () -> service.salvar(parte));
        }

        @Test
        void Deve_validar_documento_cpf_invalido() {
            parte.getDocumento().setValor("123");
            assertThrows(IllegalArgumentException.class, () -> service.salvar(parte));
        }

        @Test
        void Deve_validar_documento_cnpj_invalido() {
            parte.getDocumento().setValor("123");
            parte.getDocumento().setTipoDocumento(TipoDocumento.CNPJ);

            assertThrows(IllegalArgumentException.class, () -> service.salvar(parte));
        }

        @Test
        void Deve_buscar_documento_existente_ao_validar_documento() {
            parte.getDocumento().setId(1L);

            when(partesRepository.findByTipoParteEnvolvidaAndDocumentoValor(any(), any()))
                    .thenReturn(Collections.emptyList());

            when(documentosRepository.findById(1L)).thenReturn(Optional.of(documento));

            when(partesRepository.findById(parte.getId())).thenReturn(Optional.of(parte));

            when(partesRepository.save(any())).thenReturn(parte);

            ParteEnvolvida resultado = service.salvar(parte);

            assertNotNull(resultado);
            verify(documentosRepository).findById(1L);
        }
    }

    @Nested
    class Dado_buscar_por_id {

        @Test
        void Deve_retornar_parte_envolvida_quando_existir() {
            when(partesRepository.findById(1L)).thenReturn(Optional.of(parte));

            ParteEnvolvida resultado = service.buscarPorId(1L);

            assertEquals(parte, resultado);
        }

        @Test
        void Deve_lancar_excecao_quando_parte_envolvida_nao_existir() {
            when(partesRepository.findById(99L)).thenReturn(Optional.empty());

            assertThrows(EntityNotFoundException.class, () -> service.buscarPorId(99L));
        }
    }

    @Nested
    class Dado_excluir {

        @Test
        void Deve_excluir_quando_parte_nao_estiver_em_uso() {
            when(processosRepository.existsByPartesEnvolvidasId(1L)).thenReturn(false);

            service.excluir(1L);

            verify(partesRepository).deleteById(1L);
        }

        @Test
        void Deve_lancar_excecao_quando_parte_estiver_em_uso() {
            when(processosRepository.existsByPartesEnvolvidasId(1L)).thenReturn(true);

            assertThrows(EntidadeEmUsoException.class, () -> service.excluir(1L));
            verify(partesRepository, never()).deleteById(any());
        }
    }

    @Nested
    class Dado_listar_por_ids {

        @Test
        void Deve_retornar_lista_correspondente() {
            List<Long> ids = List.of(1L, 2L);
            List<ParteEnvolvida> partes = List.of(parte, criarParteEnvolvida(2L));

            when(partesRepository.findAllById(ids)).thenReturn(partes);

            List<ParteEnvolvida> resultado = service.listarPorIds(ids);

            assertEquals(2, resultado.size());
        }
    }

    private ParteEnvolvida criarParteEnvolvida(Long id) {
        ParteEnvolvidaDocumento doc = new ParteEnvolvidaDocumento();
        doc.setId(1L);
        doc.setValor("12345678900");
        doc.setTipoDocumento(TipoDocumento.CPF);

        ParteEnvolvida p = new ParteEnvolvida();
        p.setId(id);
        p.setDocumento(doc);
        p.setTelefone("(48) 99999-9999");
        p.setTipoParteEnvolvida(TipoParteEnvolvida.AUTOR);
        return p;
    }
}
