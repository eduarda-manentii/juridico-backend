package br.com.attus.gerenciamentoprocessos.dto;

import br.com.attus.gerenciamentoprocessos.model.ParteEnvolvidaDocumento;
import br.com.attus.gerenciamentoprocessos.model.enums.TipoParteEnvolvida;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ParteEnvolvidaDto {

    private Long id;
    private String nomeCompleto;
    private TipoParteEnvolvida tipoParteEnvolvida;
    private ParteEnvolvidaDocumento documento;
    private String email;
    private int telefone;

}
