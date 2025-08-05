package br.com.attus.gerenciamentoprocessos.dto;

import br.com.attus.gerenciamentoprocessos.model.enums.TipoDocumento;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ParteEnvolvidaDocumentoDto {

    private Long id;

    @NotNull(message = "O tipo do documento é obrigatório")
    private TipoDocumento tipoDocumento;

    @NotEmpty(message = "O valor é obrigatório")
    private String valor;
}
