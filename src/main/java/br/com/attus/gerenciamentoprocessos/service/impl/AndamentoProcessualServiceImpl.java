package br.com.attus.gerenciamentoprocessos.service.impl;

import br.com.attus.gerenciamentoprocessos.model.AndamentoProcessual;
import br.com.attus.gerenciamentoprocessos.repository.AndamentosProcessuaisRepository;
import br.com.attus.gerenciamentoprocessos.service.AndamentoProcessualService;
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
        Optional<AndamentoProcessual> andamentoProcessualEncontrado = andamentosProcessuaisRepository.findById(id);
        if(andamentoProcessualEncontrado.isPresent()) {
            return andamentoProcessualEncontrado.get();
        } else {
            throw new NullPointerException("Não foi encontrado andamento processual para o id informado.");
        }
    }

    @Override
    public void excluir(Long id) {
        this.andamentosProcessuaisRepository.deleteById(id);
    }
}
