package br.com.attus.gerenciamentoprocessos.controller;

import br.com.attus.gerenciamentoprocessos.Mocker;
import br.com.attus.gerenciamentoprocessos.dto.ParteEnvolvidaDto;
import br.com.attus.gerenciamentoprocessos.exceptions.ObrigatoriedadeIdException;
import br.com.attus.gerenciamentoprocessos.mapper.ParteEnvolvidaMapper;
import br.com.attus.gerenciamentoprocessos.model.ParteEnvolvida;
import br.com.attus.gerenciamentoprocessos.service.ParteEnvolvidaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ParteEnvolvidaControllerTest {

    @Mock
    private ParteEnvolvidaService service;

    @Mock
    private ParteEnvolvidaMapper mapper;

    private ParteEnvolvidaController controller;

    private Mocker mocker = new Mocker();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new ParteEnvolvidaController(service, mapper);
    }

    @Nested
    class Dada_uma_parte_envolvida {

        private ParteEnvolvida entidade;
        private ParteEnvolvidaDto dto;

        @BeforeEach
        void init() {
            entidade = mocker.gerarParteEnvolvida(1L);
            dto = mocker.gerarParteEnvolvidaDto(1L);
        }

        @Nested
        class Quando_excluir {

            @Nested
            class Quando_nao_eh_possivel_excluir {

                @Test
                void Entao_deve_lancar_excecao() {
                    doThrow(new IllegalStateException("Não é possível excluir")).when(service).excluir(1L);

                    IllegalStateException exc = assertThrows(IllegalStateException.class, () -> controller.excluir(1L));

                    assertEquals("Não é possível excluir", exc.getMessage());
                    verify(service).excluir(1L);
                }
            }

            @Nested
            class Quando_excluir_com_sucesso {

                @Test
                void Entao_deve_retornar_status_204_e_corpo_nulo() {
                    doNothing().when(service).excluir(1L);

                    ResponseEntity<Void> response = controller.excluir(1L);

                    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
                    assertNull(response.getBody());
                    verify(service).excluir(1L);
                }
            }
        }

        @Nested
        class Quando_buscar_por_id {

            @Nested
            class Quando_nao_encontrar {

                @Test
                void Entao_deve_retornar_404() {
                    when(service.buscarPorId(1L)).thenThrow(new NoSuchElementException("Não encontrado"));

                    ResponseEntity<ParteEnvolvidaDto> response = controller.buscarPorId(1L);

                    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
                    assertNull(response.getBody());
                    verify(service).buscarPorId(1L);
                }
            }

            @Nested
            class Quando_encontrar {

                @Test
                void Entao_deve_retornar_status_200_e_dto() {
                    when(service.buscarPorId(1L)).thenReturn(entidade);
                    when(mapper.toDto(entidade)).thenReturn(dto);

                    ResponseEntity<ParteEnvolvidaDto> response = controller.buscarPorId(1L);

                    assertEquals(HttpStatus.OK, response.getStatusCode());
                    assertEquals(dto, response.getBody());
                    verify(service).buscarPorId(1L);
                    verify(mapper).toDto(entidade);
                }
            }
        }

        @Nested
        class Quando_alterar_sem_id {

            @Test
            void Entao_deve_lancar_obrigatoriedade_id_exception() {
                ParteEnvolvidaDto dtoSemId = mocker.gerarParteEnvolvidaDto(null);

                assertThrows(ObrigatoriedadeIdException.class, () -> controller.alterar(dtoSemId));
            }
        }

        @Nested
        class Quando_alterar {

            @Test
            void Entao_deve_alterar_e_retornar_status_200_e_dto() {
                when(mapper.toEntity(dto)).thenReturn(entidade);
                when(service.salvar(entidade)).thenReturn(entidade);
                when(mapper.toDto(entidade)).thenReturn(dto);

                ResponseEntity<ParteEnvolvidaDto> response = controller.alterar(dto);

                assertEquals(HttpStatus.OK, response.getStatusCode());
                assertEquals(dto, response.getBody());

                verify(mapper).toEntity(dto);
                verify(service).salvar(entidade);
                verify(mapper).toDto(entidade);
            }
        }

        @Nested
        class Quando_inserir {

            @Test
            void Entao_deve_retornar_status_201_e_objeto() {
                when(mapper.toEntity(dto)).thenReturn(entidade);
                when(service.salvar(entidade)).thenReturn(entidade);
                when(mapper.toDto(entidade)).thenReturn(dto);

                ResponseEntity<ParteEnvolvidaDto> response = controller.inserir(dto);

                assertEquals(HttpStatus.CREATED, response.getStatusCode());
                assertEquals(dto, response.getBody());

                verify(mapper).toEntity(dto);
                verify(service).salvar(entidade);
                verify(mapper).toDto(entidade);
            }
        }
    }
}
