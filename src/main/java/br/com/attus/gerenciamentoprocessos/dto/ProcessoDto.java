package br.com.attus.gerenciamentoprocessos.dto;

import br.com.attus.gerenciamentoprocessos.model.enums.StatusProcesso;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDate;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProcessoDto extends RepresentationModel<ProcessoDto> {

    @NotNull(message = "Data de abertura é obrigatória")
    private LocalDate dataAbertura;

    @NotEmpty(message = "Descrição do caso é obrigatória")
    private String descricaoCaso;

    @NotNull(message = "Status do processo é obrigatório")
    private StatusProcesso status;

    @NotNull(message = "Andamento processual é obrigatório")
    private AndamentoProcessualDto andamentoProcessual;

    @NotEmpty(message = "É necessário informar pelo menos uma parte envolvida")
    private List<ParteEnvolvidaDto> partesEnvolvidas;

}
