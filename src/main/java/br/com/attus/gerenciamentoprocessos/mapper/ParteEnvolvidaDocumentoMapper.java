package br.com.attus.gerenciamentoprocessos.mapper;

import br.com.attus.gerenciamentoprocessos.dto.ParteEnvolvidaDocumentoDto;
import br.com.attus.gerenciamentoprocessos.dto.ParteEnvolvidaDto;
import br.com.attus.gerenciamentoprocessos.model.ParteEnvolvida;
import br.com.attus.gerenciamentoprocessos.model.ParteEnvolvidaDocumento;
import jakarta.persistence.Column;
import org.springframework.stereotype.Component;

@Component
public class ParteEnvolvidaDocumentoMapper {

    public ParteEnvolvidaDocumentoDto toDto(ParteEnvolvidaDocumento parteEnvolvidaDocumento) {
        return ParteEnvolvidaDocumentoDto.builder()
                .id(parteEnvolvidaDocumento.getId())
                .tipoDocumento(parteEnvolvidaDocumento.getTipoDocumento())
                .valor(parteEnvolvidaDocumento.getValor())
                .build();
    }

    public ParteEnvolvidaDocumento toEntity(ParteEnvolvidaDocumentoDto parteEnvolvidaDocumentoDto) {
        return ParteEnvolvidaDocumento.builder()
                .id(parteEnvolvidaDocumentoDto.getId())
                .tipoDocumento(parteEnvolvidaDocumentoDto.getTipoDocumento())
                .valor(parteEnvolvidaDocumentoDto.getValor())
                .build();
    }

}
