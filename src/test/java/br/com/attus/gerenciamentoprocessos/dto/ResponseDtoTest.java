package br.com.attus.gerenciamentoprocessos.dto;


import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class ResponseDtoTest {

    private static Validator validator;

    @BeforeAll
    static void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void deveValidarResponseDtoValido() {
        ResponseDto dto = ResponseDto.builder()
                .email("teste@email.com")
                .token("123456")
                .build();

        Set<ConstraintViolation<ResponseDto>> violations = validator.validate(dto);
        assertThat(violations).isEmpty();
    }

    @Test
    void deveValidarCamposObrigatoriosNulosOuVazios() {
        ResponseDto dto = new ResponseDto(); // email = null, token = null

        Set<ConstraintViolation<ResponseDto>> violations = validator.validate(dto);

        assertThat(violations).hasSize(2);

        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("email")
                && v.getMessage().equals("O email é obrigatório"));

        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("token")
                && v.getMessage().equals("A senha é obrigatória"));
    }

    @Test
    void deveValidarTokenNaoPodeSerVazio() {
        ResponseDto dto = ResponseDto.builder()
                .email("teste@email.com")
                .token("")
                .build();

        Set<ConstraintViolation<ResponseDto>> violations = validator.validate(dto);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getPropertyPath().toString()).isEqualTo("token");
        assertThat(violations.iterator().next().getMessage()).isEqualTo("A senha é obrigatória");
    }

    @Test
    void deveTestarEqualsHashCodeToStringCanEqual() {
        ResponseDto response1 = new ResponseDto();
        response1.setEmail("teste@email.com");
        response1.setToken("token123");

        ResponseDto response2 = new ResponseDto();
        response2.setEmail("teste@email.com");
        response2.setToken("token123");

        ResponseDto response3 = new ResponseDto();
        response3.setEmail("outro@email.com");
        response3.setToken("outroToken");

        assertThat(response1).isEqualTo(response2);
        assertThat(response1.hashCode()).isEqualTo(response2.hashCode());
        assertThat(response1).isNotEqualTo(response3);
        assertThat(response1.hashCode()).isNotEqualTo(response3.hashCode());
        assertThat(response1.toString()).isNotNull().isNotEmpty();
        assertThat(response1.canEqual(response2)).isTrue();
        assertThat(response1.canEqual("qualquer coisa")).isFalse();
    }

}