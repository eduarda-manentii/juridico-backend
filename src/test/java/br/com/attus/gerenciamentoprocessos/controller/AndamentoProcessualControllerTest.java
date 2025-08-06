package br.com.attus.gerenciamentoprocessos.controller;

import br.com.attus.gerenciamentoprocessos.dto.AndamentoProcessualDto;
import br.com.attus.gerenciamentoprocessos.exceptions.ObrigatoriedadeIdException;
import br.com.attus.gerenciamentoprocessos.mapper.AndamentoProcessualMapper;
import br.com.attus.gerenciamentoprocessos.model.AndamentoProcessual;
import br.com.attus.gerenciamentoprocessos.service.AndamentoProcessualService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AndamentoProcessualControllerUnitTest {

    @InjectMocks
    private AndamentoProcessualController controller;

    @Mock
    private AndamentoProcessualService service;

    @Mock
    private AndamentoProcessualMapper mapper;

    @Test
    void deveInserirAndamentoComSucesso() {
        AndamentoProcessualDto dto = new AndamentoProcessualDto();
        AndamentoProcessual entity = new AndamentoProcessual();
        entity.setId(1L);
        dto.setId(1L);

        when(mapper.toEntity(dto)).thenReturn(entity);
        when(service.salvar(entity)).thenReturn(entity);
        when(mapper.toDto(entity)).thenReturn(dto);

        ResponseEntity<AndamentoProcessualDto> response = controller.inserir(dto);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals(dto, response.getBody());
    }

    @Test
    void deveAlterarAndamentoComSucesso() {
        AndamentoProcessualDto dto = new AndamentoProcessualDto();
        dto.setId(1L);
        AndamentoProcessual entity = new AndamentoProcessual();
        entity.setId(1L);

        when(mapper.toEntity(dto)).thenReturn(entity);
        when(service.salvar(entity)).thenReturn(entity);
        when(mapper.toDto(entity)).thenReturn(dto);

        ResponseEntity<AndamentoProcessualDto> response = controller.alterar(dto);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(dto, response.getBody());
    }

    @Test
    void deveLancarExcecaoQuandoAlterarSemId() {
        AndamentoProcessualDto dto = new AndamentoProcessualDto(); // id = null

        assertThrows(ObrigatoriedadeIdException.class, () -> controller.alterar(dto));
    }

    @Test
    void deveBuscarAndamentoPorIdComSucesso() {
        Long id = 1L;
        AndamentoProcessual entity = new AndamentoProcessual();
        AndamentoProcessualDto dto = new AndamentoProcessualDto();
        dto.setId(id);

        when(service.buscarPorId(id)).thenReturn(entity);
        when(mapper.toDto(entity)).thenReturn(dto);

        ResponseEntity<AndamentoProcessualDto> response = controller.buscarPorId(id);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(dto, response.getBody());
    }

    @Test
    void deveExcluirAndamentoComSucesso() {
        Long id = 1L;

        ResponseEntity<Void> response = controller.excluir(id);

        verify(service).excluir(id);
        assertEquals(204, response.getStatusCodeValue());
        assertNull(response.getBody());
    }
}
