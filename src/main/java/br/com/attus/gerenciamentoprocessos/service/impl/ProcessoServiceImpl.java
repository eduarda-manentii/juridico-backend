package br.com.attus.gerenciamentoprocessos.service.impl;

import br.com.attus.gerenciamentoprocessos.model.AndamentoProcessual;
import br.com.attus.gerenciamentoprocessos.model.ParteEnvolvida;
import br.com.attus.gerenciamentoprocessos.model.Processo;
import br.com.attus.gerenciamentoprocessos.model.enums.StatusProcesso;
import br.com.attus.gerenciamentoprocessos.repository.ProcessosRepository;
import br.com.attus.gerenciamentoprocessos.service.AndamentoProcessualService;
import br.com.attus.gerenciamentoprocessos.service.ParteEnvolvidaService;
import br.com.attus.gerenciamentoprocessos.service.ProcessoService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ProcessoServiceImpl implements ProcessoService {

    private final ProcessosRepository  processosRepository;
    private final AndamentoProcessualService andamentosProcessuaisService;
    private final ParteEnvolvidaService parteEnvolvidaService;

    public ProcessoServiceImpl(
            ProcessosRepository processosRepository,
            AndamentoProcessualService andamentosProcessuaisService,
            ParteEnvolvidaService parteEnvolvidaService) {
        this.processosRepository = processosRepository;
        this.andamentosProcessuaisService = andamentosProcessuaisService;
        this.parteEnvolvidaService = parteEnvolvidaService;
    }

    @Override
    public Processo salvar(Processo processo) {
        if (processo.getAndamentoProcessual() != null && processo.getAndamentoProcessual().getId() != null) {
            Long andamentoId = processo.getAndamentoProcessual().getId();
            AndamentoProcessual andamento = andamentosProcessuaisService.buscarPorId(andamentoId);
            processo.setAndamentoProcessual(andamento);
        }
        if (processo.getPartesEnvolvidas() != null && !processo.getPartesEnvolvidas().isEmpty()) {
            List<Long> ids = processo.getPartesEnvolvidas().stream()
                    .map(ParteEnvolvida::getId)
                    .toList();
            List<ParteEnvolvida> partesGerenciadas = parteEnvolvidaService.listarPorIds(ids);
            if (partesGerenciadas.size() != ids.size()) {
                throw new EntityNotFoundException("Uma ou mais partes envolvidas não foram encontradas para os IDs informados.");
            }
            processo.setPartesEnvolvidas(partesGerenciadas);
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
    public Page<Processo> buscarPorFiltros(
            StatusProcesso status,
            LocalDate dataAbertura,
            String documento,
            Pageable pageable) {
        return processosRepository.buscarPorFiltros(status, dataAbertura, documento, pageable);
    }

    @Override
    public boolean existsByAndamentoProcessual_Id(Long id) {
        return processosRepository.existsByAndamentoProcessual_Id(id);
    }

}
