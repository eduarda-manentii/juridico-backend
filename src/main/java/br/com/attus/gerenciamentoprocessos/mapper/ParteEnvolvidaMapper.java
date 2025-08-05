package br.com.attus.gerenciamentoprocessos.mapper;

import br.com.attus.gerenciamentoprocessos.dto.ParteEnvolvidaDto;
import br.com.attus.gerenciamentoprocessos.model.ParteEnvolvida;
import org.springframework.stereotype.Component;

@Component
public class ParteEnvolvidaMapper {

    public ParteEnvolvidaDto toDto(ParteEnvolvida parteEnvolvida) {
        return ParteEnvolvidaDto.builder()
                .id(parteEnvolvida.getId())
                .nomeCompleto(parteEnvolvida.getNomeCompleto())
                .documento(parteEnvolvida.getDocumento())
                .tipoParteEnvolvida(parteEnvolvida.getTipoParteEnvolvida())
                .email(parteEnvolvida.getEmail())
                .telefone(parteEnvolvida.getTelefone())
                .build();
    }

    public ParteEnvolvida toEntity(ParteEnvolvidaDto parteEnvolvidaDto) {
        return ParteEnvolvida.builder()
                .id(parteEnvolvidaDto.getId())
                .nomeCompleto(parteEnvolvidaDto.getNomeCompleto())
                .documento(parteEnvolvidaDto.getDocumento())
                .tipoParteEnvolvida(parteEnvolvidaDto.getTipoParteEnvolvida())
                .email(parteEnvolvidaDto.getEmail())
                .telefone(parteEnvolvidaDto.getTelefone())
                .build();
    }

}
