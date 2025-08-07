package br.com.attus.gerenciamentoprocessos.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsuarioDto extends RepresentationModel<UsuarioDto> {

    private Long id;

    @NotEmpty(message = "Nome é obrigatório")
    private String nome;

    @NotEmpty(message = "Email é obrigatório")
    private String email;

    @NotEmpty(message = "Senha é obrigatória")
    private String senha;

}
