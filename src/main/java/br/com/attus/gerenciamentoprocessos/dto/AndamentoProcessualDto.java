package br.com.attus.gerenciamentoprocessos.dto;

import br.com.attus.gerenciamentoprocessos.model.enums.TipoAndamentoProcessual;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AndamentoProcessualDto extends RepresentationModel<AndamentoProcessualDto> {

    private Long id;

    @NotNull(message = "O tipo do andamento processual é obrigatório")
    private TipoAndamentoProcessual tipoAndamentoProcessual;

    @NotNull(message = "A data de registro é obrigatória")
    private LocalDate dataRegistro;

    @NotEmpty(message = "A descricao é obrigatória")
    private String descricao;

}
