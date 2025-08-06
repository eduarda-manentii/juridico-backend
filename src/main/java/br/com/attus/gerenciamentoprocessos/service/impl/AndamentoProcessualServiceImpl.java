package br.com.attus.gerenciamentoprocessos.service.impl;

import br.com.attus.gerenciamentoprocessos.exceptions.EntidadeEmUsoException;
import br.com.attus.gerenciamentoprocessos.model.AndamentoProcessual;
import br.com.attus.gerenciamentoprocessos.repository.AndamentosProcessuaisRepository;
import br.com.attus.gerenciamentoprocessos.repository.ProcessosRepository;
import br.com.attus.gerenciamentoprocessos.service.AndamentoProcessualService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AndamentoProcessualServiceImpl implements AndamentoProcessualService {

    private final AndamentosProcessuaisRepository andamentosProcessuaisRepository;

    private final ProcessosRepository processosRepository;

    public AndamentoProcessualServiceImpl(
            AndamentosProcessuaisRepository andamentosProcessuaisRepository,
            ProcessosRepository processosRepository) {
        this.andamentosProcessuaisRepository = andamentosProcessuaisRepository;
        this.processosRepository = processosRepository;
    }

    @Override
    public AndamentoProcessual salvar(AndamentoProcessual andamentoProcessual) {
        return andamentosProcessuaisRepository.save(andamentoProcessual);
    }

    @Override
    public AndamentoProcessual buscarPorId(Long id) {
        return andamentosProcessuaisRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Andamento processual não encontrada para o ID informado."));
    }

    @Override
    public void excluir(Long id) {
        boolean existeEmProcesso = processosRepository.existsByAndamentoProcessual_Id(id);
        if (existeEmProcesso) {
            throw new EntidadeEmUsoException("Não é possível excluir. Este andamento processual está vinculado a um processo.");
        }
        andamentosProcessuaisRepository.deleteById(id);
    }
}
