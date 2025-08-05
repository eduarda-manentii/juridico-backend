package br.com.attus.gerenciamentoprocessos.service.impl;

import br.com.attus.gerenciamentoprocessos.model.AndamentoProcessual;
import br.com.attus.gerenciamentoprocessos.model.ParteEnvolvida;
import br.com.attus.gerenciamentoprocessos.model.Processo;
import br.com.attus.gerenciamentoprocessos.repository.AndamentosProcessuaisRepository;
import br.com.attus.gerenciamentoprocessos.repository.PartesEnvolvidasRepository;
import br.com.attus.gerenciamentoprocessos.repository.ProcessosRepository;
import br.com.attus.gerenciamentoprocessos.service.ProcessoService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProcessoServiceImpl implements ProcessoService {

    private final ProcessosRepository  processosRepository;
    private final AndamentosProcessuaisRepository  andamentosProcessuaisRepository;
    private final PartesEnvolvidasRepository partesEnvolvidasRepository;

    public ProcessoServiceImpl(ProcessosRepository processosRepository, AndamentosProcessuaisRepository andamentosProcessuaisRepository, PartesEnvolvidasRepository partesEnvolvidasRepository) {
        this.processosRepository = processosRepository;
        this.andamentosProcessuaisRepository = andamentosProcessuaisRepository;
        this.partesEnvolvidasRepository = partesEnvolvidasRepository;
    }

    @Override
    public Processo salvar(Processo processo) {
        if (processo.getAndamentoProcessual() != null && processo.getAndamentoProcessual().getId() != null) {
            Long andamentoId = processo.getAndamentoProcessual().getId();
            AndamentoProcessual andamento = andamentosProcessuaisRepository.findById(andamentoId)
                    .orElseThrow(() -> new EntityNotFoundException(
                            "AndamentoProcessual com id " + andamentoId + " não encontrado"));
            processo.setAndamentoProcessual(andamento);
        }
        if (processo.getParteEnvolvida() != null) {
            List<ParteEnvolvida> partesGerenciadas = processo.getParteEnvolvida().stream()
                    .map(parte -> {
                        if (parte.getId() == null) return parte; // Nova parte, deixa o persist cuidar
                        return partesEnvolvidasRepository.findById(parte.getId())
                                .orElseThrow(() -> new EntityNotFoundException("ParteEnvolvida com id " + parte.getId() + " não encontrada"));
                    })
                    .collect(Collectors.toList());
            processo.setParteEnvolvida(partesGerenciadas);
        }
        return processosRepository.save(processo);
    }

    @Override
    public Processo buscarPorId(Long id) {
        Optional<Processo> processoEncontrado = processosRepository.findById(id);
        if(processoEncontrado.isPresent()) {
            return processoEncontrado.get();
        } else {
            throw new NullPointerException("Não foi encontrado processo para o id informado.");
        }
    }

    @Override
    public void excluir(Long id) {
        this.processosRepository.deleteById(id);
    }

}
