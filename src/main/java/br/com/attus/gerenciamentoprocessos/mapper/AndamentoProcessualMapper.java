package br.com.attus.gerenciamentoprocessos.mapper;

import br.com.attus.gerenciamentoprocessos.dto.AndamentoProcessualDto;
import br.com.attus.gerenciamentoprocessos.dto.ParteEnvolvidaDto;
import br.com.attus.gerenciamentoprocessos.model.AndamentoProcessual;
import br.com.attus.gerenciamentoprocessos.model.ParteEnvolvida;
import org.springframework.stereotype.Component;

@Component
public class AndamentoProcessualMapper {

    public AndamentoProcessualDto toDto(AndamentoProcessual andamentoProcessual) {
        return AndamentoProcessualDto.builder()
                .id(andamentoProcessual.getId())
                .tipoAndamentoProcessual(andamentoProcessual.getTipoAndamentoProcessual())
                .dataRegistro(andamentoProcessual.getDataRegistro())
                .descricao(andamentoProcessual.getDescricao())
                .build();
    }

    public AndamentoProcessual toEntity(AndamentoProcessualDto andamentoProcessualDto) {
        return AndamentoProcessual.builder()
                .id(andamentoProcessualDto.getId())
                .tipoAndamentoProcessual(andamentoProcessualDto.getTipoAndamentoProcessual())
                .dataRegistro(andamentoProcessualDto.getDataRegistro())
                .descricao(andamentoProcessualDto.getDescricao())
                .build();
    }

}
