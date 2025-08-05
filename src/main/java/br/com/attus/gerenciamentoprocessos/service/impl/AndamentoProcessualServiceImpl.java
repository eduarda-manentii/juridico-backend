package br.com.attus.gerenciamentoprocessos.service.impl;

import br.com.attus.gerenciamentoprocessos.model.AndamentoProcessual;
import br.com.attus.gerenciamentoprocessos.repository.AndamentosProcessuaisRepository;
import br.com.attus.gerenciamentoprocessos.service.AndamentoProcessualService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AndamentoProcessualServiceImpl implements AndamentoProcessualService {

    private final AndamentosProcessuaisRepository andamentosProcessuaisRepository;

    public AndamentoProcessualServiceImpl(AndamentosProcessuaisRepository andamentosProcessuaisRepository) {
        this.andamentosProcessuaisRepository = andamentosProcessuaisRepository;
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
        this.andamentosProcessuaisRepository.deleteById(id);
    }
}
