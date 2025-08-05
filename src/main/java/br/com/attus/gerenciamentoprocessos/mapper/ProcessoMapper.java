package br.com.attus.gerenciamentoprocessos.mapper;

import br.com.attus.gerenciamentoprocessos.dto.ParteEnvolvidaDto;
import br.com.attus.gerenciamentoprocessos.dto.ProcessoDto;
import br.com.attus.gerenciamentoprocessos.model.AndamentoProcessual;
import br.com.attus.gerenciamentoprocessos.model.ParteEnvolvida;
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
        List<Long> parteEnvolvidaIds = processo.getParteEnvolvida()
                .stream()
                .map(ParteEnvolvida::getId)
                .collect(Collectors.toList());
        Collections.reverse(parteEnvolvidaIds);
        return ProcessoDto.builder()
                .id(processo.getId())
                .dataAbertura(processo.getDataAbertura())
                .descricaoCaso(processo.getDescricaoCaso())
                .parteEnvolvidaIds(parteEnvolvidaIds)
                .andamentoProcessualId(processo.getAndamentoProcessual() != null ? processo.getAndamentoProcessual().getId() : null)
                .status(processo.getStatus())
                .build();
    }

    public Processo toEntity(ProcessoDto processoDto) {
        Processo processo = Processo.builder()
                .id(processoDto.getId())
                .dataAbertura(processoDto.getDataAbertura())
                .descricaoCaso(processoDto.getDescricaoCaso())
                .status(processoDto.getStatus())
                .build();
        if (processoDto.getAndamentoProcessualId() != null) {
            AndamentoProcessual andamento = new AndamentoProcessual();
            andamento.setId(processoDto.getAndamentoProcessualId());
            processo.setAndamentoProcessual(andamento);
        }
        if (processoDto.getParteEnvolvidaIds() != null) {
            List<ParteEnvolvida> partes = processoDto.getParteEnvolvidaIds()
                    .stream()
                    .map(id -> {
                        ParteEnvolvida p = new ParteEnvolvida();
                        p.setId(id);
                        return p;
                    })
                    .collect(Collectors.toList());
            processo.setParteEnvolvida(partes);
        }
        return processo;
    }

}
