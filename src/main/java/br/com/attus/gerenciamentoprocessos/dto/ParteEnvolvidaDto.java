package br.com.attus.gerenciamentoprocessos.dto;

import br.com.attus.gerenciamentoprocessos.model.enums.TipoParteEnvolvida;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ParteEnvolvidaDto extends RepresentationModel<ParteEnvolvidaDto> {

    private Long id;

    @NotEmpty(message = "Nome completo é obrigatório")
    private String nomeCompleto;

    @NotNull(message = "O tipo da parte envolvida é obrigatório")
    private TipoParteEnvolvida tipoParteEnvolvida;

    @Valid
    @NotNull(message = "Documento é obrigatório")
    private ParteEnvolvidaDocumentoDto documento;

    @Email(message = "Email inválido")
    @NotEmpty(message = "Email é obrigatório")
    private String email;

    @NotEmpty(message = "Telefone é obrigatório")
    private String telefone;

}
