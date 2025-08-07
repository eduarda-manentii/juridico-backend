package br.com.attus.gerenciamentoprocessos.dto;


import br.com.attus.gerenciamentoprocessos.model.enums.TipoAndamentoProcessual;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class AndamentoProcessualDtoTest {

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
        AndamentoProcessualDto dto = new AndamentoProcessualDto();
        Set<ConstraintViolation<AndamentoProcessualDto>> violations = validator.validate(dto);

        assertThat(violations)
                .hasSize(3)
                .anySatisfy(v -> {
                    assertThat(v.getPropertyPath().toString()).isEqualTo("tipoAndamentoProcessual");
                    assertThat(v.getMessage()).isEqualTo("O tipo do andamento processual é obrigatório");
                })
                .anySatisfy(v -> {
                    assertThat(v.getPropertyPath().toString()).isEqualTo("dataRegistro");
                    assertThat(v.getMessage()).isEqualTo("A data de registro é obrigatória");
                })
                .anySatisfy(v -> {
                    assertThat(v.getPropertyPath().toString()).isEqualTo("descricao");
                    assertThat(v.getMessage()).isEqualTo("A descricao é obrigatória");
                });
    }

    @Test
    void deveCriarDtoValido() {
        AndamentoProcessualDto dto = AndamentoProcessualDto.builder()
                .id(1L)
                .tipoAndamentoProcessual(TipoAndamentoProcessual.AUDIENCIA)
                .dataRegistro(LocalDate.now())
                .descricao("Descrição válida")
                .build();

        Set<ConstraintViolation<AndamentoProcessualDto>> violations = validator.validate(dto);
        assertThat(violations).isEmpty();

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getTipoAndamentoProcessual()).isEqualTo(TipoAndamentoProcessual.AUDIENCIA);
        assertThat(dto.getDataRegistro()).isEqualTo(LocalDate.now());
        assertThat(dto.getDescricao()).isEqualTo("Descrição válida");
    }

}