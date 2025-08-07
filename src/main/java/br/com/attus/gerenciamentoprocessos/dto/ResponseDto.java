package br.com.attus.gerenciamentoprocessos.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseDto {

    private Long id;

    @NotNull(message = "O email é obrigatório")
    private String email;

    @NotEmpty(message = "A senha é obrigatória")
    private String token;
}
