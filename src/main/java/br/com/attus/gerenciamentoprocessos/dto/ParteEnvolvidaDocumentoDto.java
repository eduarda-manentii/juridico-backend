package br.com.attus.gerenciamentoprocessos.dto;

import br.com.attus.gerenciamentoprocessos.model.enums.TipoDocumento;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ParteEnvolvidaDocumentoDto {

    @NotNull(message = "O tipo do documento é obrigatório")
    private TipoDocumento tipoDocumento;

    @NotEmpty(message = "O valor é obrigatório")
    private String valor;
}
