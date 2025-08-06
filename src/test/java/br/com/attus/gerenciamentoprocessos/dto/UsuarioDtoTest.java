package br.com.attus.gerenciamentoprocessos.dto;


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

class UsuarioDtoTest {

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
    void deveValidarCamposObrigatorios() {
        UsuarioDto dto = new UsuarioDto();

        Set<ConstraintViolation<UsuarioDto>> violations = validator.validate(dto);

        assertThat(violations).hasSize(3);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("nome") && v.getMessage().equals("Nome é obrigatório"));
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("email") && v.getMessage().equals("Email é obrigatório"));
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("senha") && v.getMessage().equals("Senha é obrigatória"));
    }

    @Test
    void deveCriarUsuarioDtoComBuilderEAdicionarLink() {
        UsuarioDto dto = UsuarioDto.builder()
                .id(1L)
                .nome("Eduarda")
                .email("eduarda@example.com")
                .senha("123456")
                .build();

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getNome()).isEqualTo("Eduarda");
        assertThat(dto.getEmail()).isEqualTo("eduarda@example.com");
        assertThat(dto.getSenha()).isEqualTo("123456");

        dto.add(Link.of("http://localhost/api/usuarios/1").withSelfRel());
        assertThat(dto.getLinks()).hasSize(1);
        assertThat(dto.getLink("self")).isPresent();
        assertThat(dto.getLink("self").get().getHref()).isEqualTo("http://localhost/api/usuarios/1");
    }

    @Test
    void deveValidarCamposVaziosComoErro() {
        UsuarioDto dto = UsuarioDto.builder()
                .nome("")
                .email("")
                .senha("")
                .build();

        Set<ConstraintViolation<UsuarioDto>> violations = validator.validate(dto);

        assertThat(violations).hasSize(3);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("nome") && v.getMessage().equals("Nome é obrigatório"));
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("email") && v.getMessage().equals("Email é obrigatório"));
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("senha") && v.getMessage().equals("Senha é obrigatória"));
    }

    @Test
    void deveTestarEqualsHashCodeToStringCanEqual() {
        UsuarioDto usuario1 = new UsuarioDto();
        usuario1.setId(1L);
        usuario1.setNome("Maria");
        usuario1.setEmail("maria@teste.com");
        usuario1.setSenha("123456");

        UsuarioDto usuario2 = new UsuarioDto();
        usuario2.setId(1L);
        usuario2.setNome("Maria");
        usuario2.setEmail("maria@teste.com");
        usuario2.setSenha("123456");

        UsuarioDto usuario3 = new UsuarioDto();
        usuario3.setId(2L);
        usuario3.setNome("João");
        usuario3.setEmail("joao@teste.com");
        usuario3.setSenha("senha");

        assertThat(usuario1).isEqualTo(usuario2);
        assertThat(usuario1.hashCode()).isEqualTo(usuario2.hashCode());
        assertThat(usuario1).isNotEqualTo(usuario3);
        assertThat(usuario1.hashCode()).isNotEqualTo(usuario3.hashCode());
        assertThat(usuario1.toString()).isNotNull().isNotEmpty();
        assertThat(usuario1.canEqual(usuario2)).isTrue();
        assertThat(usuario1.canEqual("string qualquer")).isFalse();
    }

}