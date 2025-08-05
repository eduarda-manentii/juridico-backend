package br.com.attus.gerenciamentoprocessos.service.impl;

import br.com.attus.gerenciamentoprocessos.model.AndamentoProcessual;
import br.com.attus.gerenciamentoprocessos.model.ParteEnvolvida;
import br.com.attus.gerenciamentoprocessos.model.Processo;
import br.com.attus.gerenciamentoprocessos.model.enums.StatusProcesso;
import br.com.attus.gerenciamentoprocessos.repository.AndamentosProcessuaisRepository;
import br.com.attus.gerenciamentoprocessos.repository.PartesEnvolvidasRepository;
import br.com.attus.gerenciamentoprocessos.repository.ProcessosRepository;
import br.com.attus.gerenciamentoprocessos.service.ProcessoService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
        if (processo.getParteEnvolvida() != null && !processo.getParteEnvolvida().isEmpty()) {
            List<Long> ids = processo.getParteEnvolvida().stream()
                    .map(ParteEnvolvida::getId)
                    .toList();
            List<ParteEnvolvida> partesGerenciadas = partesEnvolvidasRepository.findAllById(ids);
            if (partesGerenciadas.size() != ids.size()) {
                throw new EntityNotFoundException("Uma ou mais partes envolvidas não foram encontradas para os IDs informados.");
            }
            processo.setParteEnvolvida(partesGerenciadas);
        }
        return processosRepository.save(processo);
    }

    @Override
    public Processo buscarPorId(Long id) {
        return processosRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Processo não encontrado para o ID informado."));
    }

    @Override
    public void excluir(Long id) {
        this.processosRepository.deleteById(id);
    }

    @Override
    public void arquivarProcesso(Long id) {
        Processo processo = processosRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Processo com id " + id + " não encontrado"));
        processo.setStatus(StatusProcesso.ARQUIVADO);
        processosRepository.save(processo);
    }

    @Override
    public List<Processo> buscarPorFiltros(StatusProcesso status, LocalDate dataAbertura, String documento) {
        return processosRepository.buscarPorFiltros(status, dataAbertura, documento);
    }

}
