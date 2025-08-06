package br.com.attus.gerenciamentoprocessos.dto;


import br.com.attus.gerenciamentoprocessos.model.enums.StatusProcesso;
import br.com.attus.gerenciamentoprocessos.model.enums.TipoAndamentoProcessual;
import br.com.attus.gerenciamentoprocessos.model.enums.TipoParteEnvolvida;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class ProcessoDtoTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @BeforeAll
    static void setupValidator() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    static void close() {
        validatorFactory.close();
    }

    @Test
    void deveValidarCamposObrigatoriosNulosOuInvalidos() {
        ProcessoDto dto = new ProcessoDto();

        Set<ConstraintViolation<ProcessoDto>> violations = validator.validate(dto);

        assertThat(violations).hasSize(5);

        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("dataAbertura")
                && v.getMessage().equals("Data de abertura é obrigatória"));

        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("descricaoCaso")
                && v.getMessage().equals("Descrição do caso é obrigatória"));

        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("status")
                && v.getMessage().equals("Status do processo é obrigatório"));

        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("andamentoProcessual")
                && v.getMessage().equals("Andamento processual é obrigatório"));

        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("partesEnvolvidas")
                && v.getMessage().equals("É necessário informar pelo menos uma parte envolvida"));
    }

    @Test
    void deveValidarListaPartesEnvolvidasVazia() {
        ProcessoDto dto = ProcessoDto.builder()
                .id(1L)
                .dataAbertura(LocalDate.now())
                .descricaoCaso("Descrição válida")
                .status(StatusProcesso.ATIVO)
                .andamentoProcessual(new AndamentoProcessualDto())
                .partesEnvolvidas(new ArrayList<>()) // lista vazia
                .build();

        Set<ConstraintViolation<ProcessoDto>> violations = validator.validate(dto);

        assertThat(violations).hasSize(1);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("partesEnvolvidas")
                && v.getMessage().equals("É necessário informar pelo menos uma parte envolvida"));
    }

    @Test
    void deveCriarDtoValido() {
        AndamentoProcessualDto andamento = AndamentoProcessualDto.builder()
                .id(1L)
                .tipoAndamentoProcessual(TipoAndamentoProcessual.AUDIENCIA)
                .dataRegistro(LocalDate.now())
                .descricao("Descrição andamento")
                .build();

        ParteEnvolvidaDto parte = ParteEnvolvidaDto.builder()
                .id(1L)
                .nomeCompleto("Fulano de Tal")
                .tipoParteEnvolvida(TipoParteEnvolvida.AUTOR)
                .documento(new ParteEnvolvidaDocumentoDto())
                .email("email@exemplo.com")
                .telefone("999999999")
                .build();

        ProcessoDto dto = ProcessoDto.builder()
                .id(1L)
                .dataAbertura(LocalDate.now())
                .descricaoCaso("Descrição válida")
                .status(StatusProcesso.ATIVO)
                .andamentoProcessual(andamento)
                .partesEnvolvidas(List.of(parte))
                .build();

        Set<ConstraintViolation<ProcessoDto>> violations = validator.validate(dto);
        assertThat(violations).isEmpty();
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getDescricaoCaso()).isEqualTo("Descrição válida");
        assertThat(dto.getPartesEnvolvidas()).hasSize(1);
    }


    @Test
    void deveTestarSettersEGetters() {
        ProcessoDto dto = new ProcessoDto();
        dto.setId(10L);
        dto.setDataAbertura(LocalDate.of(2023, 7, 1));
        dto.setDescricaoCaso("Descrição do caso");
        dto.setStatus(StatusProcesso.ATIVO);

        AndamentoProcessualDto andamento = new AndamentoProcessualDto();
        andamento.setId(5L);
        andamento.setTipoAndamentoProcessual(TipoAndamentoProcessual.AUDIENCIA);
        andamento.setDataRegistro(LocalDate.of(2023, 7, 2));
        andamento.setDescricao("Andamento descrição");
        dto.setAndamentoProcessual(andamento);

        dto.setPartesEnvolvidas(Collections.emptyList());

        assertThat(dto.getId()).isEqualTo(10L);
        assertThat(dto.getDataAbertura()).isEqualTo(LocalDate.of(2023, 7, 1));
        assertThat(dto.getDescricaoCaso()).isEqualTo("Descrição do caso");
        assertThat(dto.getStatus()).isEqualTo(StatusProcesso.ATIVO);
        assertThat(dto.getAndamentoProcessual()).isEqualTo(andamento);
        assertThat(dto.getPartesEnvolvidas()).isEmpty();
    }

    @Test
    void deveTestarEqualsHashCodeToString() {
        ProcessoDto dto1 = ProcessoDto.builder()
                .id(1L)
                .dataAbertura(LocalDate.of(2023, 1, 1))
                .descricaoCaso("Caso teste")
                .status(StatusProcesso.ATIVO)
                .andamentoProcessual(new AndamentoProcessualDto())
                .partesEnvolvidas(Collections.emptyList())
                .build();

        ProcessoDto dto2 = ProcessoDto.builder()
                .id(1L)
                .dataAbertura(LocalDate.of(2023, 1, 1))
                .descricaoCaso("Caso teste")
                .status(StatusProcesso.ATIVO)
                .andamentoProcessual(new AndamentoProcessualDto())
                .partesEnvolvidas(Collections.emptyList())
                .build();
        assertThat(dto1).isEqualTo(dto2);
        assertThat(dto1.hashCode()).isEqualTo(dto2.hashCode());
        assertThat(dto1.toString()).isNotNull().isNotEmpty();
    }

    @Test
    void deveTestarBuilderEConstrutores() {
        AndamentoProcessualDto andamento = new AndamentoProcessualDto(
                1L,
                TipoAndamentoProcessual.AUDIENCIA,
                LocalDate.of(2023, 1, 1),
                "Descrição andamento"
        );

        ProcessoDto dto = ProcessoDto.builder()
                .id(1L)
                .dataAbertura(LocalDate.of(2023, 1, 1))
                .descricaoCaso("Descrição caso")
                .status(StatusProcesso.ATIVO)
                .andamentoProcessual(andamento)
                .partesEnvolvidas(Collections.emptyList())
                .build();

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getDataAbertura()).isEqualTo(LocalDate.of(2023, 1, 1));
        assertThat(dto.getDescricaoCaso()).isEqualTo("Descrição caso");
        assertThat(dto.getStatus()).isEqualTo(StatusProcesso.ATIVO);
        assertThat(dto.getAndamentoProcessual()).isEqualTo(andamento);
        assertThat(dto.getPartesEnvolvidas()).isEmpty();
        ProcessoDto dtoDefault = new ProcessoDto();
        assertThat(dtoDefault).isNotNull();
    }

    @Test
    void deveTestarEqualsHashCodeToStringESetterDocumento() {
        ParteEnvolvidaDocumentoDto documento1 = new ParteEnvolvidaDocumentoDto();
        documento1.setId(1L);
        documento1.setTipoDocumento(null);
        documento1.setValor("123456");

        ParteEnvolvidaDocumentoDto documento2 = new ParteEnvolvidaDocumentoDto();
        documento2.setId(1L);
        documento2.setTipoDocumento(null);
        documento2.setValor("123456");

        ParteEnvolvidaDto dto1 = new ParteEnvolvidaDto();
        dto1.setDocumento(documento1);
        dto1.setNomeCompleto("Fulano");
        dto1.setTipoParteEnvolvida(TipoParteEnvolvida.REU);
        dto1.setEmail("email@teste.com");
        dto1.setTelefone("123456789");

        ParteEnvolvidaDto dto2 = new ParteEnvolvidaDto();
        dto2.setDocumento(documento2);
        dto2.setNomeCompleto("Fulano");
        dto2.setTipoParteEnvolvida(TipoParteEnvolvida.ADVOGADO);
        dto2.setEmail("email@teste.com");
        dto2.setTelefone("123456789");

        ParteEnvolvidaDocumentoDto novoDocumento = new ParteEnvolvidaDocumentoDto();
        novoDocumento.setId(2L);
        dto1.setDocumento(novoDocumento);
        assertThat(dto1.getDocumento()).isEqualTo(novoDocumento);

        assertThat(dto1).isNotEqualTo(dto2);
        assertThat(dto1.hashCode()).isNotEqualTo(dto2.hashCode());

        assertThat(dto1.toString()).isNotNull().isNotEmpty();
    }

}