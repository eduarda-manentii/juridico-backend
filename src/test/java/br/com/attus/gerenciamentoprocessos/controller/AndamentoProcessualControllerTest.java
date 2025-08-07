package br.com.attus.gerenciamentoprocessos.controller;

import br.com.attus.gerenciamentoprocessos.dto.AndamentoProcessualDto;
import br.com.attus.gerenciamentoprocessos.exceptions.ObrigatoriedadeIdException;
import br.com.attus.gerenciamentoprocessos.mapper.AndamentoProcessualMapper;
import br.com.attus.gerenciamentoprocessos.model.AndamentoProcessual;
import br.com.attus.gerenciamentoprocessos.service.AndamentoProcessualService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AndamentoProcessualControllerTest {

    @InjectMocks
    private AndamentoProcessualController controller;

    @Mock
    private AndamentoProcessualService service;

    @Mock
    private AndamentoProcessualMapper mapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    class Dado_um_andamento_processual {

        @Nested
        class Quando_inserir {

            @Test
            void Entao_deve_retornar_status_201_e_objeto() {
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
        }

        @Nested
        class Dado_alterar {

            @Test
            void Entao_deve_alterar_com_sucesso() {
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
            void Entao_deve_lancar_excecao_quando_alterar_sem_id() {
                AndamentoProcessualDto dto = new AndamentoProcessualDto();
                assertThrows(ObrigatoriedadeIdException.class, () -> controller.alterar(dto));
            }
        }

        @Nested
        class Dado_buscar_por_id {

            @Test
            void Entao_deve_encontrar_e_retornar_200_e_objeto() {
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
            void Quando_nao_encontrar_deve_retornar_404() {
                Long id = 1L;
                when(service.buscarPorId(id)).thenThrow(new NoSuchElementException("Não encontrado"));

                ResponseEntity<AndamentoProcessualDto> response = controller.buscarPorId(id);

                assertEquals(404, response.getStatusCodeValue());
                assertNull(response.getBody());
            }
        }

        @Nested
        class Dado_excluir {

            @Test
            void Quando_excluir_com_sucesso_deve_retornar_204() {
                Long id = 1L;

                ResponseEntity<Void> response = controller.excluir(id);

                verify(service).excluir(id);
                assertEquals(204, response.getStatusCodeValue());
                assertNull(response.getBody());
            }

            @Test
            void Quando_excluir_com_erro_deve_lancar_excecao() {
                Long id = 1L;
                doThrow(new RuntimeException("Erro ao excluir")).when(service).excluir(id);

                RuntimeException ex = assertThrows(RuntimeException.class, () -> controller.excluir(id));
                assertEquals("Erro ao excluir", ex.getMessage());
            }
        }
    }

}
