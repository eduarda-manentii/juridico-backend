package br.com.attus.gerenciamentoprocessos.mapper;

import br.com.attus.gerenciamentoprocessos.dto.ParteEnvolvidaDto;
import br.com.attus.gerenciamentoprocessos.dto.ProcessoDto;
import br.com.attus.gerenciamentoprocessos.model.Processo;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProcessoMapper {

    private final ParteEnvolvidaMapper parteEnvolvidaMapper;
    private final AndamentoProcessualMapper  andamentoProcessualMapper;

    public ProcessoMapper(
            ParteEnvolvidaMapper parteEnvolvidaMapper,
            AndamentoProcessualMapper andamentoProcessualMapper) {
        this.parteEnvolvidaMapper = parteEnvolvidaMapper;
        this.andamentoProcessualMapper = andamentoProcessualMapper;
    }


    public ProcessoDto toDto(Processo processo) {
        List<ParteEnvolvidaDto> lista = new java.util.ArrayList<>(processo.getPartesEnvolvidas()
                .stream()
                .map(parteEnvolvidaMapper::toDto)
                .toList());
        Collections.reverse(lista);
        return ProcessoDto.builder()
                .id(processo.getId())
                .dataAbertura(processo.getDataAbertura())
                .descricaoCaso(processo.getDescricaoCaso())
                .partesEnvolvidas(lista)
                .andamentoProcessual(andamentoProcessualMapper.toDto(processo.getAndamentoProcessual()))
                .status(processo.getStatus())
                .build();
    }

    public Processo toEntity(ProcessoDto processoDto) {
        return Processo.builder()
                .id(processoDto.getId())
                .dataAbertura(processoDto.getDataAbertura())
                .descricaoCaso(processoDto.getDescricaoCaso())
                .partesEnvolvidas(processoDto.getPartesEnvolvidas()
                        .stream()
                        .map(parteEnvolvidaMapper::toEntity)
                        .collect(Collectors.toList()))
                .andamentoProcessual(andamentoProcessualMapper.toEntity(processoDto.getAndamentoProcessual()))
                .status(processoDto.getStatus())
                .build();
    }

}
