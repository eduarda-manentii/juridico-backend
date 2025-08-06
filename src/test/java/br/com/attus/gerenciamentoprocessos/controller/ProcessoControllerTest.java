package br.com.attus.gerenciamentoprocessos.controller;

import br.com.attus.gerenciamentoprocessos.controller.ProcessoController;
import br.com.attus.gerenciamentoprocessos.dto.ProcessoDto;
import br.com.attus.gerenciamentoprocessos.exceptions.ObrigatoriedadeIdException;
import br.com.attus.gerenciamentoprocessos.mapper.ProcessoMapper;
import br.com.attus.gerenciamentoprocessos.model.Processo;
import br.com.attus.gerenciamentoprocessos.model.enums.StatusProcesso;
import br.com.attus.gerenciamentoprocessos.service.ProcessoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProcessoControllerUnitTest {

    @InjectMocks
    private ProcessoController controller;

    @Mock
    private ProcessoService service;

    @Mock
    private ProcessoMapper mapper;

    @Test
    void deveInserirProcessoComSucesso() {
        ProcessoDto dto = new ProcessoDto();
        Processo entity = new Processo();
        entity.setId(1L);
        dto.setId(1L);

        when(mapper.toEntity(dto)).thenReturn(entity);
        when(service.salvar(entity)).thenReturn(entity);
        when(mapper.toDto(entity)).thenReturn(dto);

        ResponseEntity<ProcessoDto> response = controller.inserir(dto);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals(dto, response.getBody());
    }

    @Test
    void deveAlterarProcessoComSucesso() {
        ProcessoDto dto = new ProcessoDto();
        dto.setId(1L);
        Processo entity = new Processo();
        entity.setId(1L);

        when(mapper.toEntity(dto)).thenReturn(entity);
        when(service.salvar(entity)).thenReturn(entity);
        when(mapper.toDto(entity)).thenReturn(dto);

        ResponseEntity<ProcessoDto> response = controller.alterar(dto);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(dto, response.getBody());
    }

    @Test
    void deveLancarExcecaoQuandoAlterarSemId() {
        ProcessoDto dto = new ProcessoDto(); // id null

        assertThrows(ObrigatoriedadeIdException.class, () -> controller.alterar(dto));
    }

    @Test
    void deveBuscarProcessoPorIdComSucesso() {
        Long id = 1L;
        Processo entity = new Processo();
        ProcessoDto dto = new ProcessoDto();
        dto.setId(id);

        when(service.buscarPorId(id)).thenReturn(entity);
        when(mapper.toDto(entity)).thenReturn(dto);

        ResponseEntity<ProcessoDto> response = controller.buscarPorId(id);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(dto, response.getBody());
    }

    @Test
    void deveExcluirProcessoComSucesso() {
        Long id = 1L;

        ResponseEntity<Void> response = controller.excluir(id);

        verify(service).excluir(id);
        assertEquals(204, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    @Test
    void deveArquivarProcessoComSucesso() {
        Long id = 1L;

        ResponseEntity<Void> response = controller.arquivar(id);

        verify(service).arquivarProcesso(id);
        assertEquals(204, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    @Test
    void deveBuscarProcessosPorFiltrosComSucesso() {
        StatusProcesso status = StatusProcesso.ATIVO;
        LocalDate dataAbertura = LocalDate.of(2023, 1, 1);
        String documento = "123456";

        Processo processo = new Processo();
        processo.setId(1L);

        ProcessoDto dto = new ProcessoDto();
        dto.setId(1L);

        Pageable pageable = PageRequest.of(0, 15);
        Page<Processo> page = new PageImpl<>(List.of(processo), pageable, 1);

        when(service.buscarPorFiltros(status, dataAbertura, documento, pageable)).thenReturn(page);
        when(mapper.toDto(processo)).thenReturn(dto);

        ResponseEntity<Page<ProcessoDto>> response = controller.buscarPorFiltros(status, dataAbertura, documento, 0);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getTotalElements());
        assertEquals(dto, response.getBody().getContent().get(0));
    }

}
