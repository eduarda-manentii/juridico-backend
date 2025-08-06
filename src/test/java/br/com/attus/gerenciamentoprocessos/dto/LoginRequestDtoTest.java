package br.com.attus.gerenciamentoprocessos.dto;


import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class LoginRequestDtoTest {

    private static Validator validator;

    @BeforeAll
    static void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void deveValidarLoginRequestDtoValido() {
        LoginRequestDto dto = LoginRequestDto.builder()
                .email("usuario@teste.com")
                .senha("minhasenha")
                .build();

        Set<ConstraintViolation<LoginRequestDto>> violations = validator.validate(dto);
        assertThat(violations).isEmpty();
    }

    @Test
    void deveValidarCamposObrigatoriosNulosOuVazios() {
        LoginRequestDto dto = new LoginRequestDto(); // email = null, senha = null

        Set<ConstraintViolation<LoginRequestDto>> violations = validator.validate(dto);

        assertThat(violations).hasSize(2);

        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("email")
                && v.getMessage().equals("O email é obrigatório"));

        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("senha")
                && v.getMessage().equals("A senha é obrigatória"));
    }

    @Test
    void deveValidarSenhaNaoPodeSerVazia() {
        LoginRequestDto dto = LoginRequestDto.builder()
                .email("usuario@teste.com")
                .senha("")
                .build();

        Set<ConstraintViolation<LoginRequestDto>> violations = validator.validate(dto);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getPropertyPath().toString()).isEqualTo("senha");
        assertThat(violations.iterator().next().getMessage()).isEqualTo("A senha é obrigatória");
    }

    @Test
    void deveTestarEqualsHashCodeToStringCanEqual() {
        LoginRequestDto dto1 = new LoginRequestDto();
        dto1.setEmail("usuario@example.com");
        dto1.setSenha("senha123");

        LoginRequestDto dto2 = new LoginRequestDto();
        dto2.setEmail("usuario@example.com");
        dto2.setSenha("senha123");

        LoginRequestDto dto3 = new LoginRequestDto();
        dto3.setEmail("outro@example.com");
        dto3.setSenha("outrasenha");
        assertThat(dto1).isEqualTo(dto2);
        assertThat(dto1.hashCode()).isEqualTo(dto2.hashCode());
        assertThat(dto1).isNotEqualTo(dto3);
        assertThat(dto1.hashCode()).isNotEqualTo(dto3.hashCode());
        assertThat(dto1.toString()).isNotNull().isNotEmpty();
        assertThat(dto1.canEqual(dto2)).isTrue();
        assertThat(dto1.canEqual("string qualquer")).isFalse();
    }

}