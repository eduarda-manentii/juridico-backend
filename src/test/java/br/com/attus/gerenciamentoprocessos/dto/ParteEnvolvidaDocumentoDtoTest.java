package br.com.attus.gerenciamentoprocessos.dto;


import br.com.attus.gerenciamentoprocessos.model.enums.TipoDocumento;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class ParteEnvolvidaDocumentoDtoTest {

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
    void deveValidarCamposObrigatoriosNulosOuVazios() {
        ParteEnvolvidaDocumentoDto dto = new ParteEnvolvidaDocumentoDto();

        Set<ConstraintViolation<ParteEnvolvidaDocumentoDto>> violations = validator.validate(dto);

        assertThat(violations).hasSize(2);

        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("tipoDocumento")
                && v.getMessage().equals("O tipo do documento é obrigatório"));

        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("valor")
                && v.getMessage().equals("O valor é obrigatório"));
    }

    @Test
    void deveCriarDtoValido() {
        ParteEnvolvidaDocumentoDto dto = ParteEnvolvidaDocumentoDto.builder()
                .id(1L)
                .tipoDocumento(TipoDocumento.CPF)
                .valor("123.456.789-00")
                .build();

        Set<ConstraintViolation<ParteEnvolvidaDocumentoDto>> violations = validator.validate(dto);
        assertThat(violations).isEmpty();

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getTipoDocumento()).isEqualTo(TipoDocumento.CPF);
        assertThat(dto.getValor()).isEqualTo("123.456.789-00");
    }

    @Test
    void deveTestarEqualsHashCodeToStringCanEqual() {
        ParteEnvolvidaDocumentoDto dto1 = new ParteEnvolvidaDocumentoDto();
        dto1.setId(1L);
        dto1.setTipoDocumento(TipoDocumento.CPF);
        dto1.setValor("123.456.789-00");

        ParteEnvolvidaDocumentoDto dto2 = new ParteEnvolvidaDocumentoDto();
        dto2.setId(1L);
        dto2.setTipoDocumento(TipoDocumento.CPF);
        dto2.setValor("123.456.789-00");

        ParteEnvolvidaDocumentoDto dto3 = new ParteEnvolvidaDocumentoDto();
        dto3.setId(2L);
        dto3.setTipoDocumento(TipoDocumento.CPF);
        dto3.setValor("000.000.000-46");

        assertThat(dto1).isEqualTo(dto2);
        assertThat(dto1.hashCode()).isEqualTo(dto2.hashCode());
        assertThat(dto1).isNotEqualTo(dto3);
        assertThat(dto1.hashCode()).isNotEqualTo(dto3.hashCode());
        assertThat(dto1.toString()).isNotNull().isNotEmpty();
        assertThat(dto1.canEqual(dto2)).isTrue();
        assertThat(dto1.canEqual("uma string qualquer")).isFalse();
    }

}