package br.com.attus.gerenciamentoprocessos.dto;


import br.com.attus.gerenciamentoprocessos.model.enums.TipoParteEnvolvida;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.Link;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class ParteEnvolvidaDtoTest {

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
    void deveValidarCamposObrigatoriosNulos() {
        ParteEnvolvidaDto dto = new ParteEnvolvidaDto();

        Set<ConstraintViolation<ParteEnvolvidaDto>> violations = validator.validate(dto);

        assertThat(violations).hasSize(5);

        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("nomeCompleto")
                && v.getMessage().equals("Nome completo é obrigatório"));

        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("tipoParteEnvolvida")
                && v.getMessage().equals("O tipo da parte envolvida é obrigatório"));

        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("documento")
                && v.getMessage().equals("Documento é obrigatório"));

        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("email")
                && v.getMessage().equals("Email é obrigatório"));

        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("telefone")
                && v.getMessage().equals("Telefone é obrigatório"));
    }

    @Test
    void deveValidarCamposVaziosEEmailInvalido() {
        ParteEnvolvidaDto dto = ParteEnvolvidaDto.builder()
                .nomeCompleto("")
                .tipoParteEnvolvida(TipoParteEnvolvida.ADVOGADO)
                .documento(null)
                .email("email_invalido")
                .telefone("")
                .build();

        Set<ConstraintViolation<ParteEnvolvidaDto>> violations = validator.validate(dto);

        assertThat(violations).hasSize(4);

        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("nomeCompleto")
                && v.getMessage().equals("Nome completo é obrigatório"));

        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("documento")
                && v.getMessage().equals("Documento é obrigatório"));

        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("email")
                && v.getMessage().equals("Email inválido"));

        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("telefone")
                && v.getMessage().equals("Telefone é obrigatório"));
    }

    @Test
    void deveCriarParteEnvolvidaDtoValidoEAdicionarLink() {
        ParteEnvolvidaDocumentoDto documentoDto = new ParteEnvolvidaDocumentoDto();
        // aqui você pode popular campos obrigatórios do documento se necessário

        ParteEnvolvidaDto dto = ParteEnvolvidaDto.builder()
                .id(1L)
                .nomeCompleto("João da Silva")
                .tipoParteEnvolvida(TipoParteEnvolvida.ADVOGADO)
                .documento(documentoDto)
                .email("joao.silva@example.com")
                .telefone("123456789")
                .build();

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getNomeCompleto()).isEqualTo("João da Silva");
        assertThat(dto.getTipoParteEnvolvida()).isEqualTo(TipoParteEnvolvida.ADVOGADO);
        assertThat(dto.getDocumento()).isEqualTo(documentoDto);
        assertThat(dto.getEmail()).isEqualTo("joao.silva@example.com");
        assertThat(dto.getTelefone()).isEqualTo("123456789");

        dto.add(Link.of("http://localhost/api/partes/1").withSelfRel());
        assertThat(dto.getLinks()).hasSize(1);
        assertThat(dto.getLink("self")).isPresent();
        assertThat(dto.getLink("self").get().getHref()).isEqualTo("http://localhost/api/partes/1");
    }

}
