package br.com.attus.gerenciamentoprocessos.controller;

import br.com.attus.gerenciamentoprocessos.dto.ParteEnvolvidaDto;
import br.com.attus.gerenciamentoprocessos.exceptions.ObrigatoriedadeIdException;
import br.com.attus.gerenciamentoprocessos.mapper.ParteEnvolvidaMapper;
import br.com.attus.gerenciamentoprocessos.model.ParteEnvolvida;
import br.com.attus.gerenciamentoprocessos.model.ParteEnvolvidaDocumento;
import br.com.attus.gerenciamentoprocessos.model.enums.TipoDocumento;
import br.com.attus.gerenciamentoprocessos.model.enums.TipoParteEnvolvida;
import br.com.attus.gerenciamentoprocessos.service.ParteEnvolvidaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ParteEnvolvidaControllerUnitTest {

    @InjectMocks
    private ParteEnvolvidaController controller;

    @Mock
    private ParteEnvolvidaService service;

    @Mock
    private ParteEnvolvidaMapper mapper;

    @Test
    void deveInserirParteEnvolvidaComSucesso() {
        ParteEnvolvidaDto inputDto = new ParteEnvolvidaDto();
        inputDto.setNomeCompleto("Maria Teste");
        inputDto.setTipoParteEnvolvida(TipoParteEnvolvida.AUTOR);
        inputDto.setTelefone("(11) 99999-0000");
        inputDto.setEmail("maria@email.com");

        ParteEnvolvidaDocumento documento = new ParteEnvolvidaDocumento();
        documento.setId(1L);
        documento.setTipoDocumento(TipoDocumento.CPF);
        documento.setValor("123.456.789-00");

        ParteEnvolvida entidade = new ParteEnvolvida();
        entidade.setId(1L);
        entidade.setNomeCompleto("Maria Teste");
        entidade.setTipoParteEnvolvida(TipoParteEnvolvida.AUTOR);
        entidade.setTelefone("(11) 99999-0000");
        entidade.setEmail("maria@email.com");
        entidade.setDocumento(documento);

        ParteEnvolvidaDto retornoDto = new ParteEnvolvidaDto();
        retornoDto.setId(1L);
        retornoDto.setNomeCompleto("Maria Teste");
        retornoDto.setTipoParteEnvolvida(TipoParteEnvolvida.AUTOR);
        retornoDto.setTelefone("(11) 99999-0000");
        retornoDto.setEmail("maria@email.com");

        when(mapper.toEntity(inputDto)).thenReturn(entidade);
        when(service.salvar(entidade)).thenReturn(entidade);
        when(mapper.toDto(entidade)).thenReturn(retornoDto);

        ResponseEntity<ParteEnvolvidaDto> response = controller.inserir(inputDto);

        assertEquals(201, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("Maria Teste", response.getBody().getNomeCompleto());
        assertEquals("maria@email.com", response.getBody().getEmail());

        verify(service).salvar(entidade);
    }

    @Test
    void deveAlterarParteEnvolvidaComSucesso() {
        ParteEnvolvidaDto dto = new ParteEnvolvidaDto();
        dto.setId(1L);
        ParteEnvolvida entity = new ParteEnvolvida();

        when(mapper.toEntity(dto)).thenReturn(entity);
        when(service.salvar(entity)).thenReturn(entity);
        when(mapper.toDto(entity)).thenReturn(dto);

        ResponseEntity<ParteEnvolvidaDto> response = controller.alterar(dto);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(dto, response.getBody());
    }

    @Test
    void deveLancarExcecaoQuandoAlterarSemId() {
        ParteEnvolvidaDto dto = new ParteEnvolvidaDto();
        assertThrows(ObrigatoriedadeIdException.class, () -> controller.alterar(dto));
    }

    @Test
    void deveBuscarParteEnvolvidaPorIdComSucesso() {
        Long id = 1L;
        ParteEnvolvida entity = new ParteEnvolvida();
        ParteEnvolvidaDto dto = new ParteEnvolvidaDto();
        dto.setId(id);

        when(service.buscarPorId(id)).thenReturn(entity);
        when(mapper.toDto(entity)).thenReturn(dto);

        ResponseEntity<ParteEnvolvidaDto> response = controller.buscarPorId(id);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(dto, response.getBody());
    }

    @Test
    void deveExcluirParteEnvolvidaComSucesso() {
        Long id = 1L;

        ResponseEntity<Void> response = controller.excluir(id);

        verify(service).excluir(id);
        assertEquals(204, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

}
