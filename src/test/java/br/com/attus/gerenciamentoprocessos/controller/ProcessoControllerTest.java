package br.com.attus.gerenciamentoprocessos.controller;

import br.com.attus.gerenciamentoprocessos.dto.ProcessoDto;
import br.com.attus.gerenciamentoprocessos.exceptions.ObrigatoriedadeIdException;
import br.com.attus.gerenciamentoprocessos.mapper.ProcessoMapper;
import br.com.attus.gerenciamentoprocessos.model.Processo;
import br.com.attus.gerenciamentoprocessos.model.enums.StatusProcesso;
import br.com.attus.gerenciamentoprocessos.service.ProcessoService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
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
class ProcessoControllerTest {

    @InjectMocks
    private ProcessoController controller;

    @Mock
    private ProcessoService service;

    @Mock
    private ProcessoMapper mapper;

    private Processo processo;
    private ProcessoDto dto;

    @BeforeEach
    void setup() {
        processo = new Processo();
        processo.setId(1L);
        dto = new ProcessoDto();
        dto.setId(1L);
    }

    @Nested
    class Dado_um_processo {

        @Nested
        class Quando_inserir {

            ResponseEntity<ProcessoDto> response;

            @BeforeEach
            void setup() {
                when(mapper.toEntity(dto)).thenReturn(processo);
                when(service.salvar(processo)).thenReturn(processo);
                when(mapper.toDto(processo)).thenReturn(dto);

                response = controller.inserir(dto);
            }

            @Test
            void Entao_deve_retornar_status_201_e_objeto() {
                assertEquals(201, response.getStatusCodeValue());
                assertEquals(dto, response.getBody());
            }
        }

        @Nested
        class Quando_alterar {

            ResponseEntity<ProcessoDto> response;

            @BeforeEach
            void setup() {
                when(mapper.toEntity(dto)).thenReturn(processo);
                when(service.salvar(processo)).thenReturn(processo);
                when(mapper.toDto(processo)).thenReturn(dto);

                response = controller.alterar(dto);
            }

            @Test
            void Entao_deve_retornar_status_200_e_objeto() {
                assertEquals(200, response.getStatusCodeValue());
                assertEquals(dto, response.getBody());
            }
        }

        @Nested
        class Quando_alterar_sem_id {

            @Test
            void Entao_deve_lancar_excecao() {
                ProcessoDto dtoSemId = new ProcessoDto();
                assertThrows(ObrigatoriedadeIdException.class, () -> controller.alterar(dtoSemId));
            }
        }

        @Nested
        class Quando_buscar_por_id {

            @Nested
            class Quando_encontrar {

                ResponseEntity<ProcessoDto> response;

                @BeforeEach
                void setup() {
                    when(service.buscarPorId(processo.getId())).thenReturn(processo);
                    when(mapper.toDto(processo)).thenReturn(dto);

                    response = controller.buscarPorId(processo.getId());
                }

                @Test
                void Entao_deve_retornar_status_200_e_objeto() {
                    assertEquals(200, response.getStatusCodeValue());
                    assertEquals(dto, response.getBody());
                }
            }

            @Nested
            class Quando_nao_encontrar {

                @Test
                void Entao_deve_lancar_EntityNotFoundException() {
                    when(service.buscarPorId(processo.getId())).thenThrow(new jakarta.persistence.EntityNotFoundException());
                    assertThrows(jakarta.persistence.EntityNotFoundException.class, () -> {
                        controller.buscarPorId(processo.getId());
                    });
                }
            }
        }

        @Nested
        class Quando_excluir {

            ResponseEntity<Void> response;

            @BeforeEach
            void setup() {
                response = controller.excluir(processo.getId());
            }

            @Test
            void entao_deve_chamar_servico_e_retornar_204() {
                verify(service).excluir(processo.getId());
                assertEquals(204, response.getStatusCodeValue());
                assertNull(response.getBody());
            }
        }

        @Nested
        class Quando_arquivar {

            ResponseEntity<Void> response;

            @BeforeEach
            void setup() {
                response = controller.arquivar(processo.getId());
            }

            @Test
            void entao_deve_chamar_servico_e_retornar_204() {
                verify(service).arquivarProcesso(processo.getId());
                assertEquals(204, response.getStatusCodeValue());
                assertNull(response.getBody());
            }
        }

        @Nested
        class Quando_buscar_por_filtros {

            ResponseEntity<Page<ProcessoDto>> response;
            Pageable pageable;
            Page<Processo> page;

            @BeforeEach
            void setup() {
                StatusProcesso status = StatusProcesso.ATIVO;
                LocalDate dataAbertura = LocalDate.of(2023, 1, 1);
                String documento = "123456";

                pageable = PageRequest.of(0, 15);
                page = new PageImpl<>(List.of(processo), pageable, 1);

                when(service.buscarPorFiltros(status, dataAbertura, documento, pageable)).thenReturn(page);
                when(mapper.toDto(processo)).thenReturn(dto);

                response = controller.buscarPorFiltros(status, dataAbertura, documento, 0);
            }

            @Test
            void entao_deve_retornar_status_200_e_pagina() {
                assertEquals(200, response.getStatusCodeValue());
                assertNotNull(response.getBody());
                assertEquals(1, response.getBody().getTotalElements());
                assertEquals(dto, response.getBody().getContent().get(0));
            }
        }
    }

}
